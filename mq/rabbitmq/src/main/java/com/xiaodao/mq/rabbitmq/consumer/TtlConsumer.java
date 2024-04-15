package com.xiaodao.mq.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TtlConsumer {


    /**
     * 监听死信消息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "dl.ttl.queue", durable = "true"),
            exchange = @Exchange(name = "dl.ttl.direct"),
            key = "ttl"
    ))
    public void listenDlQueue(String msg) {
        log.info("接收到 dl.ttl.queue的延迟消息：{}", msg);
    }
}
