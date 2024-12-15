package com.xiaodao.batch.migrate.job;

import com.xiaodao.batch.migrate.domain.Customer;
import com.xiaodao.batch.migrate.domain.CustomerDto;
import com.xiaodao.batch.migrate.domain.CustomerRawDto;
import com.xiaodao.batch.migrate.support.*;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.List;

import static com.xiaodao.batch.migrate.support.ValidationRetainingItemProcessor.VALIDATION_CONTEXT_KEY;

/**
 * @author xiaodaojiang
 * @Classname BatchMigrateJobConfig
 * @Version 1.0.0
 * @Date 2024-08-20 23:14
 * @Created by xiaodaojiang
 */
@AutoConfigureAfter({JpaRepositoriesAutoConfiguration.class})
@Configuration
@EnableBatchProcessing
public class BatchMigrateJobConfig {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    public Job demoJob(Step validStep, Step saveDbStep) throws Exception {
        return this.jobs.get("demoJob")
                // 使用 RunIdIncrementer
                .incrementer(new RunIdIncrementer())
                .start(validStep)
                .next(validToSaveDBDecider()).on("FAILED").fail()
                .from(validToSaveDBDecider()).on("COMPLETED").to(saveDbStep)
                .end()
                .build();
    }

    @Bean
    public JobExecutionDecider validToSaveDBDecider() {
        return new ValidToSaveDBDecider();
    }

    public class ValidToSaveDBDecider implements JobExecutionDecider {
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            final ValidationContext validateContext = (ValidationContext) stepExecution.getExecutionContext().get(VALIDATION_CONTEXT_KEY);
            if (validateContext.getInvalidCount() > 0) {
                return FlowExecutionStatus.FAILED;
            } else {
                return FlowExecutionStatus.COMPLETED;
            }
        }
    }


    @Bean
    public Step validStep(ItemReader<CustomerRawDto> itemReader,
                          MyListItemWriter<CustomerRawDto> itemWriter,
                          ValidationRetainingItemProcessor<CustomerRawDto> itemValidator,
                          MyStepListener<CustomerRawDto, CustomerRawDto> myStepListener
    ) throws Exception {
        return this.steps.get("validStep")
                .<CustomerRawDto, CustomerRawDto>chunk(1)
                .reader(itemReader)
                .processor(itemValidator)
                .writer(itemWriter)
                .listener((ChunkListener) myStepListener)
                .listener((StepExecutionListener) myStepListener)
                .listener((ItemReadListener<CustomerRawDto>) myStepListener)
                .listener((ItemProcessListener<CustomerRawDto, CustomerRawDto>) myStepListener)
                .listener((ItemWriteListener<CustomerRawDto>) myStepListener)
                .listener((SkipListener<CustomerRawDto, CustomerRawDto>) myStepListener)
                .build();
    }

    @Bean
    public MyStepListener<CustomerRawDto, CustomerRawDto> myStepListener() {
        return new MyStepListener<>();
    }


    @Bean
    @StepScope
    public EasyExcelItemReader<CustomerRawDto> itemReader(@Value("#{jobParameters['file']}") Resource resource) throws IOException {
        return new EasyExcelItemReader<>(resource, CustomerRawDto.class, null);
    }

    @Bean
    @StepScope
    public MyListItemWriter<CustomerRawDto> itemWriter(ExcelItemWriter<CustomerRawDto> excelItemWriter) {
        return new MyListItemWriter<>(excelItemWriter);
    }

    @Bean
    @StepScope
    public ExcelItemWriter<CustomerRawDto> excelItemWriter(@Value("#{jobParameters['outputFile']}") Resource resource) {
        return new ExcelItemWriter<>(resource, CustomerRawDto.class);
    }

    @Bean
    @StepScope
    public ValidationRetainingItemProcessor<CustomerRawDto> itemValidator() throws Exception {
        BeanValidatingItemProcessor<CustomerRawDto> validator = new BeanValidatingItemProcessor<>();
        validator.afterPropertiesSet();

        return new ValidationRetainingItemProcessor<>(validator);
    }


    @Bean
    public Step saveDbStep(ItemReader<CustomerRawDto> itemReader,
                           ItemProcessor<CustomerRawDto, Customer> saveDbItemProcessor,
                           ItemWriter<Customer> itemWriter,
                           MyStepListener<CustomerRawDto, CustomerRawDto> myStepListener,
                           PlatformTransactionManager transactionManager
    ) throws Exception {
        return this.steps.get("saveDbStep")
                .<CustomerRawDto, Customer>chunk(10)
                .reader(itemReader)
                .processor(saveDbItemProcessor)
                .writer(itemWriter)
                .transactionManager(transactionManager)
                .listener((ChunkListener) myStepListener)
                .listener((StepExecutionListener) myStepListener)
                .listener((ItemReadListener<CustomerRawDto>) myStepListener)
                .listener((ItemProcessListener<CustomerRawDto, CustomerRawDto>) myStepListener)
                .listener((ItemWriteListener<CustomerRawDto>) myStepListener)
                .listener((SkipListener<CustomerRawDto, CustomerRawDto>) myStepListener)
                .build();
    }

    @Bean
    public ItemProcessor<CustomerRawDto, Customer> saveDbItemProcessor(
            @Qualifier("myConversionService")
            ConversionService conversionService) {
        final SaveDbItemProcessor<CustomerRawDto, CustomerDto> toDtoItemProcessor = new SaveDbItemProcessor<>(conversionService, CustomerDto.class);
        final SaveDbItemProcessor<CustomerDto, Customer> toEntityItemProcessor = new SaveDbItemProcessor<>(conversionService, Customer.class);

        var delegates = List.of(toDtoItemProcessor, toEntityItemProcessor);

        CompositeItemProcessor<CustomerRawDto, Customer> compositeItemProcessor = new CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(delegates);
        return compositeItemProcessor;
    }

    @Bean
    public ItemWriter<Customer> dbItemWriter(EntityManagerFactory entityManagerFactory) {
        final HibernateItemWriter<Customer> customerHibernateItemWriter = new HibernateItemWriter<>();
        customerHibernateItemWriter.setSessionFactory(entityManagerFactory.unwrap(SessionFactory.class));
        return customerHibernateItemWriter;
    }

    // @Bean
    // public ItemWriter<Customer> dbItemWriter(CustomerService customerService) {
    //     return new SaveDbItemWriter<>(customerService);
    // }


}
