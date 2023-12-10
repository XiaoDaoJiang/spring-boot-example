package com.xiaodao.translation.core.translator;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface DicEnum {

    String getCode();

    String getDesc();

    String convert(String val);

    @Getter
    @AllArgsConstructor
    enum SexDicEnum implements DicEnum{
        DUMMY("",""),
        FEMALE("1","男"), MALE("2","女");

        private final String code;
        private final String desc;

        public String convert(String val) {
            for (SexDicEnum value : SexDicEnum.values()) {
                if (value.code.equals(val)) {
                    return value.desc;
                }
                if (value.desc.equals(val)) {
                    return value.code;
                }
            }
            return null;
        }

    }


}
