package com.xiaodao.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserAgeBirthValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserAgeBirth {
    String message() default "年龄与出生日期不符";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
