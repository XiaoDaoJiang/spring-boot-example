package com.xiaodao.mybatis.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * 关联状态值对象
 *
 * @author jianghaitao
 * @Classname RelatedCondition
 * @Version 1.0.0
 * @Date 2024-04-08 17:33
 * @Created by jianghaitao
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RelatedCondition {

    private String code;

    private String desc;

    private List<String> allowedValues;

    public static RelatedCondition fromConditionEnum(ScaleRelatedConditionEnum conditionEnum) {
        return RelatedCondition.builder()
                .code(conditionEnum.getCode())
                .desc(conditionEnum.getDesc())
                .allowedValues(Arrays.asList(conditionEnum.getAllowedValues()))
                .build();
    }

}
