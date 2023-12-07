package com.xiaodao.audit;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class AuditEventListener implements PersistEventListener, PreInsertEventListener, SaveOrUpdateEventListener,MergeEventListener, PreUpdateEventListener {
    @Override
    public void onPersist(PersistEvent event) throws HibernateException {
        log.info("onPersist event:{}", event);
    }

    @Override
    public void onPersist(PersistEvent event, Map createdAlready) throws HibernateException {
        log.info("onPersist event:{},createdAlready:{}", event, createdAlready);
    }

    @Override
    public void onMerge(MergeEvent event) throws HibernateException {
        log.info("onMerge event:{}", event);
    }

    @Override
    public void onMerge(MergeEvent event, Map copiedAlready) throws HibernateException {
        log.info("onMerge event:{},copiedAlready:{}", event, copiedAlready);
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        log.info("onPreInsert event:{}", event);
        return false;
    }

    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        log.info("onPreUpdate event:{}", event);
        return false;
    }

    @Override
    public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
        log.info("onSaveOrUpdate event:{}", event);
    }
}
