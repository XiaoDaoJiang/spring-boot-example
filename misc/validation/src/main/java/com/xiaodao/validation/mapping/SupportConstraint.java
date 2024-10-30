package com.xiaodao.validation.mapping;

import cn.hutool.core.collection.ListUtil;
import com.xiaodao.validation.ModelConstraint;
import com.xiaodao.validation.ScriptConstraint;
import com.xiaodao.validation.ValidDateTimeFormat;
import lombok.Getter;

import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author jianghaitao
 * @Classname SupportConstraint
 * @Version 1.0.0
 * @Date 2024-10-29 18:52
 * @Created by jianghaitao
 */
@Getter
public enum SupportConstraint {

    // 内置校验
    /**
     * 非空
     */
    NOT_NULL(NotNull.class, ListUtil.of("NotNull")),
    /**
     * 非空
     */
    NOT_EMPTY(NotEmpty.class, ListUtil.of("NotEmpty")),
    /**
     * 最小值
     */
    MIN(Min.class, ListUtil.of("Min")),
    /**
     * 最大值
     */
    MAX(Max.class, ListUtil.of("Max")),
    /**
     * 长度
     */
    LENGTH(Size.class, ListUtil.of("Size")),
    /**
     * 正则表达式
     */
    PATTERN(Pattern.class, ListUtil.of("Pattern")),
    /**
     * 自定义校验
     */
    CUSTOM(Pattern.class, ListUtil.of("Custom")),
    /**
     * 电子邮件
     */
    EMAIL(Email.class, ListUtil.of("Email")),

    // 自定义校验约束
    /**
     * 日期格式
     */
    DATETIME(ValidDateTimeFormat.class, ListUtil.of("日期时间")),

    /**
     * 类级别验证
     */
    CLASS_VALID(ModelConstraint.class, ListUtil.of("ModelConstraint", "类校验")),

    /**
     * 字段脚本语言约束
     */
    SCRIPT_CONSTRAINT(ScriptConstraint.class, ListUtil.of("ScriptConstraint", "脚本约束")),
    //
    ;


    private Class<? extends Annotation> constraintType;

    private List<String> constraintNames;

    SupportConstraint(Class<? extends Annotation> constraintType, List<String> constraintNames) {
        this.constraintType = constraintType;
        this.constraintNames = constraintNames;
    }

    public static Class<? extends Annotation> resolveConstraintType(String constraintName) {
        for (SupportConstraint supportConstraint : SupportConstraint.values()) {
            if (supportConstraint.getConstraintNames().contains(constraintName)) {
                return supportConstraint.getConstraintType();
            }
        }
        return null;
    }

}
