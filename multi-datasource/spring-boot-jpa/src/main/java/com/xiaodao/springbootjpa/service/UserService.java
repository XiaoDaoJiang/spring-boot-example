package com.xiaodao.springbootjpa.service;


import com.xiaodao.springbootjpa.model.User;

import java.util.List;

/**
 * UserService
 *
 * @author jianght
 * @date 2021/9/29
 */
public interface UserService {

    List<User> findUserByAgeWithin(Integer maxAge);

    User saveUser(User user);

    boolean checkUserExist(User user);

    User register(String username, Integer age);
}
