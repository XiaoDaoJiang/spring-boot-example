package com.xiaodao.translation.core.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xiaodao.translation.core.serializer.DicFieldSerializer;
import com.xiaodao.translation.core.translator.DicEnum;
import com.xiaodao.translation.core.translator.EnumTranslator;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@JacksonAnnotationsInside
@Translate(translator = EnumTranslator.class)
@JsonSerialize(using = DicFieldSerializer.class)
public @interface EnumDicField {

    DicField dicField() default @DicField();

    Class<? extends Enum<? extends DicEnum>> dicEnum();


}
