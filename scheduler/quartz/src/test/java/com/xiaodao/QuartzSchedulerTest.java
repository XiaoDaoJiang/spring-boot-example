package com.xiaodao;

import com.xiaodao.QuartzScheduleApplication;
import com.xiaodao.quartz.job.DemoJob01;
import com.xiaodao.quartz.job.DemoJob02;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Scheduler 手动设置任务
 */
@SpringBootTest(classes = QuartzScheduleApplication.class)
public class QuartzSchedulerTest {

	@Autowired
	private Scheduler scheduler;

	@Test
	public void addDemoJob01Config() throws SchedulerException {
		// 创建 JobDetail
		JobDetail jobDetail = JobBuilder.newJob(DemoJob01.class)
				.withIdentity("demoJob01") // 名字为 demoJob01
				.storeDurably() // 没有 Trigger 关联的时候任务是否被保留。因为创建 JobDetail 时，还没 Trigger 指向它，所以需要设置为 true ，表示保留。
				.build();
		// 创建 Trigger
		SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInSeconds(5) // 频率。
				.repeatForever(); // 次数。
		Trigger trigger = TriggerBuilder.newTrigger()
				.forJob(jobDetail) // 对应 Job 为 demoJob01
				.withIdentity("demoJob01Trigger") // 名字为 demoJob01Trigger
				.withSchedule(scheduleBuilder) // 对应 Schedule 为 scheduleBuilder
				.build();
		// 添加调度任务
		scheduler.scheduleJob(jobDetail, trigger);
//        scheduler.scheduleJob(jobDetail, Sets.newSet(trigger), true);
	}

	@Test
	public void addDemoJob02Config() throws SchedulerException {
		// 创建 JobDetail
		JobDetail jobDetail = JobBuilder.newJob(DemoJob02.class)
				.withIdentity("demoJob04") // 名字为 demoJob02
				.storeDurably() // 没有 Trigger 关联的时候任务是否被保留。因为创建 JobDetail 时，还没 Trigger 指向它，所以需要设置为 true ，表示保留。
				.build();
		// 创建 Trigger
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 3 ? * 1 *")
				// cron trigger 默认的 Misfire 处理策略是以 调度触发开始时间（triggerStartTime）开始，按 cron 表达式计算的下次执行时间（nextFireTime）
				// 这个 nextFireTime 如果与当前时间的差值大于 misfireThreshold ，则会被认为是 misfire 。对于这种情况，cron trigger 有三种处理策略：
				// 1. MISFIRE_INSTRUCTION_FIRE_ONCE_NOW ：以当前时间为触发频率立即触发一次执行
				// 2. MISFIRE_INSTRUCTION_DO_NOTHING ：不触发，等待下次调度
				// 3. MISFIRE_INSTRUCTION_SMART_POLICY ：默认策略（相当于MISFIRE_INSTRUCTION_FIRE_ONCE_NOW）
				// 可通过创建 trigger 时 withMisfireHandlingInstructionXXX 方法设置具体的指令，将保存到 QRTZ_TRIGGERS.MISFIRE_INSTR 字段

				// 具体执行由 MisfireHandler 线程处理，MisfireHandler 线程会遍历所有的 trigger ，找到 misfire 的 trigger ，然后执行对应的策略。
				.withMisfireHandlingInstructionFireAndProceed();
		Trigger trigger = TriggerBuilder.newTrigger()
				.forJob(jobDetail) // 对应 Job 为 demoJob01
				.startAt(Date.from(LocalDateTime.of(2023, 12, 29, 0, 0, 0)
						.atZone(ZoneOffset.systemDefault()).toInstant())) // 设置开始时间
				.withIdentity("demoJob04Trigger") // 名字为 demoJob01Trigger
				.withSchedule(scheduleBuilder) // 对应 Schedule 为 scheduleBuilder
				.build();
		// 添加调度任务
		scheduler.scheduleJob(jobDetail, trigger);
		while (true) {

		}
//        scheduler.scheduleJob(jobDetail, Sets.newSet(trigger), true);
	}

}