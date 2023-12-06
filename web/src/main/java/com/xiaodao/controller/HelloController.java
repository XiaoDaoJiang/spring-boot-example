package com.xiaodao.controller;

import com.xiaodao.CommonResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("hello")
    public String hello() {
        return "hello world";
    }

    @RequestMapping("get")
    private CommonResult<HelloController.User> testGet(@RequestBody User user) {
        log.info("hello get：{}", user);
        return CommonResult.success(user);
    }

    @Data
    static class User {
        private String name;
    }
}
