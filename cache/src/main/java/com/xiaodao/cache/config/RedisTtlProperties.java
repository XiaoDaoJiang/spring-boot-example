package com.xiaodao.cache.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis过期时间配置
 * <pre>
 *     spring:
 *       cache:
 *         time-to-live:
 *             cache-name1: 10m
 *             # 多个key 请求使用逗号分割，并且使用"[]"包裹
 *             "[cache1,cache2]": 20h
 * </pre>
 *
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "spring.cache")
public class RedisTtlProperties {
    /**
     * 缓存过期时间
     */
    private Map<String, Duration> timeToLives = new HashMap<>();
}