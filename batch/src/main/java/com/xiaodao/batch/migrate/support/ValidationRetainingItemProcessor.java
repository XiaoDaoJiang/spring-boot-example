package com.xiaodao.batch.migrate.support;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

import javax.validation.ConstraintViolationException;

public class ValidationRetainingItemProcessor<T> implements ItemProcessor<T, ValidationResult<T>> {

    private final BeanValidatingItemProcessor<T> delegate;

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
        ValidationResult<T> result = new ValidationResult<>(item);
        try {
            delegate.process(item); // 进行校验
        } catch (ValidationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException violationException = (ConstraintViolationException) e.getCause();
                result.setViolationException(violationException); // 保存校验异常信息
            } else {
                result.setValidationException(e); // 其他类型的校验异常
            }
        }
        return result; // 返回校验结果对象
    }
}
