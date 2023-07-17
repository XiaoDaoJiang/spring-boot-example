package com.xiaodao;

import com.xiaodao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@EnableAsync
@SpringBootApplication
public class AsyncExecutorApplication {


    @Autowired
    private UserService userService;


    public static void main(String[] args) {
        SpringApplication.run(AsyncExecutorApplication.class, args);
    }



   /* @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(Thread.currentThread().getName() + " start to run async process");
        userService.process();
        System.out.println(Thread.currentThread().getName() + " call async return");
    }*/

    @RequestMapping("/hello")
    public String hello() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " start to run async process");
        userService.process();
        System.out.println(Thread.currentThread().getName() + " call async return");
        return "hello";
    }


}
