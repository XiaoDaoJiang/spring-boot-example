package com.xiaodao.lock.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.ComponentScan;

import java.time.Duration;
import java.util.UUID;


@ComponentScan("com.xiaodao.lock.service")
@DataRedisTest
public class RedisSimpleLockTest {

    @Autowired
    private RedisSimpleLock redisSimpleLock;

    @Test
    public void testLock() {
        redisSimpleLock.lock("testLock", Duration.ofSeconds(300));

    }

    @Test
    public void releaseNotSafe() {
        redisSimpleLock.releaseNotSafe("testLock");
    }

    @Test
    public void lock_and_release_by_lua() {
        String key = "order:car:aito";
        String token = UUID.randomUUID().toString().replace("-", "");
        redisSimpleLock.lockWith(key, token, Duration.ofMinutes(10));

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
        }

        redisSimpleLock.release(key, token);
    }


}
