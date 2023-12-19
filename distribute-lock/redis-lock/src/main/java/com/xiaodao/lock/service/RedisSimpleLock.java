package com.xiaodao.lock.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

@Slf4j
@Service
public class RedisSimpleLock {

    private final StringRedisTemplate redisTemplate;

    private static final String LOCK_PREFIX = "spring-redis-lock:";


    /**
     * 为了防止误删其他使用者加的锁，需要检查一下 value
     * 通过使用 lua 脚本的方式保证检查锁与释放锁的原子性
     */
    private static final String RELEASE_LOCK =
            "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                    "    return redis.call(\"del\",KEYS[1]) " +
                    "else " +
                    "    return 0 " +
                    "end";

    public RedisSimpleLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public String lock(String key, Duration duration) {
        final String threadId = Thread.currentThread().getName() + Thread.currentThread().getId();
        final String actualKey = generateKey(key);
        final Boolean isLock = redisTemplate.opsForValue().setIfAbsent(actualKey, threadId, duration);
        if (Boolean.TRUE.equals(isLock)) {
            log.info("{}:获取锁:{}成功", threadId, actualKey);
        } else {
            log.info("{}:获取锁:{}失败", threadId, actualKey);
        }

        return actualKey;
    }

    public String lockWith(String key, String token, Duration duration) {
        final String actualKey = generateKey(key);
        final Boolean isLock = redisTemplate.opsForValue().setIfAbsent(actualKey, token, duration);
        if (Boolean.TRUE.equals(isLock)) {
            log.info("{}:获取锁:{}成功", actualKey, token);
        } else {
            log.info("{}:获取锁:{}失败", actualKey, token);
        }
        return actualKey;
    }

    private static String generateKey(String key) {
        return LOCK_PREFIX + key;
    }

    public boolean releaseNotSafe(String key) {
        final String generatedKey = generateKey(key);
        final Boolean delete = redisTemplate.delete(generatedKey);
        if (Boolean.TRUE.equals(delete)) {
            log.info("解锁:{}成功", generatedKey);
            return true;
        } else {
            log.info("解锁:{}失败", generatedKey);
            return false;
        }
    }

    public boolean release(String key, String value) {
        // 释放锁时，先比较锁对应的 value 值是否相等，避免锁的误释放
        // redisTemplate.execute(RedisScript.of(RELEASE_LOCK),Collections.singletonList(generateKey(key)), value);
        /* if (result != null) {
            log.info("解锁成功：{}，{}", key, value);
        }
        else {
            // log.warn("解锁失败：{}，{}", key, value);
        } */

        // 注意正确返回值，支持的类型 org.springframework.data.redis.connection.ReturnType
        RedisScript<Long> redisScript = RedisScript.of(RELEASE_LOCK, Long.class);
        Long result = redisTemplate.execute(redisScript, Collections.singletonList(generateKey(key)), value);
        log.info("释放锁的结果：" + result);
        if (result != null && result == 1) {
            log.info("解锁成功：{}，{}", key, value);
            return true;
        } else {
            log.warn("解锁失败：{}，{}", key, value);
        }
        return false;
    }


}
