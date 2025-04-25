package com.xiaodao.jimmerdemo.model;

import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

@Entity
public interface Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    @Key
    String name();

    @Key
    String edition();

    BigDecimal price();


    /**
     * `store` map to STORE_ID
     * otherwise use JoinColumn to map to STORE_ID
     */
    @ManyToOne
    @Nullable
    BookStore store();

    @IdView
    Long storeId();

    @ManyToMany
    @JoinTable(
            name = "BOOK_AUTHOR_MAPPING",
            joinColumnName = "BOOK_ID",
            inverseJoinColumnName = "AUTHOR_ID"
    )
    List<Author> authors();

    @IdView("authors")
    List<Long> authorIds();

/*     @Column(name = "publish_year")
    Integer publishYear(); */

    @OneToMany(mappedBy = "book")
    List<BookComment> comments();

/*     @Transient(CustomerDataResolver.class)
    BookPrice priceInfo(); */

    @PropOverride(prop = "publisherEdition", columnName ="publisher_edition")
    BookPublisher bookPublisher();

}