package com.xiaodao.jimmerdemo.model;

import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Entity for table "book_store"
 */
@Entity
public interface BookStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    @Key
    @Column(name = "name")
    String name();

    @Nullable
    String website();


    /**
     * not a database column, but a relationship
     * mirror field is `store` in Book
     */
    @OneToMany(mappedBy = "store")
    List<Book> books();
}

