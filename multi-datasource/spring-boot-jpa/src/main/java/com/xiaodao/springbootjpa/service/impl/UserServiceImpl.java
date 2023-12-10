package com.xiaodao.springbootjpa.service.impl;

import com.xiaodao.springbootjpa.annotation.DS;
import com.xiaodao.springbootjpa.dao.UserRepository;
import com.xiaodao.springbootjpa.model.User;
import com.xiaodao.springbootjpa.service.AccountService;
import com.xiaodao.springbootjpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UserServiceImpl
 *
 * @author jianght
 * @date 2021/9/29
 */
@DS
@Service
public class UserServiceImpl implements UserService {


	private final UserRepository userRepository;

	@Autowired
	private AccountService accountService;

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

	@Override
	public boolean checkUserExist(User user) {
		return false;
	}

	@Transactional
	@Override
	public User register(String username, Integer age) {
		final User user = new User();
		user.setName(username);
		user.setAge(age);

		if (accountService.checkExist(username)) {
			throw new RuntimeException("账户已存在");
		}

		userRepository.save(user);
		accountService.create(username);

		return user;
	}
}
