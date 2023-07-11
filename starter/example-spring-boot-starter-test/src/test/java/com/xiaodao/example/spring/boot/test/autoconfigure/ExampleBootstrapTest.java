package com.xiaodao.example.spring.boot.test.autoconfigure;

import com.xiaodao.example.spring.boot.ExampleTestBootstrap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootTest(classes = {ExampleTestBootstrap.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ExampleBootstrapTest {

    @Autowired
    private ApplicationContext applicationContext;


    @Test
    public void testAutoConfig() {
        final ThreadPoolTaskScheduler bean = applicationContext.getBean(ThreadPoolTaskScheduler.class);
        System.out.println(bean);
    }

    @Test
    public void testAutoConfigEnabled() {
        final ThreadPoolTaskScheduler bean = applicationContext.getBean(ThreadPoolTaskScheduler.class);
        Assertions.assertNotNull(bean);

        final Object bean1 = applicationContext.getBean("exampleThreadPoolTaskScheduler");
        Assertions.assertNotNull(bean1);
    }


}
