package com.xiaodao.translation.core.translator;

import com.xiaodao.translation.core.TranslateResult;
import com.xiaodao.translation.core.annotation.DicField;
import com.xiaodao.translation.core.annotation.EnumDicField;

public class EnumTranslator extends AbstractTranslator<EnumDicField> {

    @Override
    public TranslateResult translate(String src, EnumDicField type) {
        final DicField dicField = type.dicField();
        for (Enum<? extends DicEnum> enumConstant : type.dicEnum().getEnumConstants()) {
            DicEnum dicEnum = (DicEnum) enumConstant;
            if (dicEnum.getCode().equals(src)) {
                return TranslateResult.of(dicField.ref(), dicEnum.getDesc());
            }
            if (dicEnum.getDesc().equals(src)) {
                return TranslateResult.of(dicField.ref(), dicEnum.getCode());
            }
        }

        return defaultResult(type.dicField());

    }
}
