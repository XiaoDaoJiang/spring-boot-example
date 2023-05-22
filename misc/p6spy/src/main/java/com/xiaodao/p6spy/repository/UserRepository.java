package com.xiaodao.p6spy.repository;

import com.xiaodao.p6spy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}