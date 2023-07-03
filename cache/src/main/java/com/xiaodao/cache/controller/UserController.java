package com.xiaodao.cache.controller;

import com.xiaodao.common.entity.User;
import com.xiaodao.common.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return userService.queryById(id);
    }


    @GetMapping("")
    public List<User> list() {
        return userService.list();
    }
}
