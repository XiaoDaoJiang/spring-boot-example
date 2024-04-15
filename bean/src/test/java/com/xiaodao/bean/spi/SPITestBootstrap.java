package com.xiaodao.bean.spi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

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

    @Test
    public void testName() {
        System.out.println(Arrays.toString(springSPI.list()));
    }

    @TestConfiguration
    @ComponentScan(basePackages = "com.xiaodao.bean.spi")
    public static class SPITestConfiguration {

    }

}
