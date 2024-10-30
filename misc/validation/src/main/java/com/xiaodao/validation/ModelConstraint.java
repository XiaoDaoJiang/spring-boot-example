package com.xiaodao.validation;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类级别验证
 *
 * @author jianghaitao
 * @Classname ModelValidator
 * @Version 1.0.0
 * @Date 2024-10-30 11:44
 * @Created by jianghaitao
 */
@Constraint(validatedBy = ModelValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelConstraint {
    String message() default "模型验证失败";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
