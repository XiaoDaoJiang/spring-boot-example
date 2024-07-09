package com.xiaodao.integratetest.service;

import com.xiaodao.common.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jianghaitao
 * @Classname UserHelper
 * @Version 1.0.0
 * @Date 2024-04-28 17:03
 * @Created by jianghaitao
 */

@Service
public class UserHelper {

    private final Map<String, User> userMap = new HashMap<>();

    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }
}
