package com.xiaodao.mybatismultidatasource.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;


/**
 * flyWay 多数据源初始化
 */
@Configuration
public class FlyWayMultiDataSourceConfiguration {


    @Bean
    public FlyWayMultiDataSourceMigrationInitializer flyWayMultiDataSourceMigrationInitializer(MyRoutingDataSource routingDataSource) {
        return new FlyWayMultiDataSourceMigrationInitializer(routingDataSource);
    }

    public static class FlyWayMultiDataSourceMigrationInitializer implements InitializingBean {


        private final MyRoutingDataSource routingDataSource;

        public FlyWayMultiDataSourceMigrationInitializer(MyRoutingDataSource routingDataSource) {
            this.routingDataSource = routingDataSource;
        }


        @Override
        public void afterPropertiesSet() {
            final Map<Object, DataSource> resolvedDataSources = routingDataSource.getResolvedDataSources();
            final DataSource resolvedDefaultDataSource = routingDataSource.getResolvedDefaultDataSource();
            resolvedDataSources.forEach((k, ds) -> {
                if (!ds.equals(resolvedDefaultDataSource)) {
                    final Flyway flyway = Flyway.configure().dataSource(ds).load();
                    if (flyway != null) {
                        flyway.migrate();
                    }
                }
            });
        }
    }
}


