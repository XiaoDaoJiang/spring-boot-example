package com.xiaodao.controller;

import com.xiaodao.CommonResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("hello")
    public CommonResult<String> hello() {
        return CommonResult.success("hello world");
    }


    @RequestMapping("helloForm")
    public CommonResult<User> helloForm(User user) {
        return CommonResult.success(user);
    }

    @RequestMapping("get")
    private CommonResult<HelloController.User> testGet(@RequestBody User user) {
        log.info("hello get：{}", user);
        return CommonResult.success(user);
    }

    @PostMapping("map")
    private CommonResult<HelloController.User> testMap(@RequestBody User user) {
        log.info("hello map：{}", user);
        return CommonResult.success(user);
    }

    @Data
    public static class User {
        private String name;

        private Map<String,Object> properties;
    }
}
