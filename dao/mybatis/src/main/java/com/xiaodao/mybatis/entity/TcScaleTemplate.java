package com.xiaodao.mybatis.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaodao.mybatis.valueobject.RelatedCondition;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
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

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createDate;

    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateDate;


    /**
     * 使用 TypeHandler 转换 json 和 vo
     */
    private List<RelatedCondition> relatedState;

    /* public void setRelatedState(String relatedState) {
        this.relatedState = relatedState;
        this.relatedStateJSON = new RawValue(relatedState);
    }

     *//**
     * 关联状态
     *//*
    // private Object relatedState;
    // private List<RelatedCondition> relatedState;
    @JsonRawValue
    private String relatedState;

    private RawValue relatedStateJSON; */


}

