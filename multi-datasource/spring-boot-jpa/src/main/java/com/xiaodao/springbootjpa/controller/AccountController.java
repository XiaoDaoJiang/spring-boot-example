package com.xiaodao.springbootjpa.controller;

import com.xiaodao.springbootjpa.model.Account;
import com.xiaodao.springbootjpa.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UserController
 *
 * @author jianght
 * @date 2021/9/30
 */
@RestController
@RequestMapping("account")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@GetMapping
	public List<Account> list() {
		return accountService.findAll();
	}


}
