package com.xiaodao.mq.rabbitmq.config;

import com.xiaodao.mq.rabbitmq.message.Demo01Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitConfig implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Bean
    public AsyncRabbitTemplate asyncRabbitTemplate() {
        rabbitTemplate.setBeforePublishPostProcessors(message -> {
            // 可以在此添加 traceId，传递给消费者
            message.getMessageProperties().setHeader("desc", "额外的消息头");
            return message;
        });
        return new AsyncRabbitTemplate(rabbitTemplate);
    }

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


    /**
     * Direct Exchange 示例的配置类
     */
    public static class DirectExchangeDemoConfiguration {

        // 创建 Queue
        @Bean
        public Queue demo01Queue() {
            return new Queue(Demo01Message.QUEUE, // Queue 名字
                    true, // durable: 是否持久化
                    false, // exclusive: 是否排它
                    false); // autoDelete: 是否自动删除
        }

        // 创建 Direct Exchange
        @Bean
        public DirectExchange demo01Exchange() {
            return new DirectExchange(Demo01Message.EXCHANGE,
                    true,  // durable: 是否持久化
                    false);  // exclusive: 是否排它
        }

        // 创建 Binding
        // Exchange：Demo01Message.EXCHANGE
        // Routing key：Demo01Message.ROUTING_KEY
        // Queue：Demo01Message.QUEUE
        @Bean
        public Binding demo01Binding() {
            return BindingBuilder.bind(demo01Queue()).to(demo01Exchange()).with(Demo01Message.ROUTING_KEY);
        }

    }

    public static class DeadLetterExchange {

        @Bean
        public Queue ttlQueue() {
            return QueueBuilder.durable("ttl.queue")
                    .ttl(10000)
                    .deadLetterExchange("dl.ttl.direct") // 指定死信交换机
                    .deadLetterRoutingKey("dl")
                    .build();
        }

        @Bean
        public DirectExchange ttlExchange() {
            return new DirectExchange("ttl.direct");
        }

        @Bean
        public Binding ttlBinding() {
            return BindingBuilder.bind(ttlQueue()).to(ttlExchange()).with("ttl");
        }

    }


    /**
     * 声明延迟插件实现的队列、交换机
     */
    public static class DelayExchange {

        @Bean
        public DirectExchange delayExchange() {
            return ExchangeBuilder
                    .directExchange("delay.direct.exchange")
                    .delayed()
                    .build();
        }

        @Bean
        public Queue delayQueue() {
            return QueueBuilder
                    .durable("delay.queue")
                    .build();
        }

        @Bean
        public Binding delayQueueBinding() {
            return BindingBuilder.bind(delayQueue()).to(delayExchange()).with("delay");
        }


    }


    public static class LazyQueue {
        @Bean
        public Queue lazyQueue() {
            return QueueBuilder.durable("lazy-queue")
                    .lazy()
                    .build();
        }
    }

}