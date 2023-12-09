package com.xiaodao.desensitization.core.slider.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.xiaodao.desensitization.core.annotation.ValueDesensitize;
import com.xiaodao.desensitization.core.slider.masker.DefaultSliderMasker;

import java.lang.annotation.*;

/**
 * 滑动脱敏注解
 *
 * 
 */
@Documented
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@ValueDesensitize(handler = DefaultSliderMasker.class)
public @interface SliderDesensitize {

    /**
     * 后缀保留长度
     */
    int suffixKeep() default 0;

    /**
     * 替换规则，会将前缀后缀保留后，全部替换成 replacer
     *
     * 例如：prefixKeep = 1; suffixKeep = 2; replacer = "*";
     * 原始字符串  123456
     * 脱敏后     1***56
     */
    String replacer() default "*";

    /**
     * 前缀保留长度
     */
    int prefixKeep() default 0;
}
