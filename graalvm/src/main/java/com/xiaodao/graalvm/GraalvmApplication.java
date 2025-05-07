package com.xiaodao.graalvm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class GraalvmApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraalvmApplication.class, args);
    }


    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

}
