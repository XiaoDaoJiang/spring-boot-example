package com.xiaodao.cache.config.composite;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;

@Slf4j
@Getter
@AllArgsConstructor
public enum CompositeCacheType {

    /**
     * Caffeine backed caching.
     */
    CAFFEINE(CaffeineCacheManager.class),

    /**
     * Redis backed caching.
     */
    REDIS(RedisCacheManager.class),

    NONE(NoOpCacheManager.class),
    ;

    private final Class<? extends CacheManager> cacheManagerClazz;


    public static Integer getOrder(CacheManager cacheManager) {
        final int length = CompositeCacheType.values().length;
        for (int i = 0; i < length; i++) {
            if (CompositeCacheType.values()[i].getCacheManagerClazz().isInstance(cacheManager)) {
                return i;
            }
        }
        log.warn("暂不支持的缓存管理器类型");
        return Integer.MAX_VALUE;
    }

    public static int compareTo(CacheManager c1, CacheManager c2) {
        return getOrder(c1).compareTo(getOrder(c2));
    }
}
