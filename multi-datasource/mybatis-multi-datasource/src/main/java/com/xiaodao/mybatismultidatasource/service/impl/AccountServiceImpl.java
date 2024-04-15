package com.xiaodao.mybatismultidatasource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaodao.common.model.Account;
import com.xiaodao.common.model.User;
import com.xiaodao.service.AccountService;
import com.xiaodao.mybatismultidatasource.mapper.AccountMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

/**
* @author xiaodaojiang
* @description 针对表【account】的数据库操作Service实现
* @createDate 2023-07-12 11:24:47
*/
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
    implements AccountService{


    @Override
    public void createAccount(User user) {
        final Account account = new Account();
        account.setUserId(user.getId());
        account.setUserAccount(user.getName());
        account.setRegisterDate(Instant.now());
        this.baseMapper.insert(account);

    }
}




