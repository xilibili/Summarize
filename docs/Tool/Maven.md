## Maven跳过单元测试

### 命令行方式

-DskipTests，不执行测试用例，但编译测试用例类生成相应的class文件至target/test-classes下。

-Dmaven.test.skip=true，不执行测试用例，也不编译测试用例类。

> 命令：mvn clean package -Dmaven.test.skip=true

### pom.xml文件配置

```xml
<build>
  <plugins>
    <!-- 跳过单元测试 -->
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <version>2.18.1</version>
      <configuration>
        <skipTests>true</skipTests>
      </configuration>
    </plugin>
  </plugins>
</build>
```

