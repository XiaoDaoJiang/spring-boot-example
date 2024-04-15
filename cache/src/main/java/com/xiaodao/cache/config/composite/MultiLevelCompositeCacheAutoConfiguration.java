package com.xiaodao.cache.config.composite;

import com.xiaodao.cache.config.composite.MultiLevelCompositeCacheAutoConfiguration.CompositeCacheConfigurationImportSelector;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.context.annotation.Primary;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 自定义二级缓存配置：caffeine + redis
 * spring cache 自动配置不会产生多个 cacheManager
 * 参照
 *
 * @see org.springframework.boot.autoconfigure.cache.CacheCondition
 * @see org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration
 * @see org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
 * 此处组合设计提供一个额外的 cacheManager（caffeine/redis）,另外一个由 spring cache 自动配置产生。
 */
// @Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring.cache.composite", value = "enabled")
@AutoConfigureAfter(CacheAutoConfiguration.class)
@ConditionalOnBean(CacheManager.class)
@EnableConfigurationProperties(CompositeCacheProperties.class)
@Import(CompositeCacheConfigurationImportSelector.class)
public class MultiLevelCompositeCacheAutoConfiguration {


    @Bean
    @Primary
    public CompositeCacheManager compositeCacheManager(Map<String, CacheManager> cacheManagers) {
        final CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
        compositeCacheManager.setCacheManagers(cacheManagers.values().stream().
                sorted(CompositeCacheType::compareTo)
                .collect(Collectors.toList()));

        // 查询缓存时，cacheManager 根据 cache name 获取 cache,获取不到则让下一个 cacheManager 获取
        // caffeineCacheManager(dynamic) 和 redisCacheManager(allowInFlightCacheCreation) 都默认支持动态创建 cacheName-> cache

        // 简单二级缓存（本地缓存+分布式缓存，本地缓存查不到，从分布式缓存中获取，放入本地缓存），同时大多数缓存管理器会动态创建不存在的 cacheName,

        // TODO 对于有些 cacheName 只想放在本地缓存 或者 分布式缓存的需求，可能需要禁用 缓存管理器的动态（惰性）创建，或者通过 自定义 CacheResolver

        // 当测试需要，或者环境问题，不提供任何缓存实现时，使用兜底缓存策略：NoOpCacheManager=>NoOpCache
        compositeCacheManager.setFallbackToNoOpCache(true);
        return compositeCacheManager;
    }


    /**
     * 加载具体缓存实现配置类
     */
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
