package com.xiaodao.validation.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaodao.validation.ModelConstraint;
import lombok.Data;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.List;

@Data
public class ValidationConfig {
    private List<Constraints> constraints;


    @Data
    public static class Constraints {
        private String className;
        private List<ConstraintDefinition<? extends Annotation>> modelConstraints;
        private List<PropertyConstraints> properties;

    }

    @Data
    public static class PropertyConstraints {
        private String name;
        private String elementType;
        private boolean ignoreAnnotations;
        private List<ConstraintDefinition<? extends Annotation>> constraints;

    }


    public static ValidationConfig loadFromFile(String path) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path), ValidationConfig.class);
    }
}
