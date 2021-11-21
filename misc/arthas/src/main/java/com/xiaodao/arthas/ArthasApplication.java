package com.xiaodao.arthas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class ArthasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArthasApplication.class, args);
    }


    @GetMapping("hello")
    public String greeting() {
        return "greet";
    }
}
