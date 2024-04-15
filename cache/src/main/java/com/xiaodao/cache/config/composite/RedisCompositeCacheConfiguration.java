package com.xiaodao.cache.config.composite;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaodao.cache.CacheableWithTTL;
import com.xiaodao.cache.CustomRedisCacheManager;
import com.xiaodao.cache.config.RedisTtlProperties;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.cache.BatchStrategies;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

/**
 * 替换 RedisCacheConfiguration 默认配置
 * redis cacheManagerCustomizers 配置，适用于深度自定义，同时又保证原始默认配置的兜底，保证灵活性
 *
 * @link org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration#cacheManager(CacheProperties, CacheManagerCustomizers, ObjectProvider, ObjectProvider, RedisConnectionFactory, ResourceLoader)
 */
// @Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisConnectionFactory.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnBean(RedisConnectionFactory.class)
@ConditionalOnMissingBean(RedisCacheManager.class)
@Conditional(CompositeCacheCondition.class)
@EnableConfigurationProperties({RedisTtlProperties.class})
public class RedisCompositeCacheConfiguration extends CachingConfigurerSupport {


    @Bean
    RedisCacheManager redisCacheManager(CacheProperties cacheProperties, CacheManagerCustomizers cacheManagerCustomizers,
                                        ObjectProvider<RedisCacheManagerBuilderCustomizer> redisCacheManagerBuilderCustomizers,
                                        RedisConnectionFactory redisConnectionFactory) {
        final RedisCacheConfiguration defaultCacheConfiguration = redisCacheConfiguration(cacheProperties);
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(
                defaultCacheConfiguration);
        List<String> cacheNames = cacheProperties.getCacheNames();
        if (!cacheNames.isEmpty()) {
            builder.initialCacheNames(new LinkedHashSet<>(cacheNames));
        }
        if (cacheProperties.getRedis().isEnableStatistics()) {
            builder.enableStatistics();
        }

        // 定制调整 cacheName ttl
        redisCacheManagerBuilderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
        final RedisCacheManager redisCacheManager = builder.build();

        final RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory, BatchStrategies.scan(1000));
        return cacheManagerCustomizers.customize(new CustomRedisCacheManager(redisCacheWriter, defaultCacheConfiguration,
                redisCacheManager.getCacheConfigurations(), true));
    }


    /**
     * 自定义RedisCacheConfiguration 设置值序列化方式，改配置会覆盖默认的缓存配置
     *
     * @param cacheProperties 缓存属性
     * @return RedisCacheConfiguration
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 缓存值 序列化与反序列化
        config = config.serializeValuesWith(SerializationPair.fromSerializer(this.getRedisSerializer()));
        if (redisProperties.getTimeToLive() != null) {
            // 兜底默认过期时间
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }

    /**
     * 自定义redis缓存名称与过期时间
     *
     * @return RedisCacheManagerBuilderCustomizer
     */
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(RedisCacheConfiguration redisCacheConfiguration, RedisTtlProperties redisTtl) {
        return (builder) -> {
            // 自定义缓存过期时间,类中已经初始化
            redisTtl.getTimeToLives().forEach((k, v) -> {
                Arrays.stream(StringUtils.split(k, ",")).forEach(cache -> {
                    builder.withCacheConfiguration(cache, redisCacheConfiguration.entryTtl(v));
                });
            });
        };
    }

    /**
     * 设置Redis Key 缓存策略
     *
     * @return 结果
     */
    @Override
    @Bean("redisTTLKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new RedisTTLKeyGenerator();
    }

    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    private RedisSerializer<Object> getRedisSerializer() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        GenericJackson2JsonRedisSerializer.registerNullValueSerializer(mapper, null);
        return new GenericJackson2JsonRedisSerializer(mapper);
    }


    /**
     * Redis Key 生成策略
     */
    public static class RedisTTLKeyGenerator implements KeyGenerator {
        /**
         * Generate a key for the given method and its parameters.
         *
         * @param target the target instance
         * @param method the method being called
         * @param params the method parameters (with any var-args expanded)
         * @return a generated key
         */
        @Override
        public Object generate(Object target, Method method, Object... params) {
            final String key = String.format("%s:%s:%s",
                    method.getDeclaringClass().getSimpleName(),
                    method.getName(),
                    StringUtils.joinWith("_", params));
            final CacheableWithTTL annotation = AnnotationUtils.getAnnotation(method, CacheableWithTTL.class);
            if (annotation != null) {
                return new TTLKey(key, Duration.of(annotation.ttl(), annotation.unit()));
            }
            return new TTLKey(key);
        }
    }

    @Getter
    public static class TTLKey {
        private final String key;

        private Duration ttl = Duration.ZERO;

        public TTLKey(String key, Duration ttl) {
            this.key = key;
            this.ttl = ttl;
        }

        public TTLKey(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TTLKey ttlKey = (TTLKey) o;

            return Objects.equals(key, ttlKey.key);
        }

        @Override
        public int hashCode() {
            return key != null ? key.hashCode() : 0;
        }
    }


}
