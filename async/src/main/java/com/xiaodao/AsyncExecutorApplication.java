package com.xiaodao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 */
@EnableAsync
@SpringBootApplication
public class AsyncExecutorApplication {


    public static void main(String[] args) {
        SpringApplication.run(AsyncExecutorApplication.class, args);
    }


}
