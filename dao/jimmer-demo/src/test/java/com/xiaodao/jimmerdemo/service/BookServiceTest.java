package com.xiaodao.jimmerdemo.service;

import com.xiaodao.jimmerdemo.model.*;
import com.xiaodao.jimmerdemo.model.dto.BookDetailView;
import com.xiaodao.jimmerdemo.model.dto.BookStoreInput;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.mutation.AssociatedSaveMode;
import org.babyfish.jimmer.sql.ast.mutation.SaveMode;
import org.babyfish.jimmer.sql.ast.query.MutableRootQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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
    private JSqlClient sqlClient2;

    @Autowired
    private BookService bookService;
    @Autowired
    private JSqlClient sqlClient;


    /**
     * sqlClient1
     */
    @Test
    void findById() {
        final BookDetailView bookDetailView = bookService.findById(1);
        System.out.println(bookDetailView);
    }

    @Test
    void testFetcher() {
        final MutableRootQuery<BookTable> query = sqlClient2.createQuery(book)
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

    // @Transactional
    @Test
    void saveBookStore() {
        // List<BookStore> bookStores = List.of(
        //         Immutables.createBookStore(draft -> {
        //             draft.setName("书店1");
        //             draft.setWebsite("https://www.baidu.com");
        //             draft.setBooks(
        //                     List.of(
        //                             Immutables.createBook(draft2 -> {
        //                                 draft2.setName("书1").setEdition(1);
        //                                 draft2.setPrice(BigDecimal.valueOf(100));
        //                             }),
        //                             Immutables.createBook(draft2 -> {
        //                                 draft2.setName("书2").setEdition(2);
        //                                 draft2.setPrice(BigDecimal.valueOf(200));
        //                             })
        //                     )
        //             );
        //         }),
        //         Immutables.createBookStore(draft -> {
        //             draft.setName("书店2");
        //             draft.setWebsite("https://www.baidu.com/book");
        //             draft.setBooks(
        //                     List.of(
        //                             Immutables.createBook(draft2 -> {
        //                                 draft2.setName("书3").setEdition(3);
        //                                 draft2.setPrice(BigDecimal.valueOf(100));
        //                             }),
        //                             Immutables.createBook(draft2 -> {
        //                                 draft2.setName("书4").setEdition(4);
        //                                 draft2.setPrice(BigDecimal.valueOf(200));
        //                             })
        //                     )
        //             );
        //         })
        // );

        // sqlClient.getEntities().saveEntitiesCommand(bookStores)
        //         .setMode(SaveMode.INSERT_ONLY)
        //         .setAssociatedModeAll(AssociatedSaveMode.APPEND)
        //         .execute();

        final List<BookStoreInput> bookStoreInputList = List.of(
                new BookStoreInput.Builder().name("书店1").website("https://www.baidu.com")
                        .books(List.of(
                                new BookStoreInput.TargetOf_books.Builder().name("书1").edition(1).price(BigDecimal.valueOf(100)).build(),
                                new BookStoreInput.TargetOf_books.Builder().name("书2").edition(2).price(BigDecimal.valueOf(200)).build()
                        )).build(),
                new BookStoreInput.Builder().name("书店2").website("https://www.baidu.com/book")
                        .books(List.of(
                                new BookStoreInput.TargetOf_books.Builder().name("书3").edition(3).price(BigDecimal.valueOf(100)).build(),
                                new BookStoreInput.TargetOf_books.Builder().name("书4").edition(4).price(BigDecimal.valueOf(200)).build()
                        )).build()
        );

        sqlClient.getEntities().insertInputs(bookStoreInputList,AssociatedSaveMode.APPEND);


    }
}