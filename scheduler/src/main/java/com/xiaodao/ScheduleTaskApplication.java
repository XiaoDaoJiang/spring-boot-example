package com.xiaodao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 */
@EnableScheduling
@SpringBootApplication
public class ScheduleTaskApplication /*implements SchedulingConfigurer*/ {

    Logger logger = LoggerFactory.getLogger(ScheduleTaskApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ScheduleTaskApplication.class, args);
    }

    /*@Bean
    public Executor taskExecutor() {
        return new ScheduledThreadPoolExecutor(5, new CustomizableThreadFactory("myScheduled-pool-"));
    }
*/
   /* @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // taskRegistrar.setScheduler(taskExecutor());
    }*/

    // @Scheduled(cron = "0/1 * * * * ?")
    public void testCron() throws InterruptedException {
        logger.info(Thread.currentThread().getName() + " start do something");
        Thread.sleep(1000 * 10);
        logger.info(Thread.currentThread().getName() + " done ");

    }


    // @Scheduled(fixedDelay = 1000)
    public void testFixedDelay() throws InterruptedException {
        logger.info(Thread.currentThread().getName() + " start do something");
        Thread.sleep(1000 * 10);
        logger.info(Thread.currentThread().getName() + " done ");

    }

    // @Scheduled(fixedRate = 1000)
    public void testFixedRate() throws InterruptedException {
        logger.info(Thread.currentThread().getName() + " start do something");
        Thread.sleep(1000 * 10);
        logger.info(Thread.currentThread().getName() + " done ");

    }


}
