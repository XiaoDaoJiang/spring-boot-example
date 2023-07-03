package com.xiaodao.cache.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * redis配置
 *
 */
@Profile("reference")
@Configuration
@EnableCaching
@EnableConfigurationProperties({RedisTtlProperties.class})
public class RedisCacheCustomConfig extends CachingConfigurerSupport {
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        RedisSerializer<Object> redisSerializer = this.getRedisSerializer();

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(redisSerializer);

        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(redisSerializer);

        template.afterPropertiesSet();
        return template;
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
        config = config.serializeValuesWith(SerializationPair.fromSerializer(this.getRedisSerializer()));
        if (redisProperties.getTimeToLive() != null) {
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
     * 自定义redis缓存名称过期时间
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
    @Bean("redisKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new RedisKeyGenerator();
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
     *
     * @author jonk
     * @date 2022/12/26 23:42
     */
    public static class RedisKeyGenerator implements KeyGenerator {
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
            return String.format("%s:%s:%s",
                    method.getDeclaringClass().getSimpleName(),
                    method.getName(),
                    StringUtils.joinWith("_", params)
            );
        }
    }
}
