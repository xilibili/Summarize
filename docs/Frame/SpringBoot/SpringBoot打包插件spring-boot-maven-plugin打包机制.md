### 引入spring-boot-maven-plugin

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
      <configuration>
        <!-- 应用启动类 -->
        <mainClass>com.**.**.*Application</mainClass>
        <layout>ZIP</layout>
      </configuration>
    </plugin>
  </plugins>
</build>
```

spring-boot-maven-plugin插件在父工程spring-boot-starter-parent中被指定为repackage

```xml
<!-- 父POM spring-boot-starter-parent 定义 -->
<plugin>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-maven-plugin</artifactId>
  <executions>
    <execution>
      <id>repackage</id>
      <goals>
        <goal>repackage</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <mainClass>${start-class}</mainClass>
  </configuration>
</plugin>
```

### 执行打包命令

```
mvn clean package
```

1. 执行以上命令后会自动触发spring-boot-maven-plugin插件的repackage目标，在target目录下回生成xxx.jar和xxx.jar.original俩个文件。
2. xxx.jar是spring-boot-maven-plugin插件重新打包后生成的可执行jar，可以通过命令java -jar xxx.jar命令启动，xxx.jar.original则是mvn package打包的原始jar

### Jar内部结构

#### xxx.jar

1. BOOT-INF：一些启动信息，包含classes和lib文件，classes是项目里生成的字节文件class和配置文件，lib是项目所需要的jar依赖

2. META-INF：主要是maven的一些元数据信息

   ```
   Manifest-Version: 1.0
   Spring-Boot-Classpath-Index: BOOT-INF/classpath.idx
   Implementation-Title: xxx
   Implementation-Version: 1.0.0-SNAPSHOT
   Start-Class: com.**.**.*Application
   Spring-Boot-Classes: BOOT-INF/classes/
   Spring-Boot-Lib: BOOT-INF/lib/
   Build-Jdk-Spec: 1.8
   Spring-Boot-Version: 2.3.10.RELEASE
   Created-By: Maven Jar Plugin 3.2.0
   Main-Class: org.springframework.boot.loader.PropertiesLauncher
   ```

   1. Start-Class：项目的主程序入口，即main方法。
   
   2. Spring-Boot-Classes：指向BOOT-INF对应的位置。
   
   3. Spring-Boot-Lib：指向BOOT-INF对应的位置。
   
   4. Main-Class：可以通过配置`<layout>ZIP</layout>`指定SpringBoot的类加载器。
   
   ![这里写图片描述](https://img-blog.csdn.net/20170912194235554?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvZ2d6NjMxMDQ3MzY3/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
   
     - ZIP:`org.springframework.boot.loader.PropertiesLauncher`，指定jar包外配置文件需要使用`ZIP`方式。[官方文档](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/loader/PropertiesLauncher.html)
   
     - JAR:`org.springframework.boot.loader.JarLauncher`[官方文档](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/loader/JarLauncher.html)
   
     - WAR:`org.springframework.boot.loader.WarLauncher`[官方文档](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/loader/WarLauncher.html)
   
       
