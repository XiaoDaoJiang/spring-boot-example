package com.xiaodao.mybatismultidatasource.service.impl;

import com.xiaodao.common.model.User;
import com.xiaodao.common.model.UserExample;
import com.xiaodao.mybatismultidatasource.mapper.UserMapper;
import com.xiaodao.service.UserService;
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
    public List<User> findUserByAgeWithin(Integer maxAge) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAgeBetween((byte) 0, maxAge.byteValue());
        return userMapper.selectByExample(userExample);
    }

    @Override
    public User saveUser(User user) {
        int i = userMapper.insertSelective(user);
        return user;
    }

    @Override
    public boolean checkUserExist(User user) {
        if (user.getId() != null) {
            return userMapper.selectByPrimaryKey(user.getId()) != null;
        } else {
            final UserExample example = new UserExample();
            example.createCriteria().andNameEqualTo(user.getName());
            return userMapper.selectByExample(example) != null;
        }
    }
}
