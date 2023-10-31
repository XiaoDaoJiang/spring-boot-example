package com.xiaodao.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "example", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(ExampleProperties.class)
// @AutoConfigureAfter
public class ExampleAutoConfiguration {

    @Bean
    public ThreadPoolTaskScheduler exampleThreadPoolTaskScheduler(ExampleProperties exampleProperties) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(exampleProperties.getPoolSize());
        threadPoolTaskScheduler.setThreadNamePrefix(exampleProperties.getThreadNamePrefix());
        return threadPoolTaskScheduler;
    }

}
