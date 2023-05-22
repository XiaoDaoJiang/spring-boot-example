package com.xiaodao.dao.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DaoJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DaoJpaApplication.class, args);
    }
}
