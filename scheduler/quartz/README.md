[线程
Scheduler 主要包含两种线程：

调度线程，负责任务调度 (QuartzSchedulerThread)
工作线程池，负责执行任务 (QuartzSchedulerResources)

任务执行包装器
JobRunShell 默认使用 SpringBeanJobFactory 创建任务实例，并注入依赖


集群模式原理
集群模式和单机模式的工作原理类似，但是需要解决以下几个问题:

Job 信息的持久化
如何感知集群中的其他节点状态，以在节点发现 failover时将任务恢复
保证任务执行的一致性，不会出现同一个任务被多个节点抢到，重复执行的现象
持久化
JobStore 用于存储 Job 和 Trigger 信息

RAMJobStore 存在内存中，无法持久化，且难以在集群间进行通信
JobStoreSupport， 通过 DriverDelegate(StdJDBCDelegate) 持久化
数据库表
QRTZ_CALENDARS	Quartz的Calendar信息
QRTZ_CRON_TRIGGERS	CronTrigger，包括Cron表达式和时区信息
QRTZ_FIRED_TRIGGERS	与已触发的Trigger相关的状态信息，以及相联Job的执行信息
QRTZ_PAUSED_TRIGGER_GRPS	已暂停的Trigger组的信息
QRTZ_SCHEDULER_STATE	存储Scheduler的状态信息，和别的Scheduler实例
QRTZ_LOCKS	锁信息
QRTZ_JOB_DETAILS	存储每一个已配置的Job的详细信息
QRTZ_SIMPLE_TRIGGERS	SimpleTrigger
QRTZ_BLOB_TRIGGERS	Trigger作为Blob类型存储
QRTZ_SIMPROP_TRIGGERS	Simprop Trigger
QRTZ_TRIGGERS	已配置的Trigger的信息
集群调度
相关类
JobStoreSupport 数据库存储任务信息实现
StdRowLockSemaphore 数据库行锁实现

QRTZ_LOCKS
+------------+--------------+------+-----+---------+-------+
| Field      | Type         | Null | Key | Default | Extra |
+------------+--------------+------+-----+---------+-------+
| SCHED_NAME | varchar(120) | NO   | PRI | NULL    |       |
| LOCK_NAME  | varchar(40)  | NO   | PRI | NULL    |       |
+------------+--------------+------+-----+---------+-------+


```java
public static final String SELECT_FOR_LOCK = "SELECT * FROM "
        + TABLE_PREFIX_SUBST + TABLE_LOCKS + " WHERE " + COL_SCHEDULER_NAME + " = " + SCHED_NAME_SUBST
        + " AND " + COL_LOCK_NAME + " = ? FOR UPDATE";

public static final String INSERT_LOCK = "INSERT INTO "
    + TABLE_PREFIX_SUBST + TABLE_LOCKS + "(" + COL_SCHEDULER_NAME + ", " + COL_LOCK_NAME + ") VALUES (" 
    + SCHED_NAME_SUBST + ", ?)"; 
```


Job 类型
有状态 Job 不可同时执行（必须为集群模式），用PersistJobDataAfterExecution 和 DisallowConcurrentExecution 注解
无状态 Job 可以同时执行

业务逻辑上的 Job 与 JobDetail 一一对应，JobDetail 由 JobKey（name+group） 标识

[https://bbs.huaweicloud.com/blogs/329247](https://bbs.huaweicloud.com/blogs/329247)


### 集群配置
同一个集群，SchedulerName 和 ScheduleInstanceName 相同，集群中的不同节点 ScheduleInstanceId 不同
```yaml
  org:
    quartz:
      scheduler:
        instanceId: AUTO # 实例 ID，根据机器名字和时间戳生成 SimpleInstanceIdGenerator
        instanceName: clusteredScheduler # 实例名字
```
