package com.xiaodao.mq.rabbitmq.consumer;// Demo01Consumer.java

import com.xiaodao.mq.rabbitmq.message.Demo01Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = Demo01Message.QUEUE)
public class Demo01Consumer {

    @RabbitHandler
    public void onMessage(Demo01Message message) {
        log.info("消费消息：[onMessage][线程名称:{} 消息内容：{}]", Thread.currentThread().getName(), message);
    }

    @RabbitHandler
    public String onMessage(Demo01Message.Demo01AsyncMessage message) {
        log.info("消费消息：[onMessage][线程名称:{} 消息内容：{}]", Thread.currentThread().getName(), message);
        return "ok";
    }

    //    @RabbitHandler(isDefault = true)
    //    public void onMessage(org.springframework.amqp.core.Message message) {
    //        log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    //    }

}