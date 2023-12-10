package com.xiaodao.desensitization.core.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xiaodao.desensitization.core.masker.Masker;
import com.xiaodao.desensitization.core.serializer.StringDesensitizeSerializer;

import java.lang.annotation.*;

/**
 * 顶级脱敏注解，自定义注解需要使用此注解
 *
 * 
 */
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside // 此注解是其他所有 jackson 注解的元注解，打上了此注解的注解表明是 jackson 注解的一部分
@JsonSerialize(using = StringDesensitizeSerializer.class) // 指定序列化器
public @interface Desensitize {

    /**
     * 脱敏处理器
     */
    Class<? extends Masker<? extends Annotation>> handler();

}
