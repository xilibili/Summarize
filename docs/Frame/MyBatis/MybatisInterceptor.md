MyBatis 允许你在映射语句执行过程中的某一点进行拦截调用。默认情况下，MyBatis 允许使用插件来拦截的方法调用包括：

-   Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)（拦截执行器的方法）
-   ParameterHandler (getParameterObject, setParameters)（拦截参数的处理）
-   ResultSetHandler (handleResultSets, handleOutputParameters)（拦截结果集的处理）
-   StatementHandler (prepare, parameterize, batch, update, query)（拦截Sql语法构建的处理）

这些类中方法的细节可以通过查看每个方法的签名来发现，或者直接查看 MyBatis 发行包中的源代码。 如果你想做的不仅仅是监控方法的调用，那么你最好相当了解要重写的方法的行为。 因为在试图修改或重写已有方法的行为时，很可能会破坏 MyBatis 的核心模块。 这些都是更底层的类和方法，所以使用插件的时候要特别当心。

通过 MyBatis 提供的强大机制，使用插件是非常简单的，只需实现 Interceptor 接口，并指定想要拦截的方法签名即可。

## 1. Interceptor

拦截器均需要实现该 `org.apache.ibatis.plugin.Interceptor` 接口。

## 2. Intercepts 拦截器
```java
@Intercepts({
	@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
```

拦截器的使用需要查看每一个type所提供的方法参数。

Signature 对应 Invocation 构造器，type 为 Invocation.Object，method 为 Invocation.Method，args 为 Invocation.Object[]。

method 对应的 update 包括了最常用的 insert/update/delete 三种操作，因此 update 本身无法直接判断sql为何种执行过程。

args 包含了其余所有的操作信息, 按数组进行存储, 不同的拦截方式有不同的参数顺序, 具体看type接口的方法签名, 然后根据签名解析。

## 3. Object 对象类型

args 参数列表中，Object.class 是特殊的对象类型。如果有数据库统一的实体 Entity 类，即包含表公共字段，比如创建、更新操作对象和时间的基类等，在编写代码时尽量依据该对象来操作，会简单很多。该对象的判断使用
```java
Object parameter = invocation.getArgs()[1];
if (parameter instanceof BaseEntity) {
    BaseEntity entity = (BaseEntity) parameter;
}
```

即可，根据语句执行类型选择对应字段的赋值。

如果参数不是实体，而且具体的参数，那么 Mybatis 也做了一些处理，比如 `@Param("name") String name` 类型的参数，会被包装成 `Map` 接口的实现来处理，即使是原始的 `Map` 也是如此。使用
```java
Object parameter = invocation.getArgs()[1];
if (parameter instanceof Map) {
    Map map = (Map) parameter;
}
```
即可，对具体统一的参数进行赋值。

## 4. SqlCommandType 命令类型

`Executor` 提供的方法中，`update` 包含了 新增，修改和删除类型，无法直接区分，需要借助 `MappedStatement` 类的属性 `SqlCommandType` 来进行判断，该类包含了所有的操作类型
```java
public enum SqlCommandType {
  UNKNOWN, INSERT, UPDATE, DELETE, SELECT, FLUSH;
}
```
毕竟新增和修改的场景，有些参数是有区别的，比如创建时间和更新时间，`update` 时是无需兼顾创建时间字段的。
```java
MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
SqlCommandType commandType = ms.getSqlCommandType();
```

## 4. 打印完整的SQL语句
mybatis.xml配置文件中添加`<plugin>`配置
```xml
<plugins>  
    <plugin interceptor="com.**.**.repository.mybatis.MybatisInterceptor" />  
</plugins>
```
Java实现：
```java
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class MybatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.info("Sql拦截器");
        // 获取xml中的一个select/update/insert/delete节点，是一条SQL语句
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        // 获取参数，if语句成立，表示sql语句有参数，参数格式是map形式
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }

        String sqlId = mappedStatement.getId(); // 获取到节点的id,即sql语句的id
        BoundSql boundSql = mappedStatement.getBoundSql(parameter); // BoundSql就是封装myBatis最终产生的sql类
        Configuration configuration = mappedStatement.getConfiguration(); // 获取节点的配置

        String sql = getSql(configuration, boundSql);
        log.info("[打印Sql]SqlId={}:{}", sqlId, sql);
        return invocation.proceed();
    }

    /**
     * Sql
     *
     * @param configuration Configuration
     * @param boundSql      BoundSql
     *
     * @return String
     */
    public static String getSql(Configuration configuration, BoundSql boundSql) {
        // 获取参数
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // sql语句中多个空格都用一个空格代替
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (CollectionUtils.isEmpty(parameterMappings) || Objects.isNull(parameterObject)) {
            return sql;
        }
        // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        // 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
        } else {
            // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            for (ParameterMapping parameterMapping : parameterMappings) {
                String propertyName = parameterMapping.getProperty();
                if (metaObject.hasGetter(propertyName)) {
                    Object obj = metaObject.getValue(propertyName);
                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                } else if (boundSql.hasAdditionalParameter(propertyName)) {
                    // 该分支是动态sql
                    Object obj = boundSql.getAdditionalParameter(propertyName);
                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                } else {
                    // 打印出缺失，提醒该参数缺失并防止错位
                    sql = sql.replaceFirst("\\?", "缺失");
                }
            }
        }
        return sql;
    }

    /**
     * getParameterValue
     *
     * @param obj Object
     *
     * @return String
     */
    private static String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,
                    DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
```