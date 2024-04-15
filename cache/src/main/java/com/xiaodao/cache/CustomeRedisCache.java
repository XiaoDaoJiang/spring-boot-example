package com.xiaodao.cache;

import com.xiaodao.cache.config.composite.RedisCompositeCacheConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;

public class CustomeRedisCache extends RedisCache {

    private final String name;

    private final Duration ttl;

    /**
     * Create new {@link RedisCache}.
     *
     * @param name        must not be {@literal null}.
     * @param cacheWriter must not be {@literal null}.
     * @param cacheConfig must not be {@literal null}.
     */
    protected CustomeRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
        this.name = name;
        this.ttl = cacheConfig.getTtl();
    }


    @Override
    protected Object lookup(Object key) {
        byte[] value = getNativeCache().get(name, serializeCacheKey(this.createTTLCacheKey(key).getKey()));

        if (value == null) {
            return null;
        }

        return deserializeCacheValue(value);
    }


    @Override
    public void put(Object key, Object value) {
        Object cacheValue = preProcessCacheValue(value);
        if (!isAllowNullValues() && cacheValue == null) {

            throw new IllegalArgumentException(String.format(
                    "Cache '%s' does not allow 'null' values. Avoid storing null via '@Cacheable(unless=\"#result == null\")' or configure RedisCache to allow 'null' via RedisCacheConfiguration.",
                    name));
        }
        final RedisCompositeCacheConfiguration.TTLKey ttlKey = this.createTTLCacheKey(key);
        super.getNativeCache().put(name, serializeCacheKey(ttlKey.getKey()), serializeCacheValue(cacheValue), ttlKey.getTtl());
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        Object cacheValue = preProcessCacheValue(value);

        if (!isAllowNullValues() && cacheValue == null) {
            return get(key);
        }

        final RedisCompositeCacheConfiguration.TTLKey ttlKey = this.createTTLCacheKey(key);

        byte[] result = super.getNativeCache().putIfAbsent(name, serializeCacheKey(ttlKey.getKey()),
                serializeCacheValue(cacheValue), ttlKey.getTtl());

        if (result == null) {
            return null;
        }

        return new SimpleValueWrapper(fromStoreValue(deserializeCacheValue(result)));
    }

    private RedisCompositeCacheConfiguration.TTLKey createTTLCacheKey(Object key) {
        String cacheKey;
        Duration duration = this.ttl;
        if (key instanceof RedisCompositeCacheConfiguration.TTLKey) {
            final RedisCompositeCacheConfiguration.TTLKey ttlKey = (RedisCompositeCacheConfiguration.TTLKey) key;
            // 返回转换后的key与ttl
            duration = ttlKey.getTtl();
            cacheKey = createCacheKey(ttlKey.getKey());
        } else {
            cacheKey = createCacheKey(key);
            if (StringUtils.contains(cacheKey, "#TTL:")) {
                final String[] keyDurationStr = cacheKey.split("#TTL:");
                cacheKey = keyDurationStr[0];
                duration = Duration.parse(keyDurationStr[1]);
            }
        }
        return new RedisCompositeCacheConfiguration.TTLKey(cacheKey, duration);

    }

    private byte[] createAndConvertCacheKey(Object key) {
        return serializeCacheKey(createCacheKey(key));
    }
}
