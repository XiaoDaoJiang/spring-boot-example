package com.xiaodao.p6spy.service;

import com.xiaodao.p6spy.model.User;
import com.xiaodao.p6spy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    public List<User> all() {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List findByName(String name) {
        return entityManager.createNativeQuery("select * from user where name = ?1", User.class)
                .setParameter(1, name)
                .getResultList();
    }
}
