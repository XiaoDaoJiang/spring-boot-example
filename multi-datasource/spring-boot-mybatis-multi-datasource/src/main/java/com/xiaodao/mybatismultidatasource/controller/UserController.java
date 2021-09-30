package com.xiaodao.mybatismultidatasource.controller;

import com.xiaodao.mybatismultidatasource.model.User;
import com.xiaodao.mybatismultidatasource.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController
 *
 * @author jianght
 * @date 2021/9/29
 */

@RestController
@RequestMapping("user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public List<User> getUserById(@RequestParam(required = false, defaultValue = "1") Byte age) {
		return userService.findUserByAgeWithin(age);
	}

	@PostMapping
	public User createUser(@RequestBody User user) {
		return userService.saveUser(user);
	}
}
