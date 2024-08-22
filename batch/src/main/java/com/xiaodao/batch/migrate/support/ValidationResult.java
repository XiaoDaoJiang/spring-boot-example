package com.xiaodao.batch.migrate.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.item.validator.ValidationException;

import javax.validation.ConstraintViolationException;

@Getter
@Setter
public class ValidationResult<T> {

    private final T item;
    private ConstraintViolationException violationException;
    private ValidationException validationException;
    private boolean error;

    public ValidationResult(T item) {
        this.item = item;
    }

    public boolean hasError() {
        return error;
    }
}
