package com.xiaodao.validation.mapping;

import cn.hutool.core.util.ClassUtil;
import lombok.NoArgsConstructor;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.cfg.context.PropertyConstraintMappingContext;
import org.hibernate.validator.cfg.context.TypeConstraintMappingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;

@NoArgsConstructor
public class MyJsonConstraintMappingContributor {


    private static final Logger log = LoggerFactory.getLogger(MyJsonConstraintMappingContributor.class);

    /**
     * 根据json配置文件创建约束映射
     */
    public void addMapping(String jsonConfigPath, HibernateValidatorConfiguration configure) {
        try {
            ValidationConfig config = ValidationConfig.loadFromFile(jsonConfigPath);
            for (ValidationConfig.Constraints constraints : config.getConstraints()) {
                final Class<?> clazz = ClassUtil.loadClass(constraints.getClassName());
                // Class<?> clazz = Class.forName(classConstraints.getClassName());
                ConstraintMapping mapping = configure.createConstraintMapping();
                final TypeConstraintMappingContext<?> typeConstraintMappingContext = mapping.type(clazz);
                if (constraints.getModelConstraints() != null) {
                    typeConstraintMappingContext.constraint(constraints.getModelConstraints());
                }
                for (ValidationConfig.PropertyConstraints property : constraints.getProperties()) {
                    final PropertyConstraintMappingContext propertyConstraintMappingContext =
                            typeConstraintMappingContext.property(property.getName(), ElementType.valueOf(property.getElementType()))
                                    // 手动设置valid后 ignoreAnnotations 不管用
                                    // .ignoreAnnotations(property.isIgnoreAnnotations())
                                    .valid();

                    if (property.getConstraints() != null) {
                        for (ConstraintDefinition<? extends Annotation> constraintDef : property.getConstraints()) {
                            // ConstraintDef<?, ?> def = ConstraintDefFactory.createConstraintDef(constraintDef);
                            // propertyConstraintMappingContext.constraint(def);
                            propertyConstraintMappingContext.constraint(constraintDef);
                        }
                    }

                }
                configure.addMapping(mapping);
            }
        } catch (Exception e) {
            log.error("Failed to load validation configuration from JSON", e);
        }
    }
}
