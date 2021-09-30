package com.xiaodao.springbootjpa.dao;

import com.xiaodao.springbootjpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByAgeIsWithin(Integer age);
}