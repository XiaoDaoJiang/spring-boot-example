package com.xiaodao.translation.core.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xiaodao.translation.core.serializer.DicFieldSerializer;
import com.xiaodao.translation.core.translator.Translator;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE})
@JacksonAnnotationsInside
@JsonSerialize(using = DicFieldSerializer.class)
public @interface Translate {

    Class<? extends Translator<? extends Annotation>> translator();

    /**
     * 是否忽略自动翻译，默认false,翻译
     */
    boolean ignore() default false;

}
