package com.xiaodao.unittest.service;

import com.xiaodao.common.entity.User;
import com.xiaodao.common.service.impl.UserBaseService;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jianghaitao
 * @Classname TestUserServiceImpl
 * @Version 1.0.0
 * @Date 2024-04-28 13:56
 * @Created by jianghaitao
 */
@Service
public class TestUserServiceImpl extends UserBaseService implements BeanNameAware {

    private String beanName = "testUserService";

    @Autowired
    private UserHelper userHelper;

    @Override
    public User update(User user) {
        final User old = this.queryById(user.getId());
        this.save(user);
        return user;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }


    @Override
    public List<User> list() {
        return userHelper.getAll();
    }
}
