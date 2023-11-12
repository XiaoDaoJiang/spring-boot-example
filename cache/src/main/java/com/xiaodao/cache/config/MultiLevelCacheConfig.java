package com.xiaodao.cache.config;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.Collections;
import java.util.Optional;

@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "generic")
@Configuration
public class MultiLevelCacheConfig extends CachingConfigurerSupport {

    @Autowired
    private RedisTtlProperties redisTtlProperties;

    @Autowired
    private LettuceConnectionFactory lettuceConnectionFactory;

    @Override
    public CacheManager cacheManager() {

        CompositeCacheManager cacheManager = new CompositeCacheManager();
        cacheManager.setCacheManagers(Lists.newArrayList(
                CaffeineCacheConfig.buildCaffeineCacheManager(),
                RedisCacheConfig.buildRedisCacheManager(redisTtlProperties, lettuceConnectionFactory)));

        cacheManager.setFallbackToNoOpCache(true);
    }

    @Override
    public CacheResolver cacheResolver() {
        return super.cacheResolver();
    }

    @Override
    public KeyGenerator keyGenerator() {
        return super.keyGenerator();
    }
}
