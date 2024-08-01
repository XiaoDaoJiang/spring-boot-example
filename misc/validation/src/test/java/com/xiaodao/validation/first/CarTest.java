package com.xiaodao.validation.first;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jianghaitao
 * @Classname CarTest
 * @Version 1.0.0
 * @Date 2024-08-01 15:06
 * @Created by jianghaitao
 */
class CarTest {

    Validator validator = Validation.byProvider(HibernateValidator.class)
            .configure()
            .buildValidatorFactory().getValidator();

    @Test
    void isValid() {
        final Car car = new Car();
        car.setLimitYears("60");

        // System.out.println(car.isValid());
        final Set<ConstraintViolation<Car>> validate = validator.validate(car);
        //
        final String invalidMsg = validate.stream().map(v -> StrUtil.format("{}.{} value={} is {}",
                        v.getRootBeanClass(), v.getPropertyPath(), v.getInvalidValue(), v.getMessage()))
                .collect(Collectors.joining(";\n"));

        System.out.println(invalidMsg);
    }
}