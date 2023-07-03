# 简易 Mybatis 框架多数据源配置
基于 `AbstractRoutingDataSource` 实现动态数据源
mybatis 通过 `SqlSessionFactoryBean` 设置自定义动态数据源
执行数据库读写方法时，通过AOP结合注解作为切点指定数据源

缺点：
* 简易粗糙，更多高级特性需要自己实现（动态修改数据源、跨数据源的事务控制）
* 对业务编码有侵入