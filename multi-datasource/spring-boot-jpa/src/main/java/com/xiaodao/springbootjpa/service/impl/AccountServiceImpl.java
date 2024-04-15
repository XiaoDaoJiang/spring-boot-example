package com.xiaodao.springbootjpa.service.impl;

import com.xiaodao.springbootjpa.annotation.DS;
import com.xiaodao.springbootjpa.dao.AccountRepository;
import com.xiaodao.springbootjpa.model.Account;
import com.xiaodao.springbootjpa.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DS("slave1")
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account create(String username) {
        final Account account = new Account();
        account.setUsername(username);
        // int i = 1 / 0;
        accountRepository.save(account);
        return account;
    }

    @Override
    // 默认的事务传播行为是 Propagation.REQUIRED，即如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。
    // 默认的隔离级别是 Isolation.DEFAULT，表示使用底层数据库的默认隔离级别。
    // @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)

    // 新建一个独立的事务运行，如果当前存在事务，则把当前事务挂起。当前事务运行后，挂起的事务继续执行。
    @Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.DEFAULT)
    // @Transactional(propagation = Propagation.NESTED,isolation = Isolation.DEFAULT)
    public boolean checkExist(String username) {
        return accountRepository.existsByUsername(username);
    }
}
