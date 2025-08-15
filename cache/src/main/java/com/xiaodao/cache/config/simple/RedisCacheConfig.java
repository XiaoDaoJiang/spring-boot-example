package com.xiaodao.cache.config.simple;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaodao.cache.config.RedisTtlProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * 直接创建一个自定义 CacheManager，适用于简单自定义场景
 */
@Profile("simple-1")
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis")
@EnableConfigurationProperties({RedisTtlProperties.class, CacheProperties.class})
@Configuration
public class RedisCacheConfig extends CachingConfigurerSupport {


    /**
     * 配置CacheManager
     *
     * @return
     */
    @Bean
    public CacheManager cacheManager(
            CacheProperties cacheProperties,
            RedisTtlProperties redisTtlProperties, LettuceConnectionFactory lettuceConnectionFactory) {
        return buildRedisCacheManager(cacheProperties, redisTtlProperties, lettuceConnectionFactory);
    }

    public static CacheManager buildRedisCacheManager(CacheProperties cacheProperties, RedisTtlProperties redisTtlProperties, LettuceConnectionFactory lettuceConnectionFactory) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        //解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 设置默认配置（失效时间默认为0，永不过期）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // .entryTtl(Duration.ZERO)
                //                .entryTtl(Duration.ofSeconds(20))   //设置缓存失效时间
                .entryTtl(cacheProperties.getRedis().getTimeToLive())
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .prefixCacheNameWith(cacheProperties.getRedis().getKeyPrefix());

        // 自定义缓存失效时间
        Map<String, RedisCacheConfiguration> customCacheConfig = new HashMap<>();
        redisTtlProperties.getTimeToLives().forEach((cacheNames, duration) -> {
            Arrays.stream(StringUtils.split(cacheNames, ",")).forEach(cache -> {
                // 这里还可以自定义缓存键前缀：config.entryTtl(duration)..prefixCacheNameWith("")
                customCacheConfig.put(cache, config.entryTtl(duration));
            });
        });

        RedisCacheManager cacheManager = RedisCacheManager.builder(lettuceConnectionFactory)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(customCacheConfig)
                .build();
        return cacheManager;
    }


}