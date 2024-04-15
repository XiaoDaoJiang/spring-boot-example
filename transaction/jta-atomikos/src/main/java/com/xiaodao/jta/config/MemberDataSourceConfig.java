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
public class MemberDataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.member")
    public DataSourceProperties memberDataSourceProperties() {
        return new DataSourceProperties();
    }


   /*  @Bean
    @Primary
    @ConfigurationProperties("app.datasource.member.hikari")
    public HikariDataSource memberDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    } */

    @SneakyThrows
    @Bean("memberXaDataSource")
    @Primary
    public DataSource memberXaDataSource(@Qualifier("memberDataSourceProperties") DataSourceProperties memberDataSourceProperties) {
        final AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(memberDataSourceProperties.getUrl());
        mysqlXaDataSource.setPassword(memberDataSourceProperties.getPassword());
        mysqlXaDataSource.setUser(memberDataSourceProperties.getUsername());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        atomikosDataSourceBean.setXaDataSource(mysqlXaDataSource);
        atomikosDataSourceBean.setUniqueResourceName("memberXaDataSource");
        atomikosDataSourceBean.setTestQuery("SELECT 1");
        return atomikosDataSourceBean;
    }

}
