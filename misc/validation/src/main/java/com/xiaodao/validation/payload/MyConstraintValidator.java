package com.xiaodao.validation.payload;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.NumberUtil;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.util.Set;

/**
 * @author jianghaitao
 * @Classname MyConstraintValidator
 * @Version 1.0.0
 * @Date 2024-08-01 16:12
 * @Created by jianghaitao
 */
public class MyConstraintValidator implements ConstraintValidator<MyAgeStrConstraint, String> {

    private Severity payload;

    private int max;

    @Override
    public void initialize(MyAgeStrConstraint constraintAnnotation) {
        // 建议在初始化时，存储payload，不需要在isValid再获取
        if (constraintAnnotation.payload() != null && constraintAnnotation.payload().length > 0) {
            payload = (Severity) Singleton.get(constraintAnnotation.payload()[0]);
        }
        max = (int) constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        final boolean isValid = value != null && !value.isEmpty();
        // 使用 HibernateConstraintValidatorContext 动态设置消息模板中的变量
        HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
        hibernateContext.addMessageParameter("value", value);

        if (isValid) {
            if (NumberUtil.isInteger(value)) {
                final int age = NumberUtil.parseInt(value);
                if (age > max) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(payload != null ?
                            payload.getMsgPrefix() + context.getDefaultConstraintMessageTemplate()
                            : "tip:" + context.getDefaultConstraintMessageTemplate())
                            .addConstraintViolation();
                    return false;
                }

            } else {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("输入的🚗使用年份非数字：{value}")
                        .addConstraintViolation();
                return false;
            }
        }

        if (context instanceof ConstraintValidatorContextImpl) {
            final ConstraintValidatorContextImpl validatorContext = (ConstraintValidatorContextImpl) context;

            // 这里可以根据 payload 类型来改变校验逻辑
            final Set<Class<? extends Payload>> constraintValidatorPayload = validatorContext.getConstraintDescriptor().getPayload();
            if (constraintValidatorPayload != null) {
                System.out.println("constraintValidatorPayload: " + constraintValidatorPayload);
            }

            if (!isValid && constraintValidatorPayload != null) {
                context.disableDefaultConstraintViolation();

                final String constraintMessageTemplate;
                if (constraintValidatorPayload.contains(Severity.Info.class)) {
                    // todo 启用不了${foo} el
                    // HV000257: Expression variables have been defined for constraint interface com.xiaodao.validation.payload.MyConstraint while Expression Language is not enabled.
                    constraintMessageTemplate = "warn:" + validatorContext.getDefaultConstraintMessageTemplate();
                } else {
                    constraintMessageTemplate = "error:" + validatorContext.getDefaultConstraintMessageTemplate();
                }

                // todo 启用不了${foo} el
                hibernateContext.addExpressionVariable("foo", value + "hhh");

                hibernateContext
                        .buildConstraintViolationWithTemplate(constraintMessageTemplate)
                        .addConstraintViolation();
            }
        }


        return isValid;
    }
}
