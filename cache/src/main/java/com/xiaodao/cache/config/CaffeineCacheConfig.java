package com.xiaodao.cache.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "caffeine")
@Configuration
public class CaffeineCacheConfig extends CachingConfigurerSupport {

    @Override
    public CacheManager cacheManager() {
        return buildCaffeineCacheManager();
    }

    static CacheManager buildCaffeineCacheManager() {
        return new CaffeineCacheManager();
    }

    @Override
    public KeyGenerator keyGenerator() {
        return super.keyGenerator();
    }
}
