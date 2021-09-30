package com.xiaodao.mybatismultidatasource.service.impl;

import com.xiaodao.mybatismultidatasource.mapper.UserMapper;
import com.xiaodao.mybatismultidatasource.model.User;
import com.xiaodao.mybatismultidatasource.model.UserExample;
import com.xiaodao.mybatismultidatasource.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * UserServiceImpl
 *
 * @author jianght
 * @date 2021/9/29
 */
@Service
public class UserServiceImpl implements UserService {


	@Resource
	private UserMapper userMapper;

	@Override
	public List<User> findUserByAgeWithin(Byte maxAge) {
		UserExample userExample = new UserExample();
		userExample.createCriteria().andAgeBetween((byte) 0, maxAge);
		return userMapper.selectByExample(userExample);
	}

	@Override
	public User saveUser(User user) {
		int i = userMapper.insertSelective(user);
		return user;
	}
}
