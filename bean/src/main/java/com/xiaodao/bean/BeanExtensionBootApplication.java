package com.xiaodao.bean;

import com.xiaodao.bean.extension.TestApplicationContextInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.xiaodao.bean.extension")
public class BeanExtensionBootApplication {


    public static void main(String[] args) {
        final SpringApplication springApplication = new SpringApplicationBuilder(BeanExtensionBootApplication.class).application();
        springApplication.addInitializers(new TestApplicationContextInitializer());
        final ConfigurableApplicationContext applicationContext = springApplication.run(args);
    }


}
