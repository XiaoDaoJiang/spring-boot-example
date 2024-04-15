package com.xiaodao.jta.config;

import com.mysql.cj.jdbc.MysqlXADataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration(proxyBeanMethods = false)
public class OrderDataSourceConfig {

    @Bean
    @ConfigurationProperties("app.datasource.order")
    public DataSourceProperties orderDataSourceProperties() {
        return new DataSourceProperties();
    }



    /**
     * 测试 xa 事务能否与普通事务共存
     */
    /* @Bean
    @ConfigurationProperties("app.datasource.order.hikari")
    public HikariDataSource orderDataSource(@Qualifier("orderDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    } */

    @SneakyThrows
    @Bean("orderXaDataSource")
    public DataSource orderXaDataSource(@Qualifier("orderDataSourceProperties") DataSourceProperties orderDataSourceProperties) {
        final AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(orderDataSourceProperties.getUrl());
        mysqlXaDataSource.setPassword(orderDataSourceProperties.getPassword());
        mysqlXaDataSource.setUser(orderDataSourceProperties.getUsername());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        atomikosDataSourceBean.setXaDataSource(mysqlXaDataSource);
        atomikosDataSourceBean.setUniqueResourceName("orderXaDataSource");
        return atomikosDataSourceBean;
    }

}
