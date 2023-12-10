package com.xiaodao.desensitization.core.slider.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.xiaodao.desensitization.core.annotation.Desensitize;
import com.xiaodao.desensitization.core.slider.masker.PasswordMasker;

import java.lang.annotation.*;

/**
 * 密码
 *
 * 
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@Desensitize(handler = PasswordMasker.class)
public @interface PasswordDesensitize {

    /**
     * 前缀保留长度
     */
    int prefixKeep() default 0;

    /**
     * 后缀保留长度
     */
    int suffixKeep() default 0;

    /**
     * 替换规则，密码;
     *
     * 比如：123456 脱敏之后为 ******
     */
    String replacer() default "*";

}
