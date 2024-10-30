package com.xiaodao.validation;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class DateTimeFormatValidator implements ConstraintValidator<ValidDateTimeFormat, String> {


    @Override
    public void initialize(ValidDateTimeFormat constraintAnnotation) {
        // Initialization logic if needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are handled by @NotNull if needed
        }

        try {
            DateUtil.parse(value);
        } catch (Exception e) {
            log.debug("日期时间{}转换异常：", value);
            return false;
        }
        return true;
    }

}
