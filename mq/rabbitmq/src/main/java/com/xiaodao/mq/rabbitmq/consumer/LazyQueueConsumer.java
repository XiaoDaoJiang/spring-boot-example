package com.xiaodao.mq.rabbitmq.consumer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LazyQueueConsumer {

    @RabbitListener(queuesToDeclare = @Queue(
            name = "lazy.queue",
            durable = "true",
            arguments = @Argument(name = "x-queue-mode", value = "lazy")))
    public void listenerLazyQueue(String msg) {
        log.info("接收到 lazy.queue 的消息：{}", msg);
    }

}
