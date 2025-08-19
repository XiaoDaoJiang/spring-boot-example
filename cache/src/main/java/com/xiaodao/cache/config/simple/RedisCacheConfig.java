package com.xiaodao.cache.config.simple;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.xiaodao.cache.config.RedisTtlProperties;
import com.xiaodao.common.entity.User;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


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
        ObjectMapper mapper = new ObjectMapper();

        // 更安全的类型处理配置
        PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(Object.class) // 允许所有类型，也可以更具体地限制
                // .allowIfSubType(User.class).allowIfBaseType("java.")
                .build();

        mapper.activateDefaultTyping(
                typeValidator,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        // 时间处理
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());

        // 字段处理
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        RedisSerializer<Object> serializer = new GenericJackson2JsonRedisSerializer(mapper);

        // Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        // ObjectMapper om = new ObjectMapper();
        // om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 启用记录类型信息，类似 GenericJackson2JsonRedisSerializer
        // om.activateDefaultTyping(om.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        // jackson2JsonRedisSerializer.setObjectMapper(om);

        // 设置默认配置（失效时间默认为0，永不过期）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // .entryTtl(Duration.ZERO)
                //                .entryTtl(Duration.ofSeconds(20))   //设置缓存失效时间
                .entryTtl(cacheProperties.getRedis().getTimeToLive())
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
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