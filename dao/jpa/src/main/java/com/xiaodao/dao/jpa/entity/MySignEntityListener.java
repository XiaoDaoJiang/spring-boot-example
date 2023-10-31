package com.xiaodao.dao.jpa.entity;

import com.xiaodao.dao.jpa.service.SignatureService;
import com.xiaodao.dao.jpa.util.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Service
@Slf4j
public class MySignEntityListener {

    @Autowired
    private SignatureService signatureService;

    @PrePersist
    @PreUpdate
    public void sign(BaseEntity entity) {
        log.info("execute sign ");
        if (entity != null) {
            // log.info("set sign hmac");
            entity.setSignature(signatureService.sign(entity));
        }
        log.info("execute sign done ");

    }

    @PostLoad
    public void verify(BaseEntity entity) {
        if (entity != null) {
            // log.info("execute verify ");
            log.info("execute checkSing ");
            log.info("check sign result: {}", entity.getSignature().equals(SignUtils.doSign(SignUtils.getSignProperties(this))));

        }
    }
}
