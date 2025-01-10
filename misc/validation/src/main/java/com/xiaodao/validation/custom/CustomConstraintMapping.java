package com.xiaodao.validation.custom;

import org.hibernate.validator.internal.cfg.context.DefaultConstraintMapping;
import org.hibernate.validator.internal.properties.javabean.JavaBeanHelper;

/**
 * @author jianghaitao
 * @Classname CustomConstraintMapping
 * @Version 1.0.0
 * @Date 2025-01-02 17:04
 * @Created by jianghaitao
 */
public class CustomConstraintMapping extends DefaultConstraintMapping {


    public CustomConstraintMapping(JavaBeanHelper javaBeanHelper) {
        super(javaBeanHelper);
    }

    public static CustomConstraintMapping create(JavaBeanHelper javaBeanHelper) {
        return new CustomConstraintMapping(javaBeanHelper);
    }
}
