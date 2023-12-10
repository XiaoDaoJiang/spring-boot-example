package com.xiaodao.translation.core.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DicField {

    /**
     * 要翻译的字段名
     */
    String ref() default "";

    /**
     * 翻译后的字段名
     */
    String refName() default "";

    /**
     * 字典类型，全局唯一
     */
    String dicType() default "";

    String defaultVal() default "";

    String message() default "字段值不存在对应字典";


}
