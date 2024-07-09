package com.xiaodao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class UnitTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnitTestApplication.class, args);
    }
}
