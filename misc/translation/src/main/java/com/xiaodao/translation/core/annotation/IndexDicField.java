package com.xiaodao.translation.core.annotation;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xiaodao.translation.core.serializer.DicFieldSerializer;
import com.xiaodao.translation.core.translator.IndexTranslator;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@JacksonAnnotationsInside
@JsonSerialize(using = DicFieldSerializer.class)
@Translate(translator = IndexTranslator.class)
public @interface IndexDicField {

    String[] dicKeys() default {};

    String[] dicValues() default {};

    DicField dicField() default @DicField();


}
