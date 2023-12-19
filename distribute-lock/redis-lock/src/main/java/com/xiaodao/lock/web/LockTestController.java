package com.xiaodao.lock.web;

import com.xiaodao.lock.service.RedisSimpleLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Slf4j
@RestController
public class LockTestController {

    @Autowired
    private RedisSimpleLock redisSimpleLock;


    @GetMapping("lock")
    public String lock(String res) {
        return redisSimpleLock.lock(res, Duration.ofSeconds(300));
    }
}
