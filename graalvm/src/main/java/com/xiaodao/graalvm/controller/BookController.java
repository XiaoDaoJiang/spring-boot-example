package com.xiaodao.graalvm.controller;

import com.xiaodao.graalvm.model.Tables;
import com.xiaodao.graalvm.model.dto.BookDetailView;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/* *
 * @author jianghaitao
 * @Classname BookController
 * @Version 1.0.0
 * @Date 2025-04-30 16:28
 * @Created by jianghaitao */


@RestController
public class BookController {

    @Autowired
    private JSqlClient sqlClient;


    @RequestMapping("/book")
    List<BookDetailView> book() {
        final var execute = sqlClient.createQuery(Tables.BOOK_TABLE)
                .select(Tables.BOOK_TABLE.fetch(BookDetailView.class))
                .execute();

        return execute;
    }

}
