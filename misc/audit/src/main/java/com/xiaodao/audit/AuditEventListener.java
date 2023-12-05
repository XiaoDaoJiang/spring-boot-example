package com.xiaodao.audit;

import org.hibernate.HibernateException;
import org.hibernate.event.spi.*;

import java.util.Map;

public class AuditEventListener implements PersistEventListener, PreInsertEventListener, SaveOrUpdateEventListener,MergeEventListener, PreUpdateEventListener {
    @Override
    public void onPersist(PersistEvent event) throws HibernateException {

    }

    @Override
    public void onPersist(PersistEvent event, Map createdAlready) throws HibernateException {

    }

    @Override
    public void onMerge(MergeEvent event) throws HibernateException {

    }

    @Override
    public void onMerge(MergeEvent event, Map copiedAlready) throws HibernateException {

    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        return false;
    }

    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        return false;
    }

    @Override
    public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {

    }
}
