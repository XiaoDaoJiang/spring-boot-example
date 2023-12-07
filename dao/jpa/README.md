## Interceptors
>通过文件配置的监听器或拦截器类都是通过反射实例化的无状态对象，不够灵活，不是Spring 容器中的bean;

>hibernate 拦截器是固定sessionFactory上，或者对于特定session 指定，硬编码，
```java
SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
Session session = sessionFactory
	.withOptions()
	.interceptor(new LoggingInterceptor())
	.openSession();
session.getTransaction().begin();

Customer customer = session.get(Customer.class, customerId);
customer.setName("Mr. John Doe");
//Entity Customer#1 changed from [John Doe, 0] to [Mr. John Doe, 0]

session.getTransaction().commit();

```
----
```java
SessionFactory sessionFactory = new MetadataSources(new StandardServiceRegistryBuilder().build())
	.addAnnotatedClass(Customer.class)
	.getMetadataBuilder()
	.build()
	.getSessionFactoryBuilder()
	.applyInterceptor(new LoggingInterceptor())
	.build();
```


## event listener
hibernate 内置很多默认的事件类型及对应的监听器，每种事件有对应的监听器组
```java
public final class EventType<T> {
    
}
class EventListenerGroupImpl<T> implements EventListenerGroup<T> {
    
}
```

通过 EventEngine(老版本：EventListenerRegistry)  初始化时将构建entity 关联的 callback 以及 注册对应事件类型及监听器组并初始化默认的监听器

```java


public class EventListenerRegistryImpl implements EventListenerRegistry, Stoppable {
    
    
    private EventListenerGroupImpl[] buildListenerGroups() {
        EventListenerGroupImpl[] listenerArray = new EventListenerGroupImpl[EventType.values().size()];

        // auto-flush listeners
        prepareListeners(
                AUTO_FLUSH,
                new DefaultAutoFlushEventListener(),
                listenerArray
        );

        // create listeners
        prepareListeners(
                PERSIST,
                new DefaultPersistEventListener(),
                listenerArray
        );

        // create-onflush listeners
        prepareListeners(
                PERSIST_ONFLUSH,
                new DefaultPersistOnFlushEventListener(),
                listenerArray
        );

        // delete listeners
        prepareListeners(
                DELETE,
                new DefaultDeleteEventListener(),
                listenerArray
        );
        // …………
    }
}
```

org.hibernate.internal.FastSessionServices 内部维护很多会话共享的缓存以便于快速访问，
包括了 EventListenerRegistryImpl 中构建的各种事件类型的 EventListenerGroup

```java

```

通过自定义实现各种实体类相关生命周期的监听器，并通过 EventListenerRegistryImpl 添加到对应的事件类型组下
```java
EntityManagerFactory entityManagerFactory = entityManagerFactory();
SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap( SessionFactoryImplementor.class );
sessionFactory
	.getServiceRegistry()
	.getService( EventListenerRegistry.class )
	.prependListeners( EventType.LOAD, new SecuredLoadEntityListener() );

Customer customer = entityManager.find( Customer.class, customerId );
```

## 拦截器
通过事件监听器 触发 会话的拦截器

```java
public class DefaultPreLoadEventListener implements PreLoadEventListener {
	
	public void onPreLoad(PreLoadEvent event) {
		EntityPersister persister = event.getPersister();
		event.getSession()
			.getInterceptor()
			.onLoad( 
					event.getEntity(), 
					event.getId(), 
					event.getState(), 
					persister.getPropertyNames(), 
					persister.getPropertyTypes() 
				);
	}
	
}
```

注册拦截器
代码
* 为特定 session 设置拦截器
* 为 sessionFactory 设置拦截器
配置
* org.hibernate.cfg.AvailableSettings.INTERCEPTOR
* org.hibernate.cfg.AvailableSettings.SESSION_SCOPED_INTERCEPTOR
```properties
spring.jpa.properties.hibernate.session_factory.interceptor=xx.MyInterceptor
spring.jpa.properties.hibernate.ejb.event.load=xx.MyLoadEventListener
```

## Jpa Callbacks (mark entity create or modify and auditor)


> 原理依赖 jpa 通过定义生命周期事件注解（@PrePersist、@PreUpdate）来指定回调方法（被事件注释的方法）
> hibernate 定义各类具体事件（PostLoadEvent，PostInsertEvent），通过事件监听器（PostLoadEventListener、PostInsertEventListener），中的 CallbackRegistryImpl invoke 回调方法

@EnableJpaAuditing
-> register jpaAuditingHandler:
```java
public class AuditingHandler extends AuditingHandlerSupport implements InitializingBean {
    
}
public class IsNewAwareAuditingHandler extends AuditingHandler{
    
}
```
在 实体类中带事件注解方法或者指定事件监听器定义带注解方法
> 为什么这种回调能够拿到“侦听器”中注入的变量signatureService
> EventEngine 初始化jpaCallback 时会传入 ManagedBeanRegistry，相当于从Spring 容器中获取到了bean，目标回调方法生成 ListenerCallback，自然能够访问
```java
/**
 * 一个监听器类带有注解方法，这个方法就是会被抽象为一个回调callbackMethod 
 */
public class MySignEntityListener {

    @Autowired
    private SignatureService signatureService;

    @PrePersist
    @PreUpdate
    public void sign(BaseEntity entity) {
        log.info("execute sign ");
        if (entity != null) {
            // log.info("set sign hmac");
            entity.setSignature(signatureService.sign(entity));
        }
        log.info("execute sign done ");

    }

    @PostLoad
    public void verify(BaseEntity entity) {
        if (entity != null) {
            // log.info("execute verify ");
            log.info("execute checkSing ");
            log.info("check sign result: {}", entity.getSignature().equals(SignUtils.doSign(SignUtils.getSignProperties(this))));

        }
    }
}
```

-> @entity or @MappedSuperclass exist @EntityListeners(AuditingEntityListener.class)
```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditEntity {

    @CreatedBy
    public Long createBy;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedBy
    private Long modifyBy;

    @LastModifiedDate
    private LocalDateTime modifyTime;

}

public class AuditingEntityListener {

    private @Nullable ObjectFactory<AuditingHandler> handler;

    /**
     * Configures the {@link AuditingHandler} to be used to set the current auditor on the domain types touched.
     *
     * @param auditingHandler must not be {@literal null}.
     */
    public void setAuditingHandler(ObjectFactory<AuditingHandler> auditingHandler) {

        Assert.notNull(auditingHandler, "AuditingHandler must not be null!");
        this.handler = auditingHandler;
    }

    /**
     * Sets modification and creation date and auditor on the target object in case it implements {@link Auditable} on
     * persist events.
     *
     * @param target
     */
    @PrePersist
    public void touchForCreate(Object target) {

        Assert.notNull(target, "Entity must not be null!");

        if (handler != null) {

            AuditingHandler object = handler.getObject();
            if (object != null) {
                object.markCreated(target);
            }
        }
    }

    /**
     * Sets modification and creation date and auditor on the target object in case it implements {@link Auditable} on
     * update events.
     *
     * @param target
     */
    @PreUpdate
    public void touchForUpdate(Object target) {

        Assert.notNull(target, "Entity must not be null!");

        if (handler != null) {

            AuditingHandler object = handler.getObject();
            if (object != null) {
                object.markModified(target);
            }
        }
    }
}
```
when listener callback AuditingEntityListener effect by AuditHandler

#### more jpa 
```java
@Entity(name = "Publisher")
@ExcludeDefaultListeners
@ExcludeSuperclassListeners
public static class Publisher extends BaseEntity {

	@Id
	private Long id;

	private String name;

	//Getters and setters omitted for brevity
}
```