package com.xiaodao.jimmerdemo.service;

import com.xiaodao.jimmerdemo.model.BookFetcher;
import com.xiaodao.jimmerdemo.model.BookTable;
import com.xiaodao.jimmerdemo.model.Tables;
import com.xiaodao.jimmerdemo.model.dto.BookDetailView;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.query.MutableRootQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author jianghaitao
 * @Classname BookServiceTest
 * @Version 1.0.0
 * @Date 2024-11-29 14:59
 * @Created by jianghaitao
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BookServiceTest {

    BookTable book = Tables.BOOK_TABLE;

    @Autowired
    private JSqlClient sqlClient;

    @Autowired
    private BookService bookService;

    @Test
    void findById() {
        final BookDetailView bookDetailView = bookService.findById(1);
        System.out.println(bookDetailView);
    }

    @Test
    void testFetcher() {
        final MutableRootQuery<BookTable> query = sqlClient.createQuery(book)
                .where(book.id().eq(1L));

        query.select(book.fetch(BookFetcher.$.allTableFields()))
                .execute()
                .forEach(System.out::println);


        query.select(book.fetch(BookFetcher.$.allScalarFields()))
                .execute()
                .forEach(System.out::println);

        query.select(book.fetch(BookFetcher.$.allReferenceFields()))
                .execute()
                .forEach(System.out::println);
    }
}