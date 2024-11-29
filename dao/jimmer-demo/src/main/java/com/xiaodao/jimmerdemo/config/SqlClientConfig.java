package com.xiaodao.jimmerdemo.config;

import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class SqlClientConfig {

    @Bean
    public PlatformTransactionManager tm1(ApplicationContext ctx, @Qualifier("ds1") DataSource dataSource) {
        return new JimmerTransactionManager(SqlClients.java(ctx, dataSource, null)
        );
    }

    @Bean
    public PlatformTransactionManager tm2(ApplicationContext ctx, @Qualifier("ds2") DataSource dataSource) {
        return new JimmerTransactionManager(SqlClients.java(ctx, dataSource, null)
        );
    }

    // @Bean
    // public JSqlClient sqlClient() {
    //     return TransactionalSqlClients.java();
    // }
}