package com.xiaodao.mybatismultidatasource.service;

import com.xiaodao.mybatismultidatasource.model.User;

import java.util.List;

/**
 * UserService
 *
 * @author jianght
 * @date 2021/9/29
 */
public interface UserService {

	List<User> findUserByAgeWithin(Byte maxAge);

	User saveUser(User user);
}
