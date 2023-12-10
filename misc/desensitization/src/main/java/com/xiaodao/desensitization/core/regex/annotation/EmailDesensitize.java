package com.xiaodao.desensitization.core.regex.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.xiaodao.desensitization.core.annotation.Desensitize;
import com.xiaodao.desensitization.core.regex.masker.EmailMasker;

import java.lang.annotation.*;

/**
 * 邮箱脱敏注解
 *
 * 
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@Desensitize(handler = EmailMasker.class)
public @interface EmailDesensitize {

    /**
     * 匹配的正则表达式
     */
    String regex() default "(^.)[^@]*(@.*$)";

    /**
     * 替换规则，邮箱;
     *
     * 比如：example@gmail.com 脱敏之后为 e****@gmail.com
     */
    String replacer() default "$1****$2";
}
