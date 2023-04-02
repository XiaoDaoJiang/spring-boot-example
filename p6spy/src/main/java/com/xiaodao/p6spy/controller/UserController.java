package com.xiaodao.p6spy.controller;

import com.xiaodao.p6spy.model.User;
import com.xiaodao.p6spy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<User> all() {
        return userService.all();
    }

    @GetMapping("/test")
    public User findByEmail(String email) {
        return userService.findByEmail(email);
    }

    @GetMapping("/test2")
    public List findByName(String name) {
        return userService.findByName(name);
    }

}
