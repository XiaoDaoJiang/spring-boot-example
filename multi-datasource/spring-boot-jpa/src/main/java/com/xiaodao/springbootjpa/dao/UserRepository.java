package com.xiaodao.springbootjpa.dao;

import com.xiaodao.common.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByAgeLessThan(Integer age);
}