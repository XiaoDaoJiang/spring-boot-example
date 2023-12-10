package com.xiaodao.desensitization.core.slider.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.xiaodao.desensitization.core.annotation.Desensitize;
import com.xiaodao.desensitization.core.slider.masker.ChineseNameMasker;

import java.lang.annotation.*;

/**
 * 中文名
 *
 * 
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@Desensitize(handler = ChineseNameMasker.class)
public @interface ChineseNameDesensitize {

    /**
     * 前缀保留长度
     */
    int prefixKeep() default 1;

    /**
     * 后缀保留长度
     */
    int suffixKeep() default 0;

    /**
     * 替换规则，中文名;比如：刘子豪脱敏之后为刘**
     */
    String replacer() default "*";

}
