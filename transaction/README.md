## 本地事务
### 简单使用事务
* 声明式事务
```java
@Transactional
public void insert() {
    // ...
}

```

* 编程式事务
```java
@Autowired
private TransactionTemplate transactionTemplate;
public void testTransaction() {

    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
        @Override
        private void doInTransactionWithoutResult(TransactionStatus transactionStatus) {

            try {

                // ....  业务代码
            } catch (Exception e){
                //回滚
                transactionStatus.setRollbackOnly();
            }

        }
    });
}

@Autowired
private PlatformTransactionManager transactionManager;

public void testTransaction() {

    TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
    try {
        // ....  业务代码
        transactionManager.commit(status);
    } catch (Exception e) {
        transactionManager.rollback(status);
    }
}

```
## 原理
使用 AOP 来生成代理对象，代理对象来管理事务
TransactionInterceptor => TransactionAspectSupport => TransactionManager(PlatformTransactionManager/ReactiveTransactionManager )

### 事务管理器
-- spring-tx
PlatformTransactionManager,
-- spring-orm
JpaTransactionManager,
-- spring-jdbc
DataSourceTransactionManager（mybatis使用）

程序启动，被Spring管理的Bean有被 @Transactional 注解的类都会代理对象，拦截对应方法，先创建事务，再执行业务逻辑，最后判断提交还是回滚。
同时程序启动时，会根据引入orm框架包和数据源配置文件自动生成对应的事务管理器并设置对应的数据源（当然也可以通过代码控制生成多个事务管理器并设置数据源（多数据源那就是再包装一层而已））

>AbstractPlatformTransactionManager
    ->DataSourceTransactionManager 提供对 Spring JDBC 抽象框架与 mybatis 事务支持
    ->JpaTransactionManager 提供集成JPA框架事务支持
    ->JtaTransactionManager 提供对分布式事务的支持，部分特定应用服务器有自己的事务支持，如：WebLogicJtaTransactionManager、WebSphereUowTransactionManager
    ->HibernateTransactionManager 提供集成 Hibernate框架事务的支持

调用事务方法，会先创建事务，而用哪个事务管理器创建事务也是可以指定的（），再获取数据源获取数据库连接connection，并绑定到当前线程中，

>使用 AbstractRoutingDataSource 的 determineCurrentLookupKey() 方法获取当前线程绑定的数据源
所以应该再开启事务前，指定数据源，否则会使用默认的数据源，而不是我们想要的数据源

事务管理器会在创建事务时，统一使用 TransactionSynchronizationManager.bindResource() 方法绑定到当前线程资源（如 session，connection）
所以默认情况下，一旦开启事务，当前线程的数据库连接就已经确定了，就算手动切换数据源，但只要处于同一个事务中实际操作数据库也只会从线程绑定的连接去用

虽然能够通过 在切换数据源后并开启新内部事务（Propagation.REQUIRES_NEW）实现对目标数据源的访问，
但是这样即便外部事务回滚，已提交的内部事务也不会回滚，所以不是很好的解决方案

### 监听事务
TransactionSynchronizationManager.registerSynchronization() 方法注册事务同步器，事务提交或回滚时，会调用对应的方法

```java
/**
 * {{@link org.springframework.transaction.support.TransactionSynchronizationManager}}
 */
public interface TransactionSynchronization {

    void suspend();

    void resume();

    void flush();

    void beforeCommit(boolean readOnly);

    void beforeCompletion();

    void afterCommit();

    void afterCompletion(int status);
}
```
例如 Hibernate/JPA 的 Session/EntityManager 就是在事务提交前，调用 flush() 方法，将缓存的数据同步到数据库中，
所以处理大量数据时，按批次进行 flush 会有一定的性能提升（但要注意hibernate维护大量待flush的更改持有在内存中）


## 分布式事务
### JTA/XA
### 单体应用的多数据源事务
1. atomikos(Transactions Essentials)
### 微服务/SOA中的分布式事务
