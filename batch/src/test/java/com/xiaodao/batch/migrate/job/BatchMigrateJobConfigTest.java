package com.xiaodao.batch.migrate.job;

import com.xiaodao.batch.migrate.domain.CustomerRawDto;
import com.xiaodao.batch.migrate.support.ValidationResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author xiaodaojiang
 * @Classname BatchMigrateJobConfigTest
 * @Version 1.0.0
 * @Date 2024-08-21 00:52
 * @Created by xiaodaojiang
 */
// @ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BatchMigrateJobConfig.class})
public class BatchMigrateJobConfigTest {

    @Autowired
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private ListItemWriter<ValidationResult<CustomerRawDto>> listItemWriter;

    @Test
    public void testItemValidation() throws Exception {

        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("file", "classpath:customerRawDto.xlsx")
                .toJobParameters();


        // when
        JobExecution jobExecution = this.jobLauncher.run(this.job, jobParameters);

        // then
        Assert.assertEquals(ExitStatus.COMPLETED.getExitCode(), jobExecution.getExitStatus().getExitCode());
        List<? extends ValidationResult<CustomerRawDto>> writtenItems = this.listItemWriter.getWrittenItems();
        Assert.assertEquals(1, writtenItems.size());
        Assert.assertEquals("想撤", writtenItems.get(0).getItem().getCustomerName());
    }

}