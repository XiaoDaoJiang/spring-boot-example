package com.xiaodao.dao.jpa.repository;

import com.xiaodao.dao.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}