package com.xiaodao.validation.mapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaodao.validation.User;
import com.xiaodao.validation.UserAgeBirth;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.cfg.GenericConstraintDef;
import org.hibernate.validator.cfg.defs.EmailDef;
import org.hibernate.validator.cfg.defs.MinDef;
import org.hibernate.validator.cfg.defs.NotNullDef;
import org.hibernate.validator.cfg.defs.SizeDef;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.lang.annotation.ElementType;
import java.util.Set;

public class JsonConstraintLoader {

    public static Validator loadValidatorFromJson(String jsonPath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(jsonPath));

        HibernateValidatorConfiguration config = Validation.byProvider(HibernateValidator.class)
                .configure();

        ConstraintMapping mapping = config.createConstraintMapping();

        JsonNode userConstraints = root.get("User");
        if (userConstraints != null) {
            mapping.type(User.class)
                    .constraint(new GenericConstraintDef<>(UserAgeBirth.class))
                    .field("name")
                    .constraint(new NotNullDef().message(userConstraints.get("name").get("NotNull").get("message").asText()))
                    .constraint(new SizeDef()
                            .min(userConstraints.get("name").get("Size").get("min").asInt())
                            .max(userConstraints.get("name").get("Size").get("max").asInt())
                            .message(userConstraints.get("name").get("Size").get("message").asText()))
                    .property("email", ElementType.FIELD)
                    .constraint(new EmailDef().message(userConstraints.get("email").get("Email").get("message").asText()))
                    .property("age", ElementType.FIELD)
                    .constraint(new MinDef()
                            .value(userConstraints.get("age").get("Min").get("value").asLong())
                            .message(userConstraints.get("age").get("Min").get("message").asText()))

            ;

        }

        config.addMapping(mapping);
        ValidatorFactory factory = config.buildValidatorFactory();

        return factory.getValidator();
    }

    public static void main(String[] args) throws Exception {
        Validator validator = loadValidatorFromJson("E:\\IdeaProjects\\learn\\spring-boot-example\\misc\\validation\\src\\main\\resources\\user-validation.json");
        User user = User.builder().build();
        user.setName("A");
        user.setEmail("invalid-email");
        user.setAge(16);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            System.out.println(violation.getPropertyPath() + ": " + violation.getMessage());
        }
    }
}
