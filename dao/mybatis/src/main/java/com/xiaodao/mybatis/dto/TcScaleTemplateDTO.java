package com.xiaodao.mybatis.dto;

import com.xiaodao.mybatis.valueobject.ScaleRelatedConditionEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * (TcScaleTemplate)实体类
 *
 * @author jianghaitao
 * @since 2024-04-08 11:13:01
 */
@Setter
@Getter
public class TcScaleTemplateDTO {

    private Integer id;

    private String name;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;

    /**
     * 关联状态
     */
    private List<ScaleRelatedConditionEnum> relatedStates;

}

