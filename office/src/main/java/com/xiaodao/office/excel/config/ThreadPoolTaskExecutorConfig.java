package com.xiaodao.office.excel.config;

import cn.hutool.core.thread.NamedThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolTaskExecutorConfig {

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolExecutor threadPoolExecutor() {
        BlockingQueue<Runnable> blockingDeque = new ArrayBlockingQueue<>(100, false);
        ThreadFactory threadFactory = new NamedThreadFactory("thread-hrs-rankReform", false);
        return new ThreadPoolExecutor(10, 10, 10, TimeUnit.MINUTES, blockingDeque, threadFactory);
    }

}
