package com.xiaodao.jta.config;

import com.xiaodao.jta.entity.member.Member;
import com.xiaodao.jta.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = MemberRepository.class,
        entityManagerFactoryRef = "memberEntityManagerFactory",
        transactionManagerRef = "memberTransactionManager"
)
public class MemberJpaBaseConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private HibernateProperties hibernateProperties;

    @Bean(name = "entityManagerPrimary")
    public EntityManager entityManager(LocalContainerEntityManagerFactoryBean factoryBean) {
        return factoryBean.getObject().createEntityManager();
    }

    /**
     * @see org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration
     * 当你为 LocalContainerEntityManagerFactoryBean 自己创建一个Bean时，在创建自动配置的 LocalContainerEntityManagerFactoryBean 时应用的任何定制都会丢失。
     * 例如，在Hibernate的情况下， spring.jpa.hibernate 前缀下的任何属性都不会自动应用到你的 LocalContainerEntityManagerFactoryBean。
     * 如果你依赖这些属性来配置诸如命名策略或DDL模式，你将需要在创建 LocalContainerEntityManagerFactoryBean bean时明确配置。
     */
    @Primary
    @Bean(name = "memberEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean memberEntityManagerFactory(EntityManagerFactoryBuilder builder, DataSource memberDataSource) {
        return builder
                .dataSource(memberDataSource)
                // 标记为jta事务，否则就算指定了使用 jta 事务管理器开启事务同样会因为触发 单个分支的自动提交而抛出异常
                // @Transactional(transactionManager = "jtaTransactionManager")
                // com.atomikos.jdbc.AtomikosSQLException: Cannot call method 'commit' while a global transaction is running
                .jta(true)
                .packages(Member.class)
                .properties(hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings()))
                .persistenceUnit("memberPersistenceUnit")
                .build();
    }

    @Primary
    @Bean(name = "memberTransactionManager")
    public PlatformTransactionManager memberTransactionManager(
            final @Qualifier("memberEntityManagerFactory") LocalContainerEntityManagerFactoryBean memberEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(memberEntityManagerFactory.getObject()));
    }

}
