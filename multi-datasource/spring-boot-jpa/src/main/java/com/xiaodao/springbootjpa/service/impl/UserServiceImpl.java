package com.xiaodao.springbootjpa.service.impl;

import com.xiaodao.common.model.User;
import com.xiaodao.service.UserService;
import com.xiaodao.springbootjpa.dao.UserRepository;
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


	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public List<User> findUserByAgeWithin(Integer maxAge) {
		return userRepository.findByAgeLessThan(maxAge);
	}

	@Override
	public User saveUser(User user) {
		userRepository.save(user);
		return user;
	}
}
