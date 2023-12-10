package com.xiaodao.translation.controller;

import com.xiaodao.translation.core.annotation.DicField;
import com.xiaodao.translation.core.annotation.EnumDicField;
import com.xiaodao.translation.core.annotation.Translation;
import com.xiaodao.translation.core.translator.DicEnum;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Data
    public static class User {
        private String name;
        @EnumDicField(dicEnum = DicEnum.SexDicEnum.class,dicField = @DicField(defaultVal = "未知"))
        private String sex;

        private String sexName;
    }

    @Translation
    @RequestMapping
    public User getUser() {
        User user = new User();
        user.setName("张三");
        user.setSex("1");
        return user;
    }

}
