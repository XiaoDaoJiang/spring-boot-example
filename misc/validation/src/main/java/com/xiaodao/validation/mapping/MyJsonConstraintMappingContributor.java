package com.xiaodao.validation.mapping;

import cn.hutool.core.util.ClassUtil;
import com.xiaodao.validation.custom.CustomConstraintMapping;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.context.PropertyConstraintMappingContext;
import org.hibernate.validator.cfg.context.TypeConstraintMappingContext;
import org.hibernate.validator.internal.engine.ConfigurationImpl;
import org.hibernate.validator.internal.engine.DefaultPropertyNodeNameProvider;
import org.hibernate.validator.internal.properties.DefaultGetterPropertySelectionStrategy;
import org.hibernate.validator.internal.properties.javabean.JavaBeanHelper;
import org.hibernate.validator.spi.nodenameprovider.PropertyNodeNameProvider;
import org.hibernate.validator.spi.properties.GetterPropertySelectionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.List;

@NoArgsConstructor
public class MyJsonConstraintMappingContributor {


    private static final Logger log = LoggerFactory.getLogger(MyJsonConstraintMappingContributor.class);

    /**
     * 根据json配置文件创建约束映射
     */
    public void addMapping(String jsonConfigPath, HibernateValidatorConfiguration configure) {
        final ConfigurationImpl configuration = (ConfigurationImpl) configure;
        PropertyNodeNameProvider propertyNodeNameProvider = configuration.getPropertyNodeNameProvider();
        if (propertyNodeNameProvider == null) {
            propertyNodeNameProvider = new DefaultPropertyNodeNameProvider();
        }
        GetterPropertySelectionStrategy getterPropertySelectionStrategy = configuration.getGetterPropertySelectionStrategy();
        if (getterPropertySelectionStrategy == null) {
            getterPropertySelectionStrategy = new DefaultGetterPropertySelectionStrategy();
        }
        try {
            ValidationConfig config = ValidationConfig.loadFromFile(jsonConfigPath);
            for (ValidationConfig.Constraints constraints : config.getConstraints()) {
                final Class<?> clazz = ClassUtil.loadClass(constraints.getClassName());
                // Class<?> clazz = Class.forName(classConstraints.getClassName());
                // ConstraintMapping mapping = configure.createConstraintMapping();
                final CustomConstraintMapping mapping = CustomConstraintMapping.create(new JavaBeanHelper(getterPropertySelectionStrategy, propertyNodeNameProvider));
                final TypeConstraintMappingContext<?> typeConstraintMappingContext = mapping.type(clazz);
                final List<ConstraintDefinition<? extends Annotation>> modelConstraints = constraints.getModelConstraints();
                if (modelConstraints != null && !modelConstraints.isEmpty()) {
                    modelConstraints.forEach(typeConstraintMappingContext::constraint);
                }
                if (CollectionUtils.isNotEmpty(constraints.getProperties())) {
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
                }
                configure.addMapping(mapping);
            }
        } catch (Exception e) {
            log.error("Failed to load validation configuration from JSON", e);
        }
    }
}
