package com.xiaodao.unittest.mapper;

import com.xiaodao.common.entity.User;

import java.util.List;

/**
 * @author jianghaitao
 * @Classname UserMapper
 * @Version 1.0.0
 * @Date 2024-08-16 09:57
 * @Created by jianghaitao
 */
public interface UserMapper {

    public List<User> selectAll();

}
