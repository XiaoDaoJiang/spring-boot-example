package com.xiaodao.scheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class CronExpressionUtils {

    public static String getNextCronExpression(LocalDateTime lastTime, long interval) {
        // 计算下次执行时间点
        LocalDateTime nextTime = lastTime.plusDays(interval);
        LocalDateTime now = LocalDateTime.now();

        while (nextTime.isBefore(now)) {
            nextTime = nextTime.plusDays(interval);
        }

        // 将下次执行时间点转换为 cron 表达式
        String cron = String.format("0 %d %d ? %d %d *", nextTime.getMinute(), nextTime.getHour(), nextTime.getDayOfMonth(), nextTime.getMonthValue());
        return cron;
    }

    public static void main(String[] args) {
        // 假设上次执行时间为 2023-03-15 10:00:00，下次执行间隔为 20 天
        LocalDateTime lastTime = LocalDateTime.of(2023, 3, 15, 10, 0, 0);
        long interval = 20;

        String cron = getNextCronExpression(lastTime, interval);
        System.out.println(cron); // 输出：
    }

}
