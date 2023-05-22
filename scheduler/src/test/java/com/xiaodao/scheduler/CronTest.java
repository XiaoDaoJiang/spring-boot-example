package com.xiaodao.scheduler;

import org.springframework.scheduling.support.CronExpression;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CronTest {

	/**
	 * @param lastExecutionDate 上次执行时间
	 * @param interval          执行间隔 单位：天
	 *                          <p>
	 *                          0 0 0 19/1 * ?
	 */
	public static String getCronForDelayLastExecution(String initCron, LocalDate lastExecutionDate, long interval) {
		final CronExpression ce = CronExpression.parse(initCron);
		String[] cronArr = initCron.split(" ");

		final int day = lastExecutionDate.getDayOfMonth();
		String dayStr = 1 + "/" + interval;
		cronArr[3] = dayStr;

		return String.join(" ", cronArr);
	}

	public static String getLastExecution(String initCron) {
		Date date = new Date();
		final LocalDate startMonthDayDate = LocalDate.now().withDayOfMonth(1);
		CronExpression cronExpression = CronExpression.parse(initCron);
		ZoneId zone = ZoneId.systemDefault();
		ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.from(startMonthDayDate), zone);

		dateTime = cronExpression.next(dateTime);
		System.out.println(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		return null;
	}

	public static void main(String[] args) {
		final LocalDate lastExecutionDate = LocalDate.now().plusDays(3);
		final String newCronStr = getCronForDelayLastExecution("0 0 0 1,15 * ?", lastExecutionDate, 4);
		System.out.printf("new cron = %s \n", newCronStr);

		CronExpression cronExpression = CronExpression.parse(newCronStr);
		ZoneId zone = ZoneId.systemDefault();

		ZonedDateTime dateTime = ZonedDateTime.ofInstant(lastExecutionDate.atStartOfDay().atZone(zone).toInstant(), zone);

		for (int i = 0; i < 100; i++) {
			dateTime = cronExpression.next(dateTime);
			System.out.println(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		}

	}


}
