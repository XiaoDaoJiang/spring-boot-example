package com.xiaodao.dao.jpa.service;

import cn.hutool.core.lang.Assert;
import com.xiaodao.dao.jpa.entity.User;
import com.xiaodao.dao.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    @Qualifier("entityManagerPrimary")
    private EntityManager entityManager2;

    @Autowired
    private EntityManager entityManager3;


    @Transactional
    public User createUser(User user) {
        final User save = userRepository.save(user);
        System.out.println(entityManager);
        final User queryByEMT = entityManager2.find(User.class, save.getId());

        Assert.equals(save, queryByEMT);

        return save;


    }

}
