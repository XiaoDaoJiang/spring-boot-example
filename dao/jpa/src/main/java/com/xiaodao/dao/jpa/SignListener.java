package com.xiaodao.dao.jpa;

import com.xiaodao.dao.jpa.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;

@Slf4j
public class SignListener implements PreUpdateEventListener, PreInsertEventListener {

    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        final Object entity = event.getEntity();
        if (entity instanceof BaseEntity) {
            log.info("需要签名");
            // ((BaseEntity) entity).sign();
        }
        return false;
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        final Object entity = event.getEntity();
        if (entity instanceof BaseEntity) {
            log.info("需要签名");
            // ((BaseEntity) entity).sign();
        }
        return false;
    }
}