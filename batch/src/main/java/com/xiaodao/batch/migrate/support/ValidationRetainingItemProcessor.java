package com.xiaodao.batch.migrate.support;

import lombok.Data;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

import javax.validation.ConstraintViolationException;

public class ValidationRetainingItemProcessor<T> implements ItemProcessor<T, ValidationResult<T>> {

    private final BeanValidatingItemProcessor<T> delegate;

    private final ValidationContext validationContext = new ValidationContext();

    public ValidationRetainingItemProcessor() {
        this.delegate = new BeanValidatingItemProcessor<>();
        this.delegate.setFilter(false); // 不过滤非法的 Bean
    }

    public ValidationRetainingItemProcessor(BeanValidatingItemProcessor<T> delegate) {
        this.delegate = delegate;
        this.delegate.setFilter(false); // 不过滤非法的 Bean
    }

    @Override
    public ValidationResult<T> process(T item) {
        this.validationContext.totalCount++;
        ValidationResult<T> result = new ValidationResult<>(item);
        try {
            delegate.process(item); // 进行校验
            this.validationContext.successCount++;
        } catch (ValidationException e) {
            this.validationContext.invalidCount++;
            result.setError(true);
            if (e.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException violationException = (ConstraintViolationException) e.getCause();
                result.setViolationException(violationException); // 保存校验异常信息
            } else {
                result.setValidationException(e); // 其他类型的校验异常
            }
        }
        return result; // 返回校验结果对象
    }

    @BeforeStep
    public void initValidStepContext(StepExecution stepExecution) {
        stepExecution.getExecutionContext().put("validationContext", this.validationContext);
    }

    @Data
    public static class ValidationContext {
        private int totalCount;
        private int successCount;
        private int invalidCount;
    }

}
