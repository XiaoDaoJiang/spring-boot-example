package com.xiaodao.jimmerdemo;

import com.xiaodao.jimmerdemo.model.*;
import com.xiaodao.jimmerdemo.model.AuthorFetcher;
import com.xiaodao.jimmerdemo.model.BookFetcher;
import com.xiaodao.jimmerdemo.model.BookStoreFetcher;
import com.xiaodao.jimmerdemo.model.repository.BookRepository;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.sql.fetcher.Fetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class JimmerDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JimmerDemoApplication.class, args);
    }

    @RestController
    static class HelloController {

        @Autowired
        private BookRepository bookRepository;

        @RequestMapping("/hello")
        public HttpEntity<Object> hello() {
            int pageIndex = 0;
            int pageSize = 10;
            Fetcher<Book> fetcher = BookFetcher.$.allScalarFields()
                    .store(BookStoreFetcher.$.allScalarFields())
                    .authors(AuthorFetcher.$.allScalarFields());
            String sortCode = "";
            String name = null;
            BigDecimal minPrice = null;
            BigDecimal maxPrice = null;
            String storeName = null;
            String storeWebsite = null;
            String authorName = null;
            Gender authorGender = null;
            final Page<Book> books = bookRepository.findBooks(pageIndex, pageSize, fetcher, sortCode, name, minPrice, maxPrice, storeName, storeWebsite, authorName, authorGender);
            final List<Book> rows = books.getRows();
            for (Book book : rows) {
                book.price();
            }
            return new HttpEntity<>(books);
        }
    }
}
