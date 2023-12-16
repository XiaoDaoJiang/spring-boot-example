# spring-boot-example
# async
# aop
# starter
* crypto-spring-boot-starter
* prevent-repeat-submit-spring-boot-starter
* uid-generator-spring-boot-starter
# container
# cache
* caffeine
* redis
* multiLevelCache（Composite，多级缓存）
# cloud
* feign（token失效重试）
* ribbon
* hystrix
# dao
* jpa
  * EntityManager(注入代理与固定实例的区别，多次不同entity manager查询结果差异)
* hibernate orm
# db-connection-pool
# etl
* kettle(pdi,logger event listener)
# metrics
* actuator(HealthIndicator)
# mq
* rabbitmq
* kafka(kafka stream)
  * 流计算
  * 窗口
# multi-datasource
* dynamic-datasource（多数据源管理，元数据抽象）
# office
* excel(导入异步处理，模板抽象（解析，翻译，格式校验），SPI解耦业务规则)
# scheduler
* timer task
* ScheduledExecutorService
* Spring Schedule
* Quartz(cluster，dynamic job management,job listener)
  * dynamic job management
  * job listener
  * cluster
* xxl-job
# search engine
* elasticsearch(索引生命周期管理，索引模板，已新增)
# sharding
* sharding-jdbc
# transaction
# misc
* audit(业务审计日志)
  * org.hibernate.Interceptor 
  * org.hibernate.event PostInsertEventListener/PostUpdateEventListener
* dictionary translate（字典翻译）
* masking（脱敏）
* tesseract-ocr(ocr识别及优化，提高精度)
  * tess4j
  * Linux install dependencies
  * performance
  * compare other ocr(PaddleOCR) [https://blog.csdn.net/weixin_41021342/article/details/127203654]
  
* prevent-repeat-submit（防重复提交）
  * form token 校验
  * redisson lock 实现
* UidGenerator(分布式唯一 ID 生成器)
* arthas
* mail
* p6spy
* lock
# validation
* hibernate validator
# web
* request/response log（请求响应日志trace）
# test
  * test aspect
    * controller layer (mvc)
    * repository layer (jpa)
  * benchmark
    * JMH