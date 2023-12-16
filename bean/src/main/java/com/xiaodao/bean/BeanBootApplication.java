package com.xiaodao.bean;

import com.xiaodao.bean.extension.TestApplicationContextInitializer;
import com.xiaodao.bean.scope.ProtoTypeTestBean;
import com.xiaodao.bean.scope.ProtoTypeTestBeanB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.xiaodao.bean.scope")
public class BeanBootApplication implements CommandLineRunner {

    @Autowired
    private ProtoTypeTestBean protoTypeTestBean;

    @Autowired
    private ProtoTypeTestBeanB protoTypeTestBeanB;

    public static void main(String[] args) {
        final SpringApplication springApplication = new SpringApplicationBuilder(BeanBootApplication.class).application();
        springApplication.addInitializers(new TestApplicationContextInitializer());
        final ConfigurableApplicationContext applicationContext = springApplication.run(args);

        log.info("applicationContext:{}",applicationContext.getBean(ProtoTypeTestBean.class));
        log.info("applicationContext:{}",applicationContext.getBean(ProtoTypeTestBean.class));

        log.info("applicationContext:{}",applicationContext.getBean(ProtoTypeTestBeanB.class));
        log.info("applicationContext:{}",applicationContext.getBean(ProtoTypeTestBeanB.class));
        // final ConfigurableApplicationContext run = SpringApplication.run(BeanBootApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        log.info("run:{}",protoTypeTestBean);
        log.info("run:{}",protoTypeTestBean);
        log.info("run:{}",protoTypeTestBean);

        log.info("run:{}",protoTypeTestBeanB);
        log.info("run:{}",protoTypeTestBeanB);
        log.info("run:{}",protoTypeTestBeanB);


        log.info("run:{}",protoTypeTestBean.getProtoTypeTestBeanB());
        protoTypeTestBeanB.test();
        protoTypeTestBean.testA();
        protoTypeTestBean.getProtoTypeTestBeanB().test();

    }
}
