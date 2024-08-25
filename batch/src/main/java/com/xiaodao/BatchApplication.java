package com.xiaodao;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * 启动类所在包，无法直接通过指定 @ComponentScan 扫描到依赖的common 包中有 respository，entity（可能与@ComponentScan并不一致，需要特殊指定 @EnableJpaRepositories和 @EntityScan）
 * <p>
 * 当使用了@ComponentScan或@SpringBootApplication 显式指定了basePackages时，不会再默认扫描当前包及子包，所以需要同时显式指定当前所在包
 */
@EnableBatchProcessing
// @EnableJpaRepositories(basePackages = {"com.xiaodao.common"})
// @EntityScan(basePackages = "com.xiaodao.common")
// @ComponentScan(basePackages = {"com.xiaodao.batch", "com.xiaodao.common"})
// @ComponentScan(basePackages = {"com.xiaodao.common.respository", "com.xiaodao.common.service", "com.xiaodao.batch"})
@SpringBootApplication
public class BatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }


    @Bean
    ApplicationRunner applicationRunner(Environment environment) {
        return args -> {
            System.out.println(Arrays.toString(environment.getActiveProfiles()));
            System.out.println(environment.getProperty("spring.config.activate.on-profile"));
            System.out.println(environment.getProperty("spring.datasource.url"));
            System.out.println("Hello, World!");
        };
    }


}
