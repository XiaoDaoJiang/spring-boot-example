package com.xiaodao.jimmerdemo.model;

import org.babyfish.jimmer.sql.*;
import org.jetbrains.annotations.Nullable;


/**
 * <p>
 * 书评
 *
 * </p>
 *
 * @author jianghaitao
 * @date 2025-04-11
 */
@Entity
@Table(name = "book_comment")
public interface BookComment {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id();

    /**
     * 评论内容
     */
    @Nullable
    String content();

    @ManyToOne
    @JoinColumn(name = "book_id",foreignKeyType = ForeignKeyType.FAKE)
    @Nullable
    Book book();
    /**
     * 关联书id
     */
    @IdView("book")
    Long bookId();

    /* @ManyToOne
    @JoinColumn(name = "book_id",foreignKeyType = ForeignKeyType.FAKE)
    @Nullable
    Author author();

     *//**
     * 关联书id
     *//*
    @IdView("author")
    Long authorId(); */


}
