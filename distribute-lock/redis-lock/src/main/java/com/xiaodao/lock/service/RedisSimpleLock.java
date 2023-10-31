package com.xiaodao.lock.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class RedisSimpleLock {

    private final StringRedisTemplate redisTemplate;

    private static final String LOCK_PREFIX = "spring-redis-lock:";

    public RedisSimpleLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void lock(String key) {
        final String threadId = Thread.currentThread().getName() + Thread.currentThread().getId();
        final String actualKey = LOCK_PREFIX + key;
        final Boolean isLock = redisTemplate.opsForValue().setIfAbsent(actualKey, threadId, Duration.ofSeconds(300));
        if (Boolean.TRUE.equals(isLock)) {
            log.info("{}:获取锁:{}成功", threadId, actualKey);
        } else {
            log.info("{}:获取锁:{}失败", threadId, actualKey);
        }
    }

}
