package com.xiaodao.translation.core.translator;

import com.xiaodao.translation.core.TranslateResult;
import com.xiaodao.translation.core.annotation.DicField;

import java.lang.annotation.Annotation;

public abstract class AbstractTranslator<T extends Annotation> implements Translator<T> {

    protected String getFiledName(DicField dicField) {
        return dicField.ref();
    }

    protected String getDefault(DicField dicField) {
        return dicField.defaultVal();
    }


    protected TranslateResult defaultResult(DicField dicField) {
        return TranslateResult.of(this.getFiledName(dicField), this.getDefault(dicField));
    }

}
