## 消息可靠性

* 事务消息（性能差）
* 生产者确认机制
  每个消息分配全局唯一id
    1. publisher confirm（发送者确认）
       确保消息发送到交换机，成功失败都会
       ○ 消息成功投递到交换机，返回ack
       ○ 消息未投递到交换机，返回nack
    2. publisher return（发送者回执）
       确保消息投递到队列中，只有当消息无法路由到队列中返回

配置文件

```yaml
spring:
  rabbitmq:
    publisher-confirm-type: correlated
    publisher-returns: true
    template:
      mandatory: true
```

```java
// 设置 RabbitTemplate(全局)
@Override
public void onApplicationEvent(ApplicationReadyEvent event) {
    if (rabbitTemplate != null) {
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
            if (ack) {
                log.info("消息发送至交换机成功：{}", correlationData.getId());
            } else {
                log.error("消息发送至交换机失败, ID:{}, 原因{}", correlationData.getId(), cause);
            }

        });

        rabbitTemplate.setReturnsCallback((message) -> {
            // 投递失败，记录日志
            log.error("消息路由队列失败：{}", message);
            // TODO 如果有业务需要，可以重发消息
        });
    }
}

// confirmCallback 可以在每次发送消息时指定，实现发送不同消息时，处理逻辑不一样的效果
@Test
public void testSendMessage2SimpleQueue() throws InterruptedException {
    // 1.消息体
    String message = "hello, spring amqp!";
    // 2.全局唯一的消息ID，需要封装到CorrelationData中
    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
    // 3.添加callback
    correlationData.getFuture().addCallback(
            result -> {
                if (result.isAck()) {
                    // 3.1.ack，消息成功
                    log.debug("消息发送成功, ID:{}", correlationData.getId());
                } else {
                    // 3.2.nack，消息失败
                    log.error("消息发送失败, ID:{}, 原因{}", correlationData.getId(), result.getReason());
                }
            },
            ex -> log.error("消息发送异常, ID:{}, 原因{}", correlationData.getId(), ex.getMessage())
    );
    // 4.发送消息
    rabbitTemplate.convertAndSend("task.direct", "task", message, correlationData);

    // 休眠一会儿，等待ack回执
    Thread.sleep(2000);
}
```

* mq持久化
    * 交换机持久化
  ```java
    @Bean
    public DirectExchange simpleExchange(){
    // 三个参数：交换机名称、是否持久化、当没有queue与其绑定时是否自动删除
    return new DirectExchange("simple.direct", true, false);
    }
  ```
    * 队列持久化
      ```java
      @Bean
      public Queue simpleQueue(){
      // 使用QueueBuilder构建队列，durable就是持久化的
      return QueueBuilder.durable("simple.queue").build();
      }
      ```
    * 消息持久化
      利用SpringAMQP发送消息时，可以设置消息的属性（MessageProperties），指定delivery-mode：org.springframework.amqp.core.MessageDeliveryMode
      ● 1：非持久化(NON_PERSISTENT)
      ● 2：持久化(PERSISTENT)
      默认情况下，SpringAMQP发出的任何消息都是持久化的，不用特意指定。
* 消费者确认机制
  RabbitMQ 的消息是采用阅后即焚的机制，一旦消息成功被消费者处理，就会删除消息（也就表明一个队列中的消息只会被一个消费者成功处理一次）；而RabbitMQ是通过消费者回执来
  确认消费者是否成功处理消息的：消费者获取消息后，应该向RabbitMQ发送ACK回执，表明自己已经处理消息。
  设想这样的场景：
  ● 1）RabbitMQ投递消息给消费者
  ● 2）消费者获取消息后，返回ACK给RabbitMQ
  ● 3）RabbitMQ删除消息
  ● 4）消费者宕机，消息尚未处理
  这样，消息就丢失了。因此消费者返回ACK的时机非常重要。而SpringAMQP则允许配置三种确认模式：
  •manual：手动ack，需要在业务代码结束后，调用api发送ack。
  •auto（类似事务）：自动ack，由spring监测listener代码是否出现异常，没有异常则返回ack；抛出异常则返回nack
  •none：关闭ack，MQ假定消费者获取消息后会成功处理，因此消息投递后立即被删除
  配置
  ```yaml
  spring:
    rabbitmq:
      listener:
        simple:
          acknowledge-mode: auto # manual,auto,none 
  ```
* 失败重试机制
    * 生产者失败重试
      ```yaml
      spring:
        rabbitmq:
          template:
            # 生产者本地重试
            retry:
              enabled: true # 开启失败重试
              initial-interval: "2s" # 初识的失败等待时长为2秒
              multiplier: 1 # 失败的等待时长倍数，下次等待时长 = multiplier * last-interval
              max-attempts: 3 # 最大重试次数
      ```
    * 消费者失败重试
        * 消息requeue：当消费者出现异常后，消息会不断requeue（重入队）到队列，再重新发送给消费者，然后再次异常，再次requeue，无限循环，导致mq的消息处理飙升，带来不必要的压力
        * 本地重试
      ```yaml
          # 消费者相关配置
      listener:
        simple:
          # consumer confirm：消费者确认机制
          acknowledge-mode: auto
          # 本地消费重试（避免消息重新入队列）
          retry:
            enabled: true
            initial-interval: 1000 # 初识的失败等待时长为1秒
            multiplier: 1 # 失败的等待时长倍数，下次等待时长 = multiplier * last-interval
            max-attempts: 3 # 最大重试次数
            stateless: true # true无状态；false有状态。如果业务中包含事务，这里改为false
          # 默认情况下，被拒绝的交付是否重新排队。(默认为true)
          default-requeue-rejected: false
      ```
  ● 开启本地重试时，消息处理过程中抛出异常，不会requeue到队列，而是在消费者本地重试
  ● 重试达到最大次数后，Spring会返回ack，消息会被丢弃
    * 失败策略
      在之前的测试中，达到最大重试次数后，消息会被丢弃，这是由Spring内部机制决定的。
      在开启重试模式后，重试次数耗尽，如果消息依然失败，则需要有MessageRecovery接口来处理，它包含三种不同的实现：
      ● RejectAndDontRequeueRecoverer：重试耗尽后，直接reject，丢弃消息。默认就是这种方式
      ● ImmediateRequeueMessageRecoverer：重试耗尽后，返回nack，消息重新入队
      ● RepublishMessageRecoverer：重试耗尽后，将失败消息投递到指定的交换机
      比较优雅的一种处理方案是RepublishMessageRecoverer，失败后将消息投递到一个指定的，专门存放异常消息的队列，后续由人工集中处理。

## 死信交换机

* 死信
    * 消费者使用basic.reject或 basic.nack声明消费失败，并且消息的requeue参数设置为false
    * 消息是一个过期消息，超时无人消费
    * 要投递的队列消息满了，无法投递
    * 死信交换机（dead letter exchange，DLX）
      ```java
      // 声明普通的 simple.queue队列，并且为其指定死信交换机：dl.direct
      @Bean
      public Queue simpleQueue2(){
          return QueueBuilder.durable("simple.queue") // 指定队列名称，并持久化
              .deadLetterExchange("dl.direct") // 指定死信交换机
              .build();
      }
      // 声明死信交换机 dl.direct
      @Bean
      public DirectExchange dlExchange(){
          return new DirectExchange("dl.direct", true, false);
      }
      // 声明存储死信的队列 dl.queue
      @Bean
      public Queue dlQueue(){
          return new Queue("dl.queue", true);
      }
      // 将死信队列 与 死信交换机绑定
      @Bean
      public Binding dlBinding(){
          return BindingBuilder.bind(dlQueue()).to(dlExchange()).with("simple");
      }
      ```
* 死信交换机的使用场景是什么？
  ● 如果队列绑定了死信交换机，死信会投递到死信交换机；
  ● 可以利用死信交换机收集所有消费者处理失败的消息（死信），交由人工处理，进一步提高消息队列的可靠性。

## TTL

一个队列中的消息如果超时未消费，则会变为死信，超时分为两种情况：
● 消息所在的队列设置了超时时间

```java

@Bean
public Queue ttlQueue() {
    return QueueBuilder.durable("ttl.queue") // 指定队列名称，并持久化
            .ttl(10000) // 设置队列的超时时间，10秒
            .deadLetterExchange("dl.ttl.direct") // 指定死信交换机
            .deadLetterRountingKey("dl")
            .build();
}
```

● 消息本身设置了超时时间

```java

@Test
public void testTTLMsg() {
    // 创建消息
    Message message = MessageBuilder
            .withBody("hello, ttl message".getBytes(StandardCharsets.UTF_8))
            .setExpiration("5000")
            .build();
    // 消息ID，需要封装到CorrelationData中
    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
    // 发送消息
    rabbitTemplate.convertAndSend("ttl.direct", "ttl", message, correlationData);
    log.debug("发送消息成功");
}
```

消息超时的两种方式是？
● 给队列设置ttl属性，进入队列后超过ttl时间的消息变为死信
● 给消息设置ttl属性，队列接收到消息超过ttl时间后变为死信
如何实现发送一个消息20秒后消费者才收到消息？
● 给消息的目标队列指定死信交换机
● 将消费者监听的队列绑定到死信交换机
● 发送消息时给消息设置超时时间为20秒

### 延迟队列

利用TTL结合死信交换机，我们实现了消息发出后，消费者延迟收到消息的效果。这种消息模式就称为延迟队列（Delay Queue）模式。
延迟队列的使用场景包括：
● 延迟发送短信
● 用户下单，如果用户在15 分钟内未支付，则自动取消
● 预约工作会议，20分钟后自动通知所有参会人员
因为延迟队列的需求非常多，所以RabbitMQ的官方也推出了一个插件，原生支持延迟队列效果。
https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/

•声明一个交换机，添加delayed属性为true(x-delayed-message)
交换机可以是路由类型
"x-delayed-type" = direct/topic/fanout/other custom exchange type
•发送消息时，添加x-delay头，值为超时时间(x-delay)

### 惰性队列

#### 消息堆积问题

生产者发送消息的速度超过了消费者处理消息的速度，就会导致队列中的消息堆积，直到队列存储消息达到上限。之后发送的消息就会成为死信，可能会被丢弃，这就是消息堆积问题。

#### 惰性队列

从RabbitMQ的3.6.0版本开始，就增加了Lazy Queues的概念，也就是惰性队列。惰性队列的特征如下：
● 接收到消息后直接存入磁盘而非内存
● 消费者要消费消息时才会从磁盘中读取并加载到内存
● 支持数百万条的消息存储

* 使用命令行设置已有队列为惰性队列

```shell
rabbitmqctl set_policy Lazy "^lazy-queue$" '{"queue-mode":"lazy"}' --apply-to queues
```

命令解读：
● rabbitmqctl ：RabbitMQ的命令行工具
● set_policy ：添加一个策略
● Lazy ：策略名称，可以自定义
● "^lazy-queue$" ：用正则表达式匹配队列的名字
● '{"queue-mode":"lazy"}' ：设置队列模式为lazy模式
● --apply-to queues：策略的作用对象，是所有的队列

* 代码声明为惰性队列
    * 基于@Bean声明lazy-queue
      ```java
          @Bean
          public Queue lazyQueue() {
              return QueueBuilder.durable("lazy-queue")
                      .lazy()
                      .build();
          }
      ```
    * 基于@RabbitListener声明LazyQueue
      ```java
      @RabbitListener(queuesToDeclare = @Queue(
              name = "lazy.queue",
              durable = "true",
              arguments = @Argument(name = "x-queue-mode", value = "lazy")))
      public void listenerLazyQueue(String msg) {
          log.info("接收到 lazy.queue 的消息：{}", msg);
      }
      ```

消息堆积问题的解决方案？
● 队列上绑定多个消费者，提高消费速度
● 使用惰性队列，可以再mq中保存更多消息
惰性队列的优点有哪些？
● 基于磁盘存储，消息上限高
● 没有间歇性的page-out，性能比较稳定
惰性队列的缺点有哪些？
● 基于磁盘存储，消息时效性会降低
● 性能受限于磁盘的IO

## 集群
RabbitMQ的是基于Erlang语言编写，而Erlang又是一个面向并发的语言，天然支持集群模式。RabbitMQ的集群有两种模式：
•普通集群：是一种分布式集群，将队列分散到集群的各个节点，从而提高整个集群的并发能力。
•镜像集群：是一种主从集群，普通集群的基础上，添加了主从备份功能，提高集群的数据可用性。
镜像集群虽然支持主从，但主从同步并不是强一致的，某些情况下可能有数据丢失的风险。因此在RabbitMQ的3.8版本以后，推出了新的功能：仲裁队列来代替镜像集群，底层采用Raft协议确保主从的数据一致性。

#### Java代码创建仲裁队列
```java
@Bean
public Queue quorumQueue() {
    return QueueBuilder
        .durable("quorum.queue") // 持久化
        .quorum() // 仲裁队列
        .build();
}
```
#### SpringAMQP连接MQ集群
注意，这里用address来代替host、port方式
```yaml
spring:
  rabbitmq:
    addresses: 192.168.150.105:8071, 192.168.150.105:8072, 192.168.150.105:8073
    username: itcast
    password: 123321
    virtual-host: /
```
