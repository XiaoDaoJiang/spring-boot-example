package com.xiaodao.mq.rabbitmq.producer;// Demo01Producer.java

import com.xiaodao.mq.rabbitmq.message.Demo01Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class Demo01Producer {

    private final RabbitTemplate rabbitTemplate;

    private final AsyncRabbitTemplate asyncRabbitTemplate;

    public Demo01Producer(RabbitTemplate rabbitTemplate, AsyncRabbitTemplate asyncRabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.asyncRabbitTemplate = asyncRabbitTemplate;
    }

    public void syncSend(Integer id) {
        // 创建 Demo01Message 消息
        Demo01Message message = new Demo01Message();
        message.setId(id);
        // 同步发送消息
        rabbitTemplate.convertAndSend(Demo01Message.EXCHANGE, Demo01Message.ROUTING_KEY, message);
    }

    /**
     * RabbitMQ 有一条默认的 Exchange: (AMQP default) 规则：
     * The default exchange is implicitly bound to every queue, with a routing key equal to the queue name. It is not possible to explicitly bind to, or unbind from the default exchange. It also cannot be deleted 。
     * 翻译过来的意思：默认交换器，隐式地绑定到每个队列，路由键等于队列名称。
     */
    public void syncSendDefault(Integer id) {
        // 创建 Demo01Message 消息
        Demo01Message message = new Demo01Message();
        message.setId(id);
        // 同步发送消息
        rabbitTemplate.convertAndSend(Demo01Message.QUEUE, message);
    }

    @Async
    public ListenableFuture<Void> asyncSend(Integer id) {
        try {
            // 发送消息
            this.syncSend(id);
            // 返回成功的 Future
            return AsyncResult.forValue(null);
        } catch (Throwable ex) {
            // 返回异常的 Future
            return AsyncResult.forExecutionException(ex);
        }
    }

    public AsyncRabbitTemplate.RabbitConverterFuture<String> asyncSend2(Integer id) {
        Demo01Message.Demo01AsyncMessage message = new Demo01Message.Demo01AsyncMessage(id);
        final AsyncRabbitTemplate.RabbitConverterFuture<String> converterFuture =
                this.asyncRabbitTemplate.convertSendAndReceive(Demo01Message.EXCHANGE, Demo01Message.ROUTING_KEY,
                        message);
        converterFuture.addCallback(new ListenableFutureCallback<String>() {

            @Override
            public void onSuccess(String result) {
                log.info("[asyncSend2][发送编号：[{}] 发送成功：{}]", id, result);
            }


            @Override
            public void onFailure(Throwable e) {
                log.info("[asyncSend2][发送编号：[{}] 发送异常]]", id, e);
            }


        });
        return converterFuture;
    }

}