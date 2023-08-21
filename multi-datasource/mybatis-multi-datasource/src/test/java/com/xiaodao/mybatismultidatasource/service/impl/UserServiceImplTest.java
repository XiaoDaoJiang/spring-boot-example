package com.xiaodao.mybatismultidatasource.service.impl;

import com.xiaodao.common.model.User;
import com.xiaodao.service.AccountService;
import com.xiaodao.service.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Repeat;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;


    @RepeatedTest(10)
    public void testCreateUser() {

        final User user = new User();
        user.setName("test");
        user.setAge(34);
        user.setEmail("x");

        if (!userService.checkUserExist(user)) {
            userService.saveUser(user);
            accountService.createAccount(user);
        }


    }

}