package com.xiaodao.springbootjpa.service.impl;

import com.xiaodao.springbootjpa.annotation.DS;
import com.xiaodao.springbootjpa.dao.AccountRepository;
import com.xiaodao.springbootjpa.model.Account;
import com.xiaodao.springbootjpa.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        int i = 1 / 0;
        accountRepository.save(account);
        return account;
    }

    @Override
    public boolean checkExist(String username) {
        return accountRepository.findByUsername(username).isPresent();
    }
}
