package com.xiaodao.cache.config.composite;

import com.xiaodao.cache.config.composite.MultiLevelCompositeCacheConfig.CompositeCacheConfigurationImportSelector;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 自定义二级缓存配置：caffeine + redis
 * spring cache 自动配置不会产生多个 cacheManager
 * @see org.springframework.boot.autoconfigure.cache.CacheCondition
 * @see org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration
 * @see org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
 * 此处组合设计提供一个额外的 cacheManager（caffeine/redis）,另外一个由 spring cache 自动配置产生。
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(CacheAutoConfiguration.class)
@ConditionalOnBean(CacheManager.class)
@EnableConfigurationProperties(CompositeProperties.class)
@Import({CompositeCacheConfigurationImportSelector.class})
public class MultiLevelCompositeCacheConfig{

    @Bean
    @Primary
    public CompositeCacheManager compositeCacheManager(CacheManager[] cacheManagers) {
        final CompositeCacheManager compositeCacheManager = new CompositeCacheManager(cacheManagers);

        // 查询缓存时，cacheManager 根据 cache name 获取 cache,获取不到则让下一个 cacheManager 获取
        // caffeineCacheManager(dynamic) 和 redisCacheManager(allowInFlightCacheCreation) 都默认支持动态创建 cacheName-> cache


        // 当 cache name获取不到 cache 时的兜底缓存策略：NoOpCacheManager=>NoOpCache
        compositeCacheManager.setFallbackToNoOpCache(true);
        return compositeCacheManager;
    }


    static class CompositeCacheConfigurationImportSelector implements ImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            CompositeCacheType[] types = CompositeCacheType.values();
            String[] imports = new String[types.length];
            for (int i = 0; i < types.length; i++) {
                imports[i] = CompositeCacheConfigurations.getConfigurationClass(types[i]);
            }
            return imports;
        }

    }

}
