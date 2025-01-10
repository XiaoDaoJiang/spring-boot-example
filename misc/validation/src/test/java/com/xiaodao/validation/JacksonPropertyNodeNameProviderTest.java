package com.xiaodao.validation;

import com.xiaodao.validation.custom.CustomConstraintMapping;
import com.xiaodao.validation.custom.JacksonPropertyNodeNameProvider;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.defs.LengthDef;
import org.hibernate.validator.internal.properties.DefaultGetterPropertySelectionStrategy;
import org.hibernate.validator.internal.properties.javabean.JavaBeanHelper;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author jianghaitao
 * @Classname JacksonPropertyNodeNameProviderTest
 * @Version 1.0.0
 * @Date 2025-01-02 14:33
 * @Created by jianghaitao
 */
public class JacksonPropertyNodeNameProviderTest {
    @Test
    public void nameIsReadFromJacksonAnnotationOnGetter() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .propertyNodeNameProvider(new JacksonPropertyNodeNameProvider())
                .buildValidatorFactory();

        Validator validator = validatorFactory.getValidator();

        Person clarkKent = new Person(null, "Kent");

        Set<ConstraintViolation<Person>> violations = validator.validate(clarkKent);
        ConstraintViolation<Person> violation = violations.iterator().next();

        assertEquals("first_name", violation.getPropertyPath().toString());
    }


    @Test
    public void nameIsReadFromJacksonAnnotationOnGetter2() {
        final JacksonPropertyNodeNameProvider jacksonPropertyNodeNameProvider = new JacksonPropertyNodeNameProvider();
        final HibernateValidatorConfiguration hibernateValidatorConfiguration = Validation.byProvider(HibernateValidator.class)
                .configure()
                .propertyNodeNameProvider(jacksonPropertyNodeNameProvider);

        // 编程式约束需要单独设置 propertyNodeNameProvider，否则会使用 defaultPropertyNodeNameProvider
        // final ConstraintMapping constraintMapping = hibernateValidatorConfiguration.createConstraintMapping();
        final CustomConstraintMapping customConstraintMapping =
                new CustomConstraintMapping(new JavaBeanHelper(new DefaultGetterPropertySelectionStrategy(), jacksonPropertyNodeNameProvider));
        customConstraintMapping.type(Person.class)
                .field("lastName")
                .constraint(new LengthDef().max(2));
        hibernateValidatorConfiguration.addMapping(customConstraintMapping);

        ValidatorFactory validatorFactory = hibernateValidatorConfiguration
                .buildValidatorFactory();

        Validator validator = validatorFactory.getValidator();

        Person clarkKent = new Person(null, "Kent");

        Set<ConstraintViolation<Person>> violations = validator.validate(clarkKent);
        for (ConstraintViolation<Person> violation : violations) {
            System.out.println(violation.getPropertyPath().toString() + ": " + violation.getMessage());
        }
        ConstraintViolation<Person> violation = violations.iterator().next();

        // assertEquals("first_name", violation.getPropertyPath().toString());
    }

}
