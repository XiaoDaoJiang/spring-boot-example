package com.xiaodao.mybatis.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.RawValue;
import com.xiaodao.mybatis.valueobject.RelatedCondition;
import com.xiaodao.mybatis.valueobject.ScaleRelatedConditionEnum;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * (TcScaleTemplate)实体类
 *
 * @author jianghaitao
 * @since 2024-04-08 11:13:01
 */
@Data
public class TcScaleTemplate implements Serializable {
    private static final long serialVersionUID = -96231236065435796L;

    private Integer id;

    private String name;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;

    public void setRelatedState(String relatedState) {
        this.relatedState = relatedState;
        this.relatedStateJSON = new RawValue(relatedState);
    }

    /**
     * 关联状态
     */
    // private Object relatedState;
    // private List<RelatedCondition> relatedState;
    @JsonRawValue
    private String relatedState;

    private RawValue relatedStateJSON;


}

