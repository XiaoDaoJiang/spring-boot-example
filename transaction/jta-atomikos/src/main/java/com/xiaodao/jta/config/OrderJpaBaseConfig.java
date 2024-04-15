package com.xiaodao.jta.config;

import com.xiaodao.jta.entity.order.Order;
import com.xiaodao.jta.repository.order.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = OrderRepository.class,
        entityManagerFactoryRef = "orderEntityManagerFactory",
        transactionManagerRef = "orderTransactionManager"
)
public class OrderJpaBaseConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private HibernateProperties hibernateProperties;

    /**
     * 当你为 LocalContainerEntityManagerFactoryBean 自己创建一个Bean时，在创建自动配置的 LocalContainerEntityManagerFactoryBean 时应用的任何定制都会丢失。
     * 例如，在Hibernate的情况下， spring.jpa.hibernate 前缀下的任何属性都不会自动应用到你的 LocalContainerEntityManagerFactoryBean。
     * 如果你依赖这些属性来配置诸如命名策略或DDL模式，你将需要在创建 LocalContainerEntityManagerFactoryBean bean时明确配置。
     */
    @Bean(name = "orderEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean orderEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                            @Qualifier("orderXaDataSource") DataSource orderXaDataSource) {
        return builder
                .dataSource(orderXaDataSource)
                // 自定义 LocalContainerEntityManagerFactoryBean 需明确设置 hibernate 相关配置，否则相关自动配置不生效
                .properties(hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings()))
                .packages(Order.class)
                .jta(true)
                .persistenceUnit("orderPersistenceUnit")
                .build();
    }

    @Bean("orderTransactionManager")
    public PlatformTransactionManager orderTransactionManager(
            final @Qualifier("orderEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactoryBean.getObject()));
    }

}
