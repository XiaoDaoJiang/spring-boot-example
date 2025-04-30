package com.xiaodao.graalvm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class GraalvmApplication {

    /* @Autowired
    private JSqlClient sqlClient; */

    public static void main(String[] args) {
        SpringApplication.run(GraalvmApplication.class, args);
    }

    /* @RequestMapping("/")
    List<BookDetailView> home() {
        final var execute = sqlClient.createQuery(Tables.BOOK_TABLE)
                .select(Tables.BOOK_TABLE.fetch(BookDetailView.class))
                .execute();

        return execute;
    } */

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

}
