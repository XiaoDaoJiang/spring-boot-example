package com.xiaodao.mybatis.valueobject;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.lang.annotation.*;

/**
 * json raw value property
 *
 * 用于序列化 json字符串（不转义） 和 反序列化 json字符串=> String
 *
 * @author jianghaitao
 * @Classname JsonRawProperty
 * @Version 1.0.0
 * @Date 2024-04-09 15:08
 * @Created by jianghaitao
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonRawValue
@JsonDeserialize(using = RawJsonDeserializer.class)
public @interface CustomJsonRawValue {
}
