package com.xiaodao.springbootjpa.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * JpaMultiDataSourceConfiguration
 *
 * @author jianght
 * @date 2021/9/30
 */
// @Configuration
// @EnableTransactionManagement
// @EnableJpaRepositories(
// 		entityManagerFactoryRef = "primaryEntityManagerFactory",
// 		transactionManagerRef = "primaryTransactionManager",
// 		basePackages = {"com.xiaodao.springbootjpa.dao"})
public class JpaMultiDataSourceConfiguration {

	/*	@Bean
	@ConfigurationProperties("app.jpa.first")
	public JpaProperties jpaProperties() {
		return new JpaProperties();
	}*/

	/*@Bean
	public LocalContainerEntityManagerFactoryBean firstEntityManagerFactory(DataSource firstDataSource,
																			JpaProperties firstJpaProperties) {
		EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder(firstJpaProperties);
		return builder.dataSource(firstDataSource).packages(Order.class).persistenceUnit("firstDs").build();
	}

	private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties jpaProperties) {
		JpaVendorAdapter jpaVendorAdapter = createJpaVendorAdapter(jpaProperties);
		return new EntityManagerFactoryBuilder(jpaVendorAdapter, jpaProperties.getProperties(), null);
	}

	private JpaVendorAdapter createJpaVendorAdapter(JpaProperties jpaProperties) {
		// ... map JPA properties as needed
		return new HibernateJpaVendorAdapter();
	}*/

	@Primary
	@ConfigurationProperties("spring.jpa")
	@Bean(name = "primaryEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
			EntityManagerFactoryBuilder primaryEntityManagerFactoryBuilder,
			@Qualifier("myRoutingDataSource") DataSource primaryDataSource,
			JpaProperties jpaProperties) {

		/*Map<String, String> primaryJpaProperties = new HashMap<>();
		primaryJpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		primaryJpaProperties.put("hibernate.hbm2ddl.auto", "create-drop");*/

		return primaryEntityManagerFactoryBuilder
				.dataSource(primaryDataSource)
				.packages("com.xiaodao.springbootjpa.model")
				.persistenceUnit("myRoutingDataSource")
				.properties(jpaProperties.getProperties())
				.build();
	}

	@Primary
	@Bean(name = "primaryTransactionManager")
	public PlatformTransactionManager primaryTransactionManager(
			@Qualifier("primaryEntityManagerFactory") EntityManagerFactory primaryEntityManagerFactory) {

		return new JpaTransactionManager(primaryEntityManagerFactory);
	}

}
