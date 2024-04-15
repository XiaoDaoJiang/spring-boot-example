package com.xiaodao.dao.jpa.config;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManager;

@TestConfiguration
public class EntityManagerConfig {


    // @Primary
    @Bean(name = "entityManagerPrimary")
    public EntityManager entityManager(LocalContainerEntityManagerFactoryBean factoryBean) {
        return factoryBean.getObject().createEntityManager();
    }
}
