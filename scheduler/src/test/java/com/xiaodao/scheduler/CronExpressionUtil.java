package com.xiaodao.scheduler;

import org.springframework.scheduling.support.CronExpression;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CronExpressionUtil {
	public static void main(String[] args) {
		String cronExpressionStr = "0 0/15 * * * ?";
		Date date = new Date();
		CronExpression cronExpression = CronExpression.parse(cronExpressionStr);
		ZoneId zone = ZoneId.systemDefault();

		ZonedDateTime dateTime = ZonedDateTime.ofInstant(date.toInstant(), zone);

		for (int i = 0; i < 10; i++) {
			dateTime = cronExpression.next(dateTime);
			System.out.println(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		}
	}
}
