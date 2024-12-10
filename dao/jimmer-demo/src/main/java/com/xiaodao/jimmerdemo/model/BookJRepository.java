package com.xiaodao.jimmerdemo.model;


import org.babyfish.jimmer.spring.repository.JRepository;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

public interface BookJRepository extends JRepository<Book, Long> {


    // spring data风格会需要一个 sqlClient 实例: No bean named 'sqlClient' available


    List<Book> findByNameOrderByEditionDesc(
            @Nullable String name
    );

    List<Book> findByPriceBetweenOrderByName(
            @Nullable BigDecimal minPrice,
            @Nullable BigDecimal maxPrice
    );

    long countByName(String name);
}