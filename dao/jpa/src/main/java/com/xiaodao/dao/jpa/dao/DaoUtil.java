package com.xiaodao.dao.jpa.dao;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class DaoUtil {
/*
    private final EntityManager entityManager;

    public DaoUtil(EntityManager entityManager) {
        this.entityManager = entityManager;
    } */

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void init(){
        System.out.println(entityManager);
        System.out.println("DaoUtil init");
    }
}
