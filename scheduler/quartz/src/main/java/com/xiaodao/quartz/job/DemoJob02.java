package com.xiaodao.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
@DisallowConcurrentExecution
public class DemoJob02 extends QuartzJobBean {


	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("[executeInternal][我开始的执行了]");
	}

}