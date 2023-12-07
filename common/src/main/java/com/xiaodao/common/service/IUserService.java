package com.xiaodao.common.service;

import com.xiaodao.common.entity.User;

import java.util.List;

public interface IUserService {
    List<User> list();

    User save(User user);

    void del(Long id) ;

    int delAll() ;

    User update(User user);

    User queryById(Long id);

    User findByName(String name);
}
