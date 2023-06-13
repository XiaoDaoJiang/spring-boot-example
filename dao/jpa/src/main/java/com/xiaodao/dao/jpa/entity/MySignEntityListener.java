package com.xiaodao.dao.jpa.entity;

import com.xiaodao.dao.jpa.service.SignatureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Service
@Slf4j
public class MySignEntityListener {

    @Autowired
    private SignatureService signatureService;

    @PrePersist
    @PreUpdate
    public void prePersist(Object o) {
        log.info("execute sign ");
        if (o instanceof BaseEntity) {
            ((BaseEntity) o).setSignature(signatureService.sign(o));
        }
        log.info("execute sign done ");
    }
}
