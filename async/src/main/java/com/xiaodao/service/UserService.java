package com.xiaodao.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.*;


@Service
public class UserService {

    final ExecutorService executorService = new ThreadPoolExecutor(2, 2,
            0L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2));


    //@Async
    public void process() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " start to process");
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            try {
                executorService.execute(() -> {
                    try {
                        Thread.sleep(5000);
                        System.out.println(Thread.currentThread().getName() + " process ");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        countDownLatch.countDown();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();

            }
        }
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + " end to process");

    }
}
