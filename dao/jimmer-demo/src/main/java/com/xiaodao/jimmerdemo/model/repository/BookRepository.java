package com.xiaodao.jimmerdemo.model.repository;

import com.xiaodao.jimmerdemo.model.Book;
import com.xiaodao.jimmerdemo.model.BookFetcher;
import com.xiaodao.jimmerdemo.model.BookTable;
import com.xiaodao.jimmerdemo.model.Gender;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.babyfish.jimmer.sql.ast.query.Order;
import org.babyfish.jimmer.sql.fetcher.Fetcher;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BookRepository {

    private static final BookTable T = BookTable.$;

    @Autowired
    private JSqlClient sqlClient1;

    public Page<Book> findBooks(

            int pageIndex, // 从0开始
            int pageSize,

            @Nullable Fetcher<Book> fetcher,

            // 例如: "name asc, edition desc"
            @Nullable String sortCode,

            @Nullable String name,
            @Nullable BigDecimal minPrice,
            @Nullable BigDecimal maxPrice,
            @Nullable String storeName,
            @Nullable String storeWebsite,
            @Nullable String authorName,
            @Nullable Gender authorGender
    ) {
        return sqlClient1
                .createQuery(T)
                .where(T.name().ilikeIf(name))
                .where(T.price().betweenIf(minPrice, maxPrice))
                .where(T.store().name().ilikeIf(storeName))
                .where(T.store().website().ilikeIf(storeWebsite))
                .where(
                        T.authors(author ->
                                Predicate.or(
                                        author.firstName().ilikeIf(authorName),
                                        author.lastName().ilikeIf(authorName)
                                )
                        )
                )
                .where(T.authors(author -> author.gender().eqIf(authorGender)))
                .orderBy(
                        Order.makeOrders(
                                T,
                                sortCode != null ?
                                        sortCode :
                                        "name asc, edition desc"
                        )
                )
                .select(
                        T.fetch(fetcher)
                )
                .fetchPage(pageIndex, pageSize);
    }
}