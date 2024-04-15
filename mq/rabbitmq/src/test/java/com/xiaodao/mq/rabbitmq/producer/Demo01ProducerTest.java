package com.xiaodao.mq.rabbitmq.producer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class Demo01ProducerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Demo01Producer producer;

    @Test
    public void testSyncSend() throws InterruptedException {
        int id = (int) (System.currentTimeMillis() / 1000);
        producer.syncSend(id);
        log.info("[testSyncSend][发送编号：[{}] 发送成功]", id);

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

    @Test
    public void tesSyncSendDefault() throws InterruptedException {
        int id = (int) (System.currentTimeMillis() / 1000);
        producer.syncSendDefault(id);
        log.info("[tesSyncSendDefault][发送编号：[{}] 发送成功]", id);

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

    @Test
    public void testAsyncSend() throws InterruptedException {
        int id = (int) (System.currentTimeMillis() / 1000);
        producer.asyncSend(id).addCallback(new ListenableFutureCallback<Void>() {

            @Override
            public void onFailure(Throwable e) {
                log.info("[testASyncSend][发送编号：[{}] 发送异常]]", id, e);
            }

            @Override
            public void onSuccess(Void aVoid) {
                log.info("[testASyncSend][发送编号：[{}] 发送成功]", id);
            }

        });
        log.info("[testASyncSend][发送编号：[{}] 调用完成]", id);

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

    @Test
    public void testAsyncSend2() throws InterruptedException {
        int id = (int) (System.currentTimeMillis() / 1000);
        final AsyncRabbitTemplate.RabbitConverterFuture<String> converterFuture = producer.asyncSend2(id);
        // do some more work
        log.info("[testASyncSend][发送编号：[{}] 调用完成]", id);

        String reply = null;
        try {
            reply = converterFuture.get();
            log.info("[testASyncSend][发送编号：[{}] 接收返回：{}]", id, reply);
        } catch (ExecutionException e) {
        }
        // 阻塞等待，保证消费
        // new CountDownLatch(1).await();
    }

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

    @Test
    public void testTTLQueue() {
        // 创建消息
        String message = "hello, ttl queue";
        // 消息ID，需要封装到CorrelationData中
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // 发送消息
        rabbitTemplate.convertAndSend("ttl.direct", "ttl", message, correlationData);
        // 记录日志
        log.debug("发送消息成功");
    }


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


    @Test
    public void testDelayedMsg() {
        // 创建消息
        Message message = MessageBuilder
                .withBody("hello, delayed message".getBytes(StandardCharsets.UTF_8))
                .setHeader("x-delay", 10000)
                .build();
        // 消息ID，需要封装到CorrelationData中
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // 发送消息
        rabbitTemplate.convertAndSend("delay.direct", "delay", message, correlationData);
        log.debug("发送消息成功");
    }

}