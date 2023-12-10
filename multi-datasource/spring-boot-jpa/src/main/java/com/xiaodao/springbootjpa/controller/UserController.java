package com.xiaodao.springbootjpa.controller;

import com.xiaodao.springbootjpa.model.User;
import com.xiaodao.springbootjpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController
 *
 * @author jianght
 * @date 2021/9/30
 */
@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping
	public List<User> getUser(@RequestParam(required = false, defaultValue = "100") Integer maxAge) {
		return userService.findUserByAgeWithin(maxAge);
	}

	@PostMapping
	public User saveUser(User user) {
		return userService.saveUser(user);
	}

	@PutMapping
	public User register(String username,Integer age) {
		return userService.register(username, age);
	}
}
