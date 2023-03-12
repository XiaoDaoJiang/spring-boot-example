package com.xiaodao.scheduler.quartz.job;

import com.xiaodao.scheduler.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class DemoJob01 extends QuartzJobBean {

	private final AtomicInteger counts = new AtomicInteger();

	@Autowired
	private DemoService demoService;


	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("[executeInternal][定时第 ({}) 次执行, demoService 为 ({})]", counts.incrementAndGet(),
				demoService);
	}
}
