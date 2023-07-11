# Spring Boot Actuator

参考：[https://docs.spring.io/spring-boot/docs/2.7.13/reference/html/actuator.html](https://docs.spring.io/spring-boot/docs/2.7.13/reference/html/actuator.html)

### Endpoints

内置endpoints

http://ip:management-server-port/actuator/[endpoint-id]

| ID | Description |
| --- | --- |
| auditevents | Exposes audit events information for the current application. Requires an AuditEventRepository bean. |
| beans | Displays a complete list of all the Spring beans in your application. |
| caches | Exposes available caches. |
| conditions | Shows the conditions that were evaluated on configuration and auto-configuration classes and the reasons why they did or did not match. |
| configprops | Displays a collated list of all @ConfigurationProperties. |
| env | Exposes properties from Spring’s ConfigurableEnvironment. |
| flyway | Shows any Flyway database migrations that have been applied. Requires one or more Flyway beans. |
| health | Shows application health information. |
| httptrace | Displays HTTP trace information (by default, the last 100 HTTP request-response exchanges). Requires an HttpTraceRepository bean. |
| info | Displays arbitrary application info. |
| integrationgraph | Shows the Spring Integration graph. Requires a dependency on spring-integration-core. |
| loggers | Shows and modifies the configuration of loggers in the application. |
| liquibase | Shows any Liquibase database migrations that have been applied. Requires one or more Liquibase beans. |
| metrics | Shows “metrics” information for the current application. |
| mappings | Displays a collated list of all @RequestMapping paths. |
| quartz | Shows information about Quartz Scheduler jobs. |
| scheduledtasks | Displays the scheduled tasks in your application. |
| sessions | Allows retrieval and deletion of user sessions from a Spring Session-backed session store. Requires a servlet-based web application that uses Spring Session. |
| shutdown | Lets the application be gracefully shutdown. Only works when using jar packaging. Disabled by default. |
| startup | Shows the https://docs.spring.io/spring-boot/docs/2.7.13/reference/html/features.html#features.spring-application.startup-tracking collected by the ApplicationStartup. Requires the SpringApplication to be configured with a BufferingApplicationStartup. |
| threaddump | Performs a thread dump. |

web application

| ID | Description |
| --- | --- |
| heapdump | Returns a heap dump file. On a HotSpot JVM, an HPROF-format file is returned. On an OpenJ9 JVM, a PHD-format file is returned. |
| jolokia | Exposes JMX beans over HTTP when Jolokia is on the classpath (not available for WebFlux). Requires a dependency on jolokia-core. |
| logfile | Returns the contents of the logfile (if the logging.file.name or the logging.file.path property has been set). Supports the use of the HTTP Range header to retrieve part of the log file’s content. |
| prometheus | Exposes metrics in a format that can be scraped by a Prometheus server. Requires a dependency on micrometer-registry-prometheus. |

endpoints 设置

- Enabling Endpoints

  默认情况下，除了`shutdown`以外的断点都是启用的，可以通过以下配置更改某个端点id的是否启用

    - management.endpoint.<id>.enabled

        ```yaml
        management:
          endpoint:
            shutdown:
              enabled: true
        ```


    要更改所有端点默认为禁用，则可以使用：
    
    ```yaml
    management:
      endpoints:
        enabled-by-default: false
      endpoint:
        info:
          enabled: true
    ```
    
    > 禁用的端点将从应用程序上下文中完全删除。如果您只想更改端点公开的技术，请改用包含和排除属性。Disabled endpoints are removed entirely from the application context. If you want to change only the technologies over which an endpoint is exposed, use the`[include` and `exclude` properties](https://docs.spring.io/spring-boot/docs/2.7.13/reference/html/actuator.html#actuator.endpoints.exposing)instead.
    > 
- Exposing Endpoints

  由于端点可能包含敏感信息，因此您应该仔细考虑何时公开它们。下表显示了内置端点的默认暴露：

  | ID | JMX | Web |
      | --- | --- | --- |
  | auditevents | Yes | No |
  | beans | Yes | No |
  | caches | Yes | No |
  | conditions | Yes | No |
  | configprops | Yes | No |
  | env | Yes | No |
  | flyway | Yes | No |
  | health | Yes | Yes |
  | heapdump | N/A | No |
  | httptrace | Yes | No |
  | info | Yes | No |
  | integrationgraph | Yes | No |
  | jolokia | N/A | No |
  | logfile | N/A | No |
  | loggers | Yes | No |
  | liquibase | Yes | No |
  | metrics | Yes | No |
  | mappings | Yes | No |
  | prometheus | N/A | No |
  | quartz | Yes | No |
  | scheduledtasks | Yes | No |
  | sessions | Yes | No |
  | shutdown | Yes | No |
  | startup | Yes | No |
  | threaddump | Yes | No |
  |  |  |  |

  要更改公开的端点，请使用以下特定于技术的`include` 和`exclude` 属性：

  | Property | Default |
      | --- | --- |
  | management.endpoints.jmx.exposure.exclude |  |
  | management.endpoints.jmx.exposure.include | * |
  | management.endpoints.web.exposure.exclude |  |
  | management.endpoints.web.exposure.include | health |

  exclude 优先与include，两者都可以配置为 endpoint IDs

    ```yaml
    management:
      endpoints:
        web:
          exposure:
            include: "*"
            exclude: "env,beans"
    ```


自定义endpoints

参考 org.springframework.boot.actuate.cache.CachesEndpoint

Bean类：

1. `@Endpoint`
2. `@WebEndpoint、@JmxEndpoint`
3. 基于特定技术实现增强现有端点（与@Endpoints一起使用） `@EndpointWebExtension` and `@EndpointJmxExtension`
4. `@ServletEndpoint`
5. `@ControllerEndpoint` and `@RestControllerEndpoint`

方法：

1. `@ReadOperation`, `@WriteOperation`,`@DeleteOperation`
2. `@RequestMapping` and `@GetMapping`

### HealthIndicator

management.endpoint.health.show-details

management.endpoint.health.show-components

获取应用程序包含整个应用与各个组件（components）的健康状态信息

HealthIndicator 可以是单个 HealthIndicator 也可以是 CompositeHealthContributor 组合多个健康贡献者；最终整个系统的health 去解决于 StatusAggregator，将每个 HealthIndicator 的状态排序，将列表中第一个作为健康状态

```yaml
management:
  endpoint:
    health:
      status:
        order: "fatal,down,out-of-service,unknown,up"
```

在适当的情况下，Spring Boot 会自动配置下表中列出的 HealthIndicators。您还可以通过配置management.health.key.enabled来启用或禁用选定的指标，其密钥如下表所示：

| Key | Name | Description |
| --- | --- | --- |
| cassandra | https://github.com/spring-projects/spring-boot/tree/v2.7.13/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/cassandra/CassandraDriverHealthIndicator.java | Checks that a Cassandra database is up. |
| couchbase | https://github.com/spring-projects/spring-boot/tree/v2.7.13/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/couchbase/CouchbaseHealthIndicator.java | Checks that a Couchbase cluster is up. |
| db | https://github.com/spring-projects/spring-boot/tree/v2.7.13/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/jdbc/DataSourceHealthIndicator.java | Checks that a connection to DataSource can be obtained. |
| diskspace | https://github.com/spring-projects/spring-boot/tree/v2.7.13/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/system/DiskSpaceHealthIndicator.java | Checks for low disk space. |
| elasticsearch | https://github.com/spring-projects/spring-boot/tree/v2.7.13/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/elasticsearch/ElasticsearchRestHealthIndicator.java | Checks that an Elasticsearch cluster is up. |
| hazelcast | https://github.com/spring-projects/spring-boot/tree/v2.7.13/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/hazelcast/HazelcastHealthIndicator.java | Checks that a Hazelcast server is up. |
| influxdb | https://github.com/spring-projects/spring-boot/tree/v2.7.13/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/influx/InfluxDbHealthIndicator.java | Checks that an InfluxDB server is up. |
| jms | https://github.com/spring-projects/spring-boot/tree/v2.7.13/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/jms/JmsHealthIndicator.java | Checks that a JMS broker is up. |
| ldap | https://github.com/spring-projects/spring-boot/tree/v2.7.13/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/ldap/LdapHealthIndicator.java | Checks that an LDAP server is up. |
| mail | https://github.com/spring-projects/spring-boot/tree/v2.7.13/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/mail/MailHealthIndicator.java | Checks that a mail server is up. |
| mongo | https://github.com/spring-projects/spring-boot/tree/v2.7.13/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/mongo/MongoHealthIndicator.java | Checks that a Mongo database is up. |
| neo4j | https://github.com/spring-projects/spring-boot/tree/v2.7.13/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/neo4j/Neo4jHealthIndicator.java | Checks that a Neo4j database is up. |
| ping | https://github.com/spring-projects/spring-boot/tree/v2.7.13/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/health/PingHealthIndicator.java | Always responds with UP. |
| rabbit | https://github.com/spring-projects/spring-boot/tree/v2.7.13/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/amqp/RabbitHealthIndicator.java | Checks that a Rabbit server is up. |
| redis | https://github.com/spring-projects/spring-boot/tree/v2.7.13/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/redis/RedisHealthIndicator.java | Checks that a Redis server is up. |
| solr | https://github.com/spring-projects/spring-boot/tree/v2.7.13/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/solr/SolrHealthIndicator.java | Checks that a Solr server is up. |

可以通过 management.health.defaults.enabled = true 来默认禁用

自定义健康指标

```java
@Component
public class MyHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        int errorCode = check();
        if (errorCode != 0) {
            return Health.down().withDetail("Error Code", errorCode).build();
        }
        return Health.up().build();
    }

    private int check() {
        // perform some specific health check
        return ...
    }

}
```

**Health Groups**

[http://localhost:8080/actuator/health/custom](http://localhost:8080/actuator/health/custom)

```yaml
management:
  endpoint:
    health:
      group:
        custom:
          include: "db"
```

### InfoContributor

*/actuator/info*

应用程序信息公开了从 ApplicationContext 中定义的所有 InfoContributor bean 收集的各种信息。 Spring Boot 包含许多自动配置的 InfoContributor bean，您可以编写自己的 bean。

Loggers
Spring Boot Actuator 能够在运行时查看和配置应用程序的日志级别。您可以查看整个列表或单个记录器的配置，该配置由显式配置的日志记录级别以及日志记录框架赋予的有效日志记录级别组成。这些级别可以是以下级别之一：

- `TRACE`
- `DEBUG`
- `INFO`
- `WARN`
- `ERROR`
- `FATAL`
- `OFF`
- `null`

/actuator/loggers

POST /actuator/loggers/com.xiaodao.ActuatorApplication

```bash
curl --location --request POST 'http://localhost:8091/actuator/loggers/com.xiaodao.ActuatorApplication' \
--header 'Content-Type: application/json' \
--data-raw '{
    "configuredLevel": "DEBUG"
}'
```

## HttpTracing

需要应用上下文中有 HttpTraceRepository 的 Bean 来启用 `/acturator/httptrace`

```java
@Configuration
@ConditionalOnProperty(prefix = "management.trace.http",value = "enabled", havingValue = "true")
public class MyHttpTracing {

    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }

}
```

您可以通过在应用程序的配置中提供 HttpTraceRepository 类型的 bean 来启用 HTTP 跟踪。为了方便起见，Spring Boot 提供了 InMemoryHttpTraceRepository，它存储最后 100 个（默认）请求响应交换的跟踪信息。与其他跟踪解决方案相比，InMemoryHttpTraceRepository 具有局限性，我们建议仅将其用于开发环境。对于生产环境，我们建议使用生产就绪的跟踪或可观察性解决方案，例如 Zipkin 或 Spring Cloud Sleuth。或者，您可以创建自己的 HttpTraceRepository。

要自定义每个跟踪中包含的项目，请使用 management.trace.http.include 配置属性。对于高级自定义，请考虑注册您自己的 HttpExchangeTracer 实现。

## Process Monitoring

在 spring-boot 模块中，您可以找到两个类来创建通常对进程监控有用的文件：
ApplicationPidFileWriter 创建一个包含应用程序 PID 的文件（默认情况下，位于应用程序目录中，文件名为 application.pid）。
WebServerPortFileWriter 创建一个或多个文件，其中包含正在运行的 Web 服务器的端口（默认情况下，位于应用程序目录中，文件名为 application.port）。
默认情况下，这些编写器未激活，但您可以启用它们：

- 通过扩展配置

  在 META-INF/spring.factories 文件中，您可以激活写入 PID 文件的侦听器（或多个侦听器）：
  org.springframework.context.ApplicationListener=\
  org.springframework.boot.context.ApplicationPidFileWriter,\
  org.springframework.boot.web.context.WebServerPortFileWriter

- 以编程方式启用进程监控

  您还可以通过调用 SpringApplication.addListeners(…) 方法并传递适当的 Writer 对象来激活侦听器。此方法还允许您在 Writer 构造函数中自定义文件名和路径。