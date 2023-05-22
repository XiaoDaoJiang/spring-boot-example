package com.xiaodao.dao.jpa.entity;

import com.xiaodao.dao.jpa.util.MyUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {


    private String signature;

//    @LastModifiedBy
//    private String lastModifiedBy;

    @LastModifiedDate
    private Date lastModifiedDate;

//    @CreatedBy
//    private String createdBy;

    @CreatedDate
    private Date createdDate;


    @PreUpdate
    @PrePersist
    public void sign() {
        // 获取所有需要签名的字段
        this.setSignature(MyUtils.doSign(MyUtils.getSignProperties(this)));

    }
}