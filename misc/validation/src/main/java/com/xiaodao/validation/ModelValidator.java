package com.xiaodao.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author jianghaitao
 * @Classname ModelValidator
 * @Version 1.0.0
 * @Date 2024-10-30 11:45
 * @Created by jianghaitao
 */
public class ModelValidator implements ConstraintValidator<ModelConstraint, Object> {

    @Override
    public void initialize(ModelConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        return true;
    }
}
