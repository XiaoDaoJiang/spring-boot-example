package com.xiaodao.batch.migrate.job;

import com.xiaodao.batch.migrate.domain.CustomerRawDto;
import com.xiaodao.batch.migrate.support.*;
import org.springframework.batch.core.*;
import com.xiaodao.batch.migrate.support.EasyExcelItemReader;
import com.xiaodao.batch.migrate.support.ExcelItemWriter;
import com.xiaodao.batch.migrate.support.ValidationResult;
import com.xiaodao.batch.migrate.support.ValidationRetainingItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * @author xiaodaojiang
 * @Classname BatchMigrateJobConfig
 * @Version 1.0.0
 * @Date 2024-08-20 23:14
 * @Created by xiaodaojiang
 */
@Configuration
@EnableBatchProcessing
public class BatchMigrateJobConfig {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    public Job demoJob(Step validStep) throws Exception {
        return this.jobs.get("demoJob")
                // 使用 RunIdIncrementer
                .incrementer(new RunIdIncrementer())
                .start(validStep)
                .build();
    }

    @Bean
    public Step validStep(ItemReader<CustomerRawDto> itemReader,
                          MyStepListener<CustomerRawDto, ValidationResult<CustomerRawDto>> myStepListener
    ) throws Exception {
        return this.steps.get("validStep")
                .<CustomerRawDto, ValidationResult<CustomerRawDto>>chunk(1)
                .reader(itemReader)
                .processor(itemValidator())
                .writer(itemWriter())
                .listener((ChunkListener) myStepListener)
                .listener((StepExecutionListener) myStepListener)
                .listener((ItemReadListener<CustomerRawDto>) myStepListener)
                .listener((ItemProcessListener<CustomerRawDto, ValidationResult<CustomerRawDto>>) myStepListener)
                .listener((ItemWriteListener<ValidationResult<CustomerRawDto>>) myStepListener)
                .listener((SkipListener<CustomerRawDto, ValidationResult<CustomerRawDto>>) myStepListener)
                .build();
    }

    @Bean
    public MyStepListener<CustomerRawDto, ValidationResult<CustomerRawDto>> myStepListener() {
        return new MyStepListener<>();
    }


    @Bean
    @StepScope
    public EasyExcelItemReader<CustomerRawDto> itemReader(@Value("#{jobParameters['file']}") Resource resource) throws IOException {
        return new EasyExcelItemReader<>(resource, CustomerRawDto.class, null);
    }

    @Bean
    @StepScope
    public ExcelItemWriter<ValidationResult<CustomerRawDto>> itemWriter() {
        return new ExcelItemWriter<>();
    }

    @Bean
    public ItemProcessor<CustomerRawDto, ValidationResult<CustomerRawDto>> itemValidator() throws Exception {
        BeanValidatingItemProcessor<CustomerRawDto> validator = new BeanValidatingItemProcessor<>();
        validator.afterPropertiesSet();

        return new ValidationRetainingItemProcessor<>(validator);
    }


}
