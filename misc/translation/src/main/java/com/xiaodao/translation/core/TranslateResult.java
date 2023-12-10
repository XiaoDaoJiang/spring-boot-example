package com.xiaodao.translation.core;

import lombok.Value;

@Value
public class TranslateResult {
    String fieldName;
    String fieldValue;

    public static TranslateResult of(String fieldName, String fieldValue) {
        return new TranslateResult(fieldName, fieldValue);
    }


}
