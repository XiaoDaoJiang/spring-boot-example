package com.xiaodao.lock.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisDistributedLockService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取分布式锁
     *
     * @param lockKey    锁的键
     * @param expireTime 锁的过期时间（毫秒）
     * @return 锁标识符，成功获取返回 UUID，失败返回 null
     */
    public String acquireLock(String lockKey, long expireTime) {
        String lockValue = UUID.randomUUID().toString();
        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, expireTime, TimeUnit.MILLISECONDS);
        if (success != null && success) {
            log.info("加锁成功：{}，{}", lockKey, lockValue);
            return lockValue;
        }
        log.warn("加锁失败：{}", lockKey);
        return null;
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁的键
     * @param lockValue 锁的值（UUID）
     * @return 是否成功释放锁
     */
    public boolean releaseLock(String lockKey, String lockValue) {
        String RELEASE_LOCK =
                "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                        "   return redis.call('del', KEYS[1]) " +
                        "else " +
                        "   return 0 " +
                        "end";

        // 注意正确返回值，支持的类型 org.springframework.data.redis.connection.ReturnType
        RedisScript<Long> redisScript = RedisScript.of(RELEASE_LOCK, Long.class);

        Long result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), lockValue);
        if (result != null && result == 1) {
            log.info("解锁成功：{}，{}", lockKey, lockValue);
            return true;
        } else {
            log.warn("解锁失败：{}，{}", lockKey, lockValue);
        }
        return false;
    }
}
