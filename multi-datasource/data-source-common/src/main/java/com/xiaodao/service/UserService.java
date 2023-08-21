package com.xiaodao.service;


import com.xiaodao.common.model.User;

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

    default boolean checkUserExist(User user) {
        return true;
    }
}
