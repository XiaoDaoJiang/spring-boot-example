package com.xiaodao.mybatis.valueobject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * 量表及模板关联状态
 *
 * @author jianghaitao
 * @Classname Condition
 * @Version 1.0.0
 * @Date 2024-04-08 16:28
 * @Created by jianghaitao
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ScaleRelatedConditionEnum {

    SWITCH_TYPE_STATUS("switchType", "开关期状态",
            new String[]{"开期", "关期"}),

    DEVICE_SWITCH_STATUS("deviceSwitchStatus", "DBS设备状态",
            new String[]{"未手术", "开机", "关机"});


    private final String code;

    private final String desc;

    private final String[] allowedValues;

    ScaleRelatedConditionEnum(String code, String desc, String[] allowedValues) {
        this.code = code;
        this.desc = desc;
        this.allowedValues = allowedValues;
    }


    public static ScaleRelatedConditionEnum getByCode(String code) {
        for (ScaleRelatedConditionEnum conditionEnum : values()) {
            if (conditionEnum.code.equals(code)) {
                return conditionEnum;
            }
        }
        return null;
    }

}
