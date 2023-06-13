package com.xiaodao.dao.jpa.entity;

import com.xiaodao.dao.jpa.util.SignUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Slf4j
@Getter
@Setter
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, MySignEntityListener.class})
public abstract class BaseEntity {


    private String signature;

    //    @LastModifiedBy
    //    private String lastModifiedBy;

    @LastModifiedDate
    private Date lastModifiedDate;

    //    @CreatedBy
    //    private String createdBy;

    @CreatedDate
    private Date createdDate;


    // @PreUpdate
    // @PrePersist
    public void sign() {
        // 获取所有需要签名的字段
        log.info("execute sign ");
        this.setSignature(SignUtils.doSign(SignUtils.getSignProperties(this)));

    }

    //
    // @PostLoad
    public void checkSing() {
        log.info("execute checkSing ");
        log.info("check sign result: {}", this.getSignature().equals(SignUtils.doSign(SignUtils.getSignProperties(this))));
    }
}