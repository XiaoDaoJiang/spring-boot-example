package com.xiaodao.bean;

import com.xiaodao.bean.extension.TestApplicationContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BeanBootApplication {

    public static void main(String[] args) {
        final SpringApplication springApplication = new SpringApplicationBuilder(BeanBootApplication.class).application();
        springApplication.addInitializers(new TestApplicationContextInitializer());
        springApplication.run(args);
        // final ConfigurableApplicationContext run = SpringApplication.run(BeanBootApplication.class, args);
    }
}
