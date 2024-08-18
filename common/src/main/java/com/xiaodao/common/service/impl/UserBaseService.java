package com.xiaodao.common.service.impl;

import com.xiaodao.common.entity.User;
import com.xiaodao.common.respository.UserRepository;
import com.xiaodao.common.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author jianghaitao
 * @Classname UserBaseService
 * @Version 1.0.0
 * @Date 2024-04-28 13:52
 * @Created by jianghaitao
 */
@Service
public class UserBaseService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> list() {
        return Collections.emptyList();
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void del(Long id) {

    }

    @Override
    public int delAll() {
        return 0;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User queryById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByName(String name) {
        return null;
    }
}
