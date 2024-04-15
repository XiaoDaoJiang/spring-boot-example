package com.xiaodao.cache.config.composite;

import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
// @ConditionalOnMissingBean(CacheManager.class)
@Conditional(CompositeCacheCondition.class)
public class NoOpCacheConfiguration {

    public NoOpCacheConfiguration() {
        System.out.println("NoOpCacheConfiguration()");
    }

    @Bean
    NoOpCacheManager cacheManager() {
        return new NoOpCacheManager();
    }

}
