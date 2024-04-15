## 2.7 之前

META-INF/spring.factories

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.xiaodao.spring.boot.autoconfigure.ExampleAutoConfiguration
```

## 2.7 之后

META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports

```properties
com.xiaodao.spring.boot.autoconfigure.ExampleAutoConfiguration
```

## 差别 spring.factories vs AutoConfiguration.imports

- spring.factories
    - 作用更广，不仅包含自动装配，还能注册某些spring的监听器
    ```properties
      org.springframework.context.ApplicationListener=\
      org.springframework.boot.context.ApplicationPidFileWriter,\
      org.springframework.boot.web.context.WebServerPortFileWriter
    ```

- AutoConfiguration.imports
    - 只用来注册自动装配类 