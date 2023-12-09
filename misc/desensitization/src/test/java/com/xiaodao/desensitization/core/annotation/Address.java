package com.xiaodao.desensitization.core.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.xiaodao.desensitization.core.DesensitizeTest;
import com.xiaodao.desensitization.core.handler.AddressHandler;

import java.lang.annotation.*;

/**
 * 地址
 *
 * 用于 {@link DesensitizeTest} 测试使用
 *
 * 
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@ValueDesensitize(handler = AddressHandler.class)
public @interface Address {

    String replacer() default "*";

}
