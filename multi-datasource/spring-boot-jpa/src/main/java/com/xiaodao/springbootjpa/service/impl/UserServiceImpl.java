package com.xiaodao.springbootjpa.service.impl;

import com.xiaodao.springbootjpa.dao.UserRepository;
import com.xiaodao.springbootjpa.model.User;
import com.xiaodao.springbootjpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserServiceImpl
 *
 * @author jianght
 * @date 2021/9/29
 */
@Service
public class UserServiceImpl implements UserService {


	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> findUserByAgeWithin(Integer maxAge) {
		return userRepository.findByAgeIsWithin(maxAge);
	}

	@Override
	public User saveUser(User user) {
		userRepository.save(user);
		return user;
	}
}
