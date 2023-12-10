package com.xiaodao.springbootjpa.service;

import com.xiaodao.springbootjpa.model.Account;

import java.util.List;

public interface AccountService {

    List<Account> findAll();

    Account create(String username);

    boolean checkExist(String username);
}
