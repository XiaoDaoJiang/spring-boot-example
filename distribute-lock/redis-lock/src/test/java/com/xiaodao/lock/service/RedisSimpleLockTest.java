package com.xiaodao.lock.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan("com.xiaodao.lock.service")
@DataRedisTest
public class RedisSimpleLockTest {

    @Autowired
    private RedisSimpleLock redisSimpleLock;

    @Test
    public void testLock() {
        redisSimpleLock.lock("testLock");
    }
}
