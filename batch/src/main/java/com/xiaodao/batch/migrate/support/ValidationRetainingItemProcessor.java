package com.xiaodao.batch.migrate.support;

import com.xiaodao.batch.migrate.domain.ValidationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.validation.BindException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@Slf4j
public class ValidationRetainingItemProcessor<T extends ValidationDTO> implements ItemProcessor<T, T> {

    private final BeanValidatingItemProcessor<T> delegate;

    public static final String VALIDATION_CONTEXT_KEY = "validationContext";

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
    public T process(T item) {
        this.validationContext.totalCount++;
        try {
            delegate.process(item); // 进行校验
            this.validationContext.successCount++;
        } catch (ValidationException e) {
            this.validationContext.invalidCount++;
            if (e.getCause() instanceof BindException) {
                BindException bindException = (BindException) e.getCause();
                item.setErrorMsg(getInvalidMessage(bindException));
            } else if (e.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException violationException = (ConstraintViolationException) e.getCause();
                final String collect = violationException.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(","));
                item.setErrorMsg(collect);
            } else {
                item.setErrorMsg(getInvalidMessage(e));
            }
        }
        return item; // 返回校验结果对象
    }

    private String getInvalidMessage(BindException bindException) {
        bindException.getFieldErrors().forEach(fieldError -> {
            log.error("field:{}, message:{}", fieldError.getField(), fieldError.getDefaultMessage());
        });
        return bindException.getFieldErrors().stream()
                .map(error -> error.getDefaultMessage() + (error.getRejectedValue() == null ? "" :
                        ":" + error.getRejectedValue()))
                .collect(Collectors.joining("；\n"));
    }

    @BeforeStep
    public void initValidStepContext(StepExecution stepExecution) {
        log.info("initValidStepContext..........");
        stepExecution.getExecutionContext().put(VALIDATION_CONTEXT_KEY, this.validationContext);
    }


    protected String getInvalidMessage(ValidationException e) {
        return e.getMessage();
    }

}
