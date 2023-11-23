## SrpingBoot整合Log4j2

> 官方文档：https://logging.apache.org/log4j/2.x/

### 为什么选择Log4j2

相比与其他的日志系统，log4j2丢数据这种情况少；disruptor技术，在多线程环境下，性能高于logback等10倍以上；利用jdk1.5并发的特性，减少了死锁的发生

log4j2优越的性能其原因在于log4j2使用了LMAX,一个无锁的线程间通信库代替了,logback和log4j之前的队列. 并发性能大大提升。

### 引入Jar包

> springboot默认是用logback的日志框架的，所以需要排除logback，不然会出现jar依赖冲突的报错。

```xml
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-web</artifactId>  
    <exclusions><!-- 去掉springboot默认配置 -->  
        <exclusion>  
            <groupId>org.springframework.boot</groupId>  
            <artifactId>spring-boot-starter-logging</artifactId>  
        </exclusion>  
    </exclusions>  
</dependency> 
<dependency> <!-- 引入log4j2依赖 -->  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-log4j2</artifactId>  
</dependency>
```

### 配置文件模板

```xml
<?xml version="1.0" encoding="UTF-8"?>  
<!--日志级别以及优先级排序: OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL -->  
<!--Configuration后面的status，用来指定log4j本身的打印日志的级别，可以不设置，当设置成trace时，你会看到log4j2内部各种详细输出 -->  
<!--monitorInterval：Log4j能够自动检测修改配置 文件和重新配置本身，设置间隔秒数 -->  
<configuration status="INFO" monitorInterval="30">  

    <properties>  
        <property name="LOG_HOME">~/export/Logs/server/</property>  
        <property name="FILE_NAME">server</property>  
    </properties>  

    <!--先定义所有的appender -->  
       <appenders>  
        <!--这个输出控制台的配置 -->  
         <console name="Console" target="SYSTEM_OUT">  
            <!--输出日志的格式 -->  
             <PatternLayout pattern="%-d{yyyy-MM-dd HH:mm:ss SSS}-->[%t]--[%p]--[%c{1}:%L]%X{mdc} -- %m%n"/>  
        </console>  

        <!-- 这个会打印出所有的info及以下级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档 -->  
         <RollingFile name="RollingFile" fileName="${LOG_HOME}/${FILE_NAME}.log"  
 filePattern="${LOG_HOME}/${FILE_NAME}-%d{yyyy-MM-dd}-%i.log">  
            <!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch） -->  
             <ThresholdFilter level="debug" onMatch="ACCEPT" onMismatch="DENY"/>  
            <PatternLayout pattern="%-d{yyyy-MM-dd HH:mm:ss SSS}-->[%t]--[%p]--[%c{1}:%L]%X{mdc} -- %m%n"/>  
            <Policies>  
                <TimeBasedTriggeringPolicy/>  
                <SizeBasedTriggeringPolicy size="1 GB"/>  
            </Policies>  
        </RollingFile>  
    </appenders>  

    <!--然后定义logger，只有定义了logger并引入的appender，appender才会生效 -->  
     <loggers>   
         <!-- 可以单独配置打印， appender-ref="appender" --> 
        <logger name="com.**.**" level="INFO"/>  

        <root level="ERROR">  
            <appender-ref ref="Console"/>  
            <appender-ref ref="RollingFile"/>  
        </root>  
    </loggers>  
</configuration>
```

### 动态修改日志级别

```java
/**  
 * Log4j2动态更改日志级别
 * @param loggerName 日志名称  
 * @param value 日志级别  
 */
 public void changeLogLevel(String loggerName, String value) {  
    LoggerContext ctx = (LoggerContext) LogManager.getContext(false);  
    org.apache.logging.log4j.core.config.Configuration config = ctx.getConfiguration();  

    LoggerConfig loggerConfig = config.getLoggerConfig(loggerName);  
    if (Objects.nonNull(loggerConfig)) {  
        loggerConfig.setLevel(Level.getLevel(value));  
    } 
    ctx.updateLoggers(config);  
}
```

### MDC 在多线程环境下进行日志调用链路的跟踪

```xml
<PatternLayout pattern="%-d{yyyy-MM-dd HH:mm:ss SSS}-->[%t]--[%p]--[%c{1}:%L]%X{traceId} -- %m%n"/>  
```

```java
MDC.put("traceId", getTraceId());
log.info("MDC");
// 注意执行remove方法
MDC.remove();
```