package com.xiaodao.jimmerdemo.model;

import org.babyfish.jimmer.sql.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public interface Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();

    @Key
    String firstName();

    @Key
    String lastName();

    /*
     * 这里，Gender是一个枚举，代码稍后给出
     */
    Gender gender();

    @ManyToMany(mappedBy = "authors")
    List<Book> books();

    LocalDateTime createdTime();

    LocalDateTime modifiedTime();


    @JoinSql("%alias.id = %target_alias.book_id")
    @ManyToMany
    List<BookComment> comments();

}