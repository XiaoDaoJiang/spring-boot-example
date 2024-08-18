package com.xiaodao.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;


@Setter
@Getter
public abstract class BaseEntity {

    @CreatedBy
    public Long createBy;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedBy
    private Long modifyBy;

    @LastModifiedDate
    private LocalDateTime modifyTime;


}
