package com.xiaodao.jimmerdemo.config;

import com.xiaodao.jimmerdemo.model.TreeNode;
import org.babyfish.jimmer.spring.SqlClients;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.runtime.DatabaseValidationMode;
import org.babyfish.jimmer.sql.runtime.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SqlClientConfig {

    // @Bean
    // public PlatformTransactionManager tm1(ApplicationContext ctx, @Qualifier("ds1") DataSource dataSource) {
    //     return new JimmerTransactionManager(SqlClients.java(ctx, dataSource, null)
    //     );
    // }
    //
    // @Bean
    // public PlatformTransactionManager tm2(ApplicationContext ctx, @Qualifier("ds2") DataSource dataSource) {
    //     return new JimmerTransactionManager(SqlClients.java(ctx, dataSource, null)
    //     );
    // }
    //

    /**
     * 通过这个方法创建一个 JSqlClient 实例,本质是 JTransactionalSqlClient,其 initialize() 方法为空实现,所以不会像JSpringSqlClient一样被 SqlClientInitializer 初始化进行数据库验证
     */
    // @Bean
    // public JSqlClient sqlClient() {
    //     return TransactionalSqlClients.java();
    // }

    // sqlClient 用于spring data风格的repository
    @Bean({"sqlClient", "sqlClient1"})
    public JSqlClient sqlClient1(
            ApplicationContext ctx,
            @Qualifier("ds1") DataSource dataSource
    ) {
        return SqlClients.java(ctx, dataSource, builder -> {
            // builder.setDatabaseValidationCatalog("jimmer_demo");
        });
    }

    @Bean("sqlClient2")
    public JSqlClient sqlClient2(
            ApplicationContext ctx,
            @Qualifier("ds2") DataSource dataSource
    ) {
        return SqlClients.java(ctx, dataSource, builder -> {
            builder/* .setDatabaseValidationCatalog("jimmer_demo_2") */
                    .setEntityManager(EntityManager.fromResources(null, clazz -> !clazz.equals(TreeNode.class)));
            // .setEntityManager(EntityManager.fromResources(null, clazz -> clazz.getPackageName().startsWith("com.xiaodao.jimmerdemo")));
        });
    }
}