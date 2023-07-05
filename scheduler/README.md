# Spring Scheduling 实现原理

## 启用

1. @org.springframework.scheduling.annotation.EnableScheduling

    ```java
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Import(SchedulingConfiguration.class)
    @Documented
    public @interface EnableScheduling {
    
    }
    ```
2. org.springframework.scheduling.annotation.SchedulingConfiguration
   ```java
    @Configuration(proxyBeanMethods = false)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public class SchedulingConfiguration {
    
        @Bean(name = TaskManagementConfigUtils.SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME)
        @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
        public ScheduledAnnotationBeanPostProcessor scheduledAnnotationProcessor() {
            // 应用中所有 Bean 含有 @Scheduled 注解后置处理器
            return new ScheduledAnnotationBeanPostProcessor();
        }
    
    }
    ```
   
## 关闭
只有一个地方启用，直接注释掉 @org.springframework.scheduling.annotation.EnableScheduling
或者在Spring boot 启动类上 ，使用注解排除
```java
@ComponentScan(excludeFilters = {@ComponentScan.Filter(value = {org.springframework.scheduling.annotation.EnableScheduling.class})})

```

## 配置解析

1. @Scheduled 任务
   org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.processScheduled(Scheduled scheduled,
   Method method, Object bean)

```java
        tasks.add(this.registrar.scheduleCronTask(new CronTask(runnable,new CronTrigger(cron,timeZone))));
        tasks.add(this.registrar.scheduleFixedDelayTask(new FixedDelayTask(runnable,fixedDelay,initialDelay)));
        tasks.add(this.registrar.scheduleFixedRateTask(new FixedRateTask(runnable,fixedRate,initialDelay)));

// Finally register the scheduled tasks
synchronized (this.scheduledTasks){
        Set<ScheduledTask> regTasks=this.scheduledTasks.computeIfAbsent(bean,key->new LinkedHashSet<>(4));
        regTasks.addAll(tasks);
        }
```

2. 实现 SchedulingConfigurer 配置类

```java

@FunctionalInterface
public interface SchedulingConfigurer {

    /**
     * Callback allowing a {@link org.springframework.scheduling.TaskScheduler
     * TaskScheduler} and specific {@link org.springframework.scheduling.config.Task Task}
     * instances to be registered against the given the {@link ScheduledTaskRegistrar}.
     * @param taskRegistrar the registrar to be configured.
     */
    void configureTasks(ScheduledTaskRegistrar taskRegistrar);

}
```

例如：

```java

@Configuration
@EnableScheduling
public class AppConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
        taskRegistrar.addTriggerTask(() -> myTask().work(), new CustomTrigger());
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(42);
    }

    @Bean
    public MyTask myTask() {
        return new MyTask();
    }
}
```

2.1 应用 SchedulingConfigurer 配置生效
在方法 org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor.finishRegistration
中，在容器中取出所有的 SchedulingConfigurer 的 Bean再遍历执行所有的 configureTasks 方法，对 ScheduledTaskRegistrar 进行配置

```java
      if(this.beanFactory instanceof ListableBeanFactory){
        Map<String, SchedulingConfigurer> beans=
        ((ListableBeanFactory)this.beanFactory).getBeansOfType(SchedulingConfigurer.class);
        List<SchedulingConfigurer> configurers=new ArrayList<>(beans.values());
        AnnotationAwareOrderComparator.sort(configurers);
        for(SchedulingConfigurer configurer:configurers){
        configurer.configureTasks(this.registrar);
        }
        }
```

2.2 设置默认 taskScheduler 调度器（taskScheduler->ScheduledExecutorService->Executors.newSingleThreadScheduledExecutor()）

```java
        if(this.registrar.hasTasks()&&this.registrar.getScheduler()==null){

            this.registrar.setTaskScheduler(resolveSchedulerBean(this.beanFactory,TaskScheduler.class,false));
            ……

            this.registrar.setTaskScheduler(resolveSchedulerBean(this.beanFactory,TaskScheduler.class,true));
            ……
            this.registrar.setScheduler(resolveSchedulerBean(this.beanFactory,ScheduledExecutorService.class,false));
            ……
            this.registrar.setScheduler(resolveSchedulerBean(this.beanFactory,ScheduledExecutorService.class,true));
            ……
            this.registrar.afterPropertiesSet();
        }
```
2.3 调度任务
将所有解析的调度任务（triggerTask、cronTask、fixedRateTasks、fixedDelayTasks）放入调度器中，
TaskScheduler(ThreadPoolTaskScheduler) => ScheduledExecutorService
每种调度任务都会被包装成 ScheduledFuture（对应调度类型有对应实现类：ReschedulingRunnable（这个由spring提供，使得任务能够按照一定的规则重复执行（cron表达式））、ScheduledFutureTask） ，放入 ScheduledExecutorService 中
```java
public void afterPropertiesSet(){
        scheduleTasks();
}

/**
 * Schedule all registered tasks against the underlying
 * {@linkplain #setTaskScheduler(TaskScheduler) task scheduler}.
 */
@SuppressWarnings("deprecation")
protected void scheduleTasks(){
        if(this.taskScheduler==null){
            this.localExecutor=Executors.newSingleThreadScheduledExecutor();
            this.taskScheduler=new ConcurrentTaskScheduler(this.localExecutor);
        }
        if(this.triggerTasks!=null){
            for(TriggerTask task:this.triggerTasks){
                addScheduledTask(scheduleTriggerTask(task));
            }
        }
        if(this.cronTasks!=null){
            for(CronTask task:this.cronTasks){
                addScheduledTask(scheduleCronTask(task));
            }
        }
        if(this.fixedRateTasks!=null){
            for(IntervalTask task:this.fixedRateTasks){
                addScheduledTask(scheduleFixedRateTask(task));
            }
        }
        if(this.fixedDelayTasks!=null){
            for(IntervalTask task:this.fixedDelayTasks){
                addScheduledTask(scheduleFixedDelayTask(task));
            }
        }
}
```

## 自动配置

org.springframework.boot.autoconfigure.task.TaskSchedulingProperties
```yaml
spring:
   task:
      scheduling:
         pool:
            size: 1
         thread-name-prefix: myScheduler-
```

org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration

```java

@ConditionalOnClass({ThreadPoolTaskScheduler.class})
@Configuration(
        proxyBeanMethods = false
)
@EnableConfigurationProperties({TaskSchedulingProperties.class})
@AutoConfigureAfter({TaskExecutionAutoConfiguration.class})
public class TaskSchedulingAutoConfiguration {
    public TaskSchedulingAutoConfiguration() {
    }

    /**
     * 若未配置任何 SchedulingConfigurer,TaskScheduler,ScheduledExecutorService 的 bean
     * 则根据 TaskSchedulingProperties 配置的 TaskSchedulerBuilder 构建定时任务调度线程池（默认为单线程:poolSize=3）
     */
    @Bean
    @ConditionalOnBean(
            name = {"org.springframework.context.annotation.internalScheduledAnnotationProcessor"}
    )
    @ConditionalOnMissingBean({SchedulingConfigurer.class, TaskScheduler.class, ScheduledExecutorService.class})
    public ThreadPoolTaskScheduler taskScheduler(TaskSchedulerBuilder builder) {
        return builder.build();
    }

    @Bean
    public static LazyInitializationExcludeFilter scheduledBeanLazyInitializationExcludeFilter() {
        return new ScheduledBeanLazyInitializationExcludeFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public TaskSchedulerBuilder taskSchedulerBuilder(TaskSchedulingProperties properties, ObjectProvider<TaskSchedulerCustomizer> taskSchedulerCustomizers) {
        TaskSchedulerBuilder builder = new TaskSchedulerBuilder();
        builder = builder.poolSize(properties.getPool().getSize());
        TaskSchedulingProperties.Shutdown shutdown = properties.getShutdown();
        builder = builder.awaitTermination(shutdown.isAwaitTermination());
        builder = builder.awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod());
        builder = builder.threadNamePrefix(properties.getThreadNamePrefix());
        builder = builder.customizers(taskSchedulerCustomizers);
        return builder;
    }
}
```