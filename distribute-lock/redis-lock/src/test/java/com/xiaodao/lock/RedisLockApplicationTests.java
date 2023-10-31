package com.xiaodao.lock;

import com.xiaodao.lock.service.RedisSimpleLock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
// @SpringBootTest(classes = RedisLockApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
// @DataRedisTest
public class RedisLockApplicationTests {

    @Autowired
    private RedisSimpleLock redisSimpleLock;

    @Test
    public void testLock() {
        redisSimpleLock.lock("testLock");
    }
}
