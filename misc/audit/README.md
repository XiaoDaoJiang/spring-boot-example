## 新增
### PostInsertEventListener

```java
public class PostInsertEvent extends AbstractEvent {
    private Object entity;
    private EntityPersister persister;
    private Object[] state;
    private Serializable id;
    
}
```

## 修改
### PostUpdateEventListener

```java
public class PostUpdateEvent extends AbstractEvent {
    private Object entity;
    private EntityPersister persister;
    private Object[] state;
    private Object[] oldState;
    private Serializable id;
    //list of dirty properties as computed by Hibernate during a FlushEntityEvent
    private final int[] dirtyProperties;
}
```
persister.getPropertyNames() 与 dirtyProperties 的交集，即为更新的属性


## 使用