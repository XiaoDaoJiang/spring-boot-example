package com.xiaodao.bean.extension;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author jianghaitao
 * @Classname TestBeanDefinitionRegistryPostProcessorTest
 * @Version 1.0.0
 * @Date 2025-01-10 13:44
 * @Created by jianghaitao
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = TestBeanDefinitionRegistryPostProcessorTest.BeanDefinitionRegistryPostProcessorTestConfiguration.class)
class TestBeanDefinitionRegistryPostProcessorTest {

    @TestConfiguration
    @ComponentScan(basePackages = "com.xiaodao.bean.extension")
    public static class BeanDefinitionRegistryPostProcessorTestConfiguration {

    }

    @Test
    public void init() {
        System.out.println("TestBeanDefinitionRegistryPostProcessorTest");
    }

}