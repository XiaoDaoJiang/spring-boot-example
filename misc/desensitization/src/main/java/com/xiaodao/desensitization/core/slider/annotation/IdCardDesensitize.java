package com.xiaodao.desensitization.core.slider.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.xiaodao.desensitization.core.annotation.Desensitize;
import com.xiaodao.desensitization.core.slider.masker.IdCardMasker;

import java.lang.annotation.*;

/**
 * 身份证
 *
 * 
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@Desensitize(handler = IdCardMasker.class)
public @interface IdCardDesensitize {

    /**
     * 前缀保留长度
     */
    int prefixKeep() default 6;

    /**
     * 后缀保留长度
     */
    int suffixKeep() default 2;

    /**
     * 替换规则，身份证号码;比如：530321199204074611 脱敏之后为 530321**********11
     */
    String replacer() default "*";

}
