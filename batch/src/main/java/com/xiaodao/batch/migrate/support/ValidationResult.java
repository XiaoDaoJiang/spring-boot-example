package com.xiaodao.batch.migrate.support;

import org.springframework.batch.item.validator.ValidationException;

import javax.validation.ConstraintViolationException;

public class ValidationResult<T> {

    private final T item;
    private ConstraintViolationException violationException;
    private ValidationException validationException;

    public ValidationResult(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }

    public boolean hasErrors() {
        return violationException != null || validationException != null;
    }

    public ConstraintViolationException getViolationException() {
        return violationException;
    }

    public void setViolationException(ConstraintViolationException violationException) {
        this.violationException = violationException;
    }

    public ValidationException getValidationException() {
        return validationException;
    }

    public void setValidationException(ValidationException validationException) {
        this.validationException = validationException;
    }
}
