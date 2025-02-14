package com.xiaodao.validation.mapping;

import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.RandomUtil;
import com.xiaodao.validation.MyScriptEvaluatorFactory;
import com.xiaodao.validation.User;
import com.xiaodao.validation.custom.CustomPropertyNodeNameProvider;
import com.xiaodao.validation.first.Bar;
import com.xiaodao.validation.first.Car;
import com.xiaodao.validation.first.Foor;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Locale;
import java.util.Set;

/**
 * @author jianghaitao
 * @Classname MyJsonConstraintMappingContributorTest
 * @Version 1.0.0
 * @Date 2024-10-29 16:01
 * @Created by jianghaitao
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MyJsonConstraintMappingContributorTest {


    Validator validator;
    private static final String jsonConfigPath = "E:\\IdeaProjects\\learn\\spring-boot-example\\misc\\validation\\src\\main\\resources\\validation-config.json";


    @BeforeEach
    public void setUp() {
        final HibernateValidatorConfiguration hibernateValidatorConfiguration = Validation.byProvider(HibernateValidator.class)
                .configure();
        final CustomPropertyNodeNameProvider customPropertyNodeNameProvider = new CustomPropertyNodeNameProvider(Locale.getDefault());
        final HibernateValidatorConfiguration configure = hibernateValidatorConfiguration
                .propertyNodeNameProvider(customPropertyNodeNameProvider)
                .scriptEvaluatorFactory(new MyScriptEvaluatorFactory(ClassLoaderUtil.getClassLoader()));
        final MyJsonConstraintMappingContributor myJsonConstraintMappingContributor = new MyJsonConstraintMappingContributor();
        myJsonConstraintMappingContributor.addMapping(jsonConfigPath, configure);
        ValidatorFactory factory = configure.buildValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    void createConstraintMappings() {
        User user = User.builder()
                .name("A")
                .sex("未知")
                .email("invalid-email")
                .age(16)
                .dateOfBirth("2024-10-29/16:01")
                .car(new Car())
                .foor(new Foor().setFoorName(RandomUtil.randomString(60)))
                .bar(new Bar().setBarName("b").setBarAddress(RandomUtil.randomString(500)))
                .build();


        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            System.out.println(violation.getPropertyPath().toString() + ": " + violation.getMessage());
        }
    }

    @Test
    public void test() {
        final Foor foor = new Foor();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        validator.validate(foor).forEach(System.out::println);
    }
}