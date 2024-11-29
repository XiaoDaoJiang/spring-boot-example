package com.xiaodao.jimmerdemo.service;

import com.xiaodao.jimmerdemo.model.dto.BookDetailView;
import org.babyfish.jimmer.sql.JSqlClient;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jianghaitao
 * @Classname BookService
 * @Version 1.0.0
 * @Date 2024-11-29 14:57
 * @Created by jianghaitao
 */
@Service
public class BookService {

    @Autowired
    private JSqlClient sqlClient;


    @Transactional(transactionManager = "tm1")
    public @Nullable BookDetailView findById(long id) {
        return sqlClient.findById(BookDetailView.class,id);
    }

}
