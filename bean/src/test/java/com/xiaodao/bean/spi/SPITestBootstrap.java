package com.xiaodao.bean.spi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 测试SPI
 *
 * @author xiaodaojiang
 * @Classname SPITestBootstrap
 * @Version 1.0.0
 * @Date 2024-04-15 23:00
 * @Created by xiaodaojiang
 */
@SpringBootTest(
        classes = SPITestBootstrap.SPITestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SPITestBootstrap {

    @Autowired
    private SpringSPI springSPI;

    @Autowired
    private NameProvider nameProvider;

    @Test
    public void testName() {
        // auto wired from SpringSpiAutoConfiguration
        System.out.println(nameProvider.getName());
    }

    @Test
    public void testNameProviderFromSPILoadManual() {
        System.out.println(nameProvider);

        NameProvider nameProvider1 = springSPI.getPrototypeBean(NameProvider.class);
        System.out.println(nameProvider1);

        NameProvider nameProvider2 = springSPI.getPrototypeBean(NameProvider.class);
        System.out.println(nameProvider2);

        assertThat(nameProvider1).isNotSameAs(nameProvider2);
    }

    @TestConfiguration
    @ComponentScan(basePackages = "com.xiaodao.bean.spi")
    public static class SPITestConfiguration {

    }

}
