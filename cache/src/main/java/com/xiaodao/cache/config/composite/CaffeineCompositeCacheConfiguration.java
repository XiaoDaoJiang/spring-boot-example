package com.xiaodao.cache.config.composite;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * 多级缓存，自定义 Caffeine 缓存配置类
 *
 * @link org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration
 */
// @Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Caffeine.class, CaffeineCacheManager.class})
@ConditionalOnMissingBean(CaffeineCacheManager.class)
@Conditional(CompositeCacheCondition.class)
public class CaffeineCompositeCacheConfiguration {

    public CaffeineCompositeCacheConfiguration() {
        System.out.println("CaffeineCompositeCacheConfiguration() ");
    }

    @Bean
    CaffeineCacheManager caffeineCacheManager(CacheProperties cacheProperties, CacheManagerCustomizers customizers,
                                              ObjectProvider<Caffeine<Object, Object>> caffeine, ObjectProvider<CaffeineSpec> caffeineSpec,
                                              ObjectProvider<CacheLoader<Object, Object>> cacheLoader) {
        CaffeineCacheManager cacheManager = createCacheManager(cacheProperties, caffeine, caffeineSpec, cacheLoader);
        List<String> cacheNames = cacheProperties.getCacheNames();
        if (!CollectionUtils.isEmpty(cacheNames)) {
            cacheManager.setCacheNames(cacheNames);
        }
        return customizers.customize(cacheManager);
    }

    private CaffeineCacheManager createCacheManager(CacheProperties cacheProperties,
                                                    ObjectProvider<Caffeine<Object, Object>> caffeine,
                                                    // 缓存指定配置
                                                    ObjectProvider<CaffeineSpec> caffeineSpec,
                                                    ObjectProvider<CacheLoader<Object, Object>> cacheLoader) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        setCacheBuilder(cacheProperties, caffeineSpec.getIfAvailable(), caffeine.getIfAvailable(), cacheManager);
        cacheLoader.ifAvailable(cacheManager::setCacheLoader);
        return cacheManager;
    }

    private void setCacheBuilder(CacheProperties cacheProperties, CaffeineSpec caffeineSpec,
                                 Caffeine<Object, Object> caffeine, CaffeineCacheManager cacheManager) {
        String specification = cacheProperties.getCaffeine().getSpec();
        if (StringUtils.hasText(specification)) {
            cacheManager.setCacheSpecification(specification);
        } else if (caffeineSpec != null) {
            cacheManager.setCaffeineSpec(caffeineSpec);
        } else if (caffeine != null) {
            cacheManager.setCaffeine(caffeine);
        }
    }

}
