package com.xiaodao.batch.migrate.job;

import com.xiaodao.batch.migrate.domain.CustomerRawDto;
import com.xiaodao.batch.migrate.support.EasyExcelItemReader;
import com.xiaodao.batch.migrate.support.MyListItemWriter;
import com.xiaodao.batch.migrate.support.ValidationResult;
import com.xiaodao.batch.migrate.support.ValidationRetainingItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
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
                .start(validStep)
                .build();
    }

    @Bean
    public Step validStep(ItemReader<CustomerRawDto> itemReader) throws Exception {
        return this.steps.get("validStep")
                .<CustomerRawDto, ValidationResult<CustomerRawDto>>chunk(1)
                .reader(itemReader)
                .processor(itemValidator())
                .writer(itemWriter())
                .build();
    }

    @Bean
    @StepScope
    public EasyExcelItemReader<CustomerRawDto> itemReader(@Value("#{jobParameters['file']}") Resource resource) throws IOException {
        return new EasyExcelItemReader<>(resource, CustomerRawDto.class, null);
    }

    @Bean
    @StepScope
    public MyListItemWriter<ValidationResult<CustomerRawDto>> itemWriter() {
        return new MyListItemWriter<>();
    }

    @Bean
    public ItemProcessor<CustomerRawDto, ValidationResult<CustomerRawDto>> itemValidator() throws Exception {
        BeanValidatingItemProcessor<CustomerRawDto> validator = new BeanValidatingItemProcessor<>();
        validator.afterPropertiesSet();

        return new ValidationRetainingItemProcessor<>(validator);
    }


}
