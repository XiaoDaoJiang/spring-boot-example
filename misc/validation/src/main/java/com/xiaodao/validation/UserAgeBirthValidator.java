package com.xiaodao.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author jianghaitao
 * @Classname UserAgeBirthValidator
 * @Version 1.0.0
 * @Date 2024-10-30 11:40
 * @Created by jianghaitao
 */
public class UserAgeBirthValidator implements ConstraintValidator<UserAgeBirth, User> {
    @Override
    public void initialize(UserAgeBirth constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(User value, ConstraintValidatorContext context) {
        return false;
    }
}
