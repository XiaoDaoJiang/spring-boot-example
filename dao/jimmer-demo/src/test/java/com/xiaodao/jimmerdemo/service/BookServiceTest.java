package com.xiaodao.jimmerdemo.service;

import com.xiaodao.jimmerdemo.model.*;
import com.xiaodao.jimmerdemo.model.dto.BookDetailView;
import com.xiaodao.jimmerdemo.model.dto.BookInput;
import com.xiaodao.jimmerdemo.model.dto.BookStoreInput;
import com.xiaodao.jimmerdemo.model.dto.BookTestView;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.mutation.AssociatedSaveMode;
import org.babyfish.jimmer.sql.ast.mutation.BatchSaveResult;
import org.babyfish.jimmer.sql.ast.mutation.SaveMode;
import org.babyfish.jimmer.sql.ast.mutation.SimpleSaveResult;
import org.babyfish.jimmer.sql.ast.query.MutableRootQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
                                new BookStoreInput.TargetOf_books.Builder().name("书1").edition("1").price(BigDecimal.valueOf(100)).build(),
                                new BookStoreInput.TargetOf_books.Builder().name("书2").edition(String.valueOf(2)).price(BigDecimal.valueOf(200)).build()
                        )).build(),
                new BookStoreInput.Builder().name("书店2").website("https://www.baidu.com/book")
                        .books(List.of(
                                new BookStoreInput.TargetOf_books.Builder().name("书3").edition(String.valueOf(3)).price(BigDecimal.valueOf(100)).build(),
                                new BookStoreInput.TargetOf_books.Builder().name("书4").edition(String.valueOf(4)).price(BigDecimal.valueOf(200)).build()
                        )).build()
        );

        sqlClient.getEntities().insertInputs(bookStoreInputList, AssociatedSaveMode.APPEND);


    }


    @Test
    void saveBookAuthorManyToMany() {
        List<Author> authors = List.of(
                Immutables.createAuthor(draft -> {
                    draft.setFirstName("author c");
                    draft.setLastName("author C");
                    draft.setGender(Gender.FEMALE);
                    draft.setCreatedTime(LocalDateTime.now());
                    draft.setModifiedTime(LocalDateTime.now());
                    draft.setBooks(List.of(
                            Immutables.createBook(bookDraft -> {
                                bookDraft.setName("book1");
                                bookDraft.setEdition(String.valueOf(1));
                                bookDraft.setPrice(BigDecimal.valueOf(100));
                            })
                    ));
                }),
                Immutables.createAuthor(draft -> {
                    draft.setFirstName("author d");
                    draft.setLastName("author D");
                    draft.setGender(Gender.MALE);
                    draft.setCreatedTime(LocalDateTime.now());
                    draft.setModifiedTime(LocalDateTime.now());
                    draft.setBooks(List.of(
                            Immutables.createBook(bookDraft -> {
                                bookDraft.setName("book2");
                                bookDraft.setEdition(String.valueOf(1));
                                bookDraft.setPrice(BigDecimal.valueOf(100));
                            })
                    ));
                })
        );

        final BatchSaveResult<Author> authorBatchSaveResult = sqlClient.getEntities().saveEntitiesCommand(authors)
                .setMode(SaveMode.INSERT_ONLY)
                .setAssociatedModeAll(AssociatedSaveMode.MERGE)
                .setKeyProps(BookProps.NAME, BookProps.EDITION)
                .execute();

        System.out.println(authorBatchSaveResult.getAffectedRowCountMap());
    }

    @Test
    void testFetcherByDTOConfig() {
        final List<BookTestView> bookTestViews = sqlClient.createQuery(Tables.BOOK_TABLE)
                .select(Tables.BOOK_TABLE.fetch(BookTestView.class))
                .execute();

        bookTestViews.forEach(System.out::println);
    }

    @Test
    void testFetcherChild() {
        final List<Book> bookTestViews = sqlClient.createQuery(Tables.BOOK_TABLE)
                .select(Tables.BOOK_TABLE.fetch(Fetchers.BOOK_FETCHER.name()
                        .comments(Fetchers.BOOK_COMMENT_FETCHER.bookId().content())))
                .execute();

        bookTestViews.forEach(System.out::println);
    }


    @Test
    void testSaveBookInputWithStoreId() {
        BookInput bookInput = new BookInput.Builder()
                .name("book_store_id")
                .edition(String.valueOf(1))
                .price(BigDecimal.valueOf(100))
                .storeId(1L)
                .build();
        final SimpleSaveResult<Book> save = sqlClient.save(bookInput);
        System.out.println(save.getAffectedRowCountMap());
    }

    @Test
    void testSameColumnToDiffEntity() {
        sqlClient.createQuery(Tables.BOOK_TABLE)
                .select(Tables.BOOK_TABLE.fetch(BookDetailView.class))
                .execute()
                .forEach(System.out::println);
    }

    @Test
    void testJoinSql() {
        sqlClient.createQuery(Tables.AUTHOR_TABLE)
                .select(Tables.AUTHOR_TABLE.fetch(
                        Fetchers.AUTHOR_FETCHER.allScalarFields().comments()
                )).forEach(System.out::println);


        final SimpleSaveResult<Author> authorSimpleSaveResult = sqlClient.save(AuthorDraft.$.produce(d -> {
            d.setFirstName("author e");
            d.setLastName("author E");
            d.setGender(Gender.FEMALE);
            d.setCreatedTime(LocalDateTime.now());
            d.setModifiedTime(LocalDateTime.now());
            // 无法保存 @JoinSql
            // d.setComments(List.of(
            //         Immutables.createBookComment(draft -> {
            //             draft.setContent("book comment 1");
            //         }),
            //         Immutables.createBookComment(draft -> {
            //             draft.setContent("book comment 2");
            //         })
            // ));
        }));

        System.out.println(authorSimpleSaveResult.getAffectedRowCountMap());
    }

    @Test
    void testSaveCommandWhenExistAssociation() {
        Book book1 = BookDraft.$.produce(d -> {
            d.setName("book exist store");
            d.setEdition("2");
            d.setPrice(BigDecimal.valueOf(100));
            // d.setPublishYear(2025);
            d.setStore(BookStoreDraft.$.produce(bd -> {
                bd.setName("bookstore 1");
                bd.setWebsite("https://www.baidu.com");
            }));
        });

        final SimpleSaveResult<Book> saveResult = sqlClient.saveCommand(book1)
                .setAssociatedModeAll(AssociatedSaveMode.APPEND)
                .setAssociatedMode(BookProps.STORE, AssociatedSaveMode.APPEND_IF_ABSENT)
                .setKeyOnlyAsReference(BookProps.STORE)
                .setKeyProps(BookStoreProps.NAME, BookStoreProps.WEBSITE)
                .execute();
        System.out.println(saveResult.getAffectedRowCountMap());
    }

    /* @Test
    void testSaveCommandInsertAbsent() {
        Book book1 = BookDraft.$.produce(d->{
            d.setName("book exist store");
            d.setEdition("2");
            d.setPrice(BigDecimal.valueOf(100));
            d.setPublishYear(2025);
        });

        final SimpleSaveResult<Book> saveResult = sqlClient.saveCommand(book1)
                .setMode(SaveMode.INSERT_IF_ABSENT)
                .execute();
        System.out.println(saveResult.isModified());
    } */


    @Test
    void testSaveEmbedded() {
        Book book1 = BookDraft.$.produce(d -> {
            d.setName("book exist store");
            d.setEdition("202502");
            d.setPrice(BigDecimal.valueOf(100));
            d.setBookPublisher(BookPublisherDraft.$.produce(bd -> {
                bd.setPublisherEdition(2);
                bd.setPublishYear("2025");
            }));
            // d.setPublishYear(2025);
        });

        final SimpleSaveResult<Book> saveResult = sqlClient.saveCommand(book1)
                .setMode(SaveMode.INSERT_IF_ABSENT)
                .execute();
        System.out.println(saveResult.isModified());
    }


    @Test
    void testSaveEmbeddedInput() {
        final BookInput bookInput = new BookInput.Builder().name("book exist store")
                .edition("202503")
                .price(BigDecimal.valueOf(100))
                .bookPublisher(Immutables.createBookPublisher(d -> {
                    d.setPublisherEdition(3);
                    d.setPublishYear("2025");
                }))
                .build();

        System.out.println(bookInput.getBookPublisher().curPublisherEdition());

        final SimpleSaveResult<Book> saveResult = sqlClient.saveCommand(bookInput)
                .setMode(SaveMode.INSERT_IF_ABSENT)
                .execute();
        System.out.println(saveResult.isModified());
    }


}