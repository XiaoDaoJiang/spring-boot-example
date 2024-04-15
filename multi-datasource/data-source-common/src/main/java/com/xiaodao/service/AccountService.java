package com.xiaodao.service;

import com.xiaodao.common.model.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaodao.common.model.User;

/**
* @author xiaodaojiang
* @description 针对表【account】的数据库操作Service
* @createDate 2023-07-12 11:24:47
*/
public interface AccountService extends IService<Account> {

    void createAccount(User user);

}
