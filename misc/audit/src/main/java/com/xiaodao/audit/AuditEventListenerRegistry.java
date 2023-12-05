package com.xiaodao.audit;

import org.hibernate.event.service.internal.EventListenerRegistryImpl;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;

@Service
public class AuditEventListenerRegistry implements InitializingBean{

    public final EntityManagerFactory entityManagerFactory;

    public AuditEventListenerRegistry(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }


    @Override
    public void afterPropertiesSet() {
        final SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        final EventListenerRegistryImpl eventListenerRegistry = sessionFactory.getServiceRegistry().getService(EventListenerRegistryImpl.class);

        eventListenerRegistry.appendListeners(EventType.PERSIST, AuditEventListener.class);
        eventListenerRegistry.appendListeners(EventType.PRE_INSERT, AuditEventListener.class);
        eventListenerRegistry.appendListeners(EventType.SAVE, AuditEventListener.class);

        eventListenerRegistry.appendListeners(EventType.SAVE_UPDATE, AuditEventListener.class);
        eventListenerRegistry.appendListeners(EventType.UPDATE, AuditEventListener.class);
        eventListenerRegistry.appendListeners(EventType.PRE_UPDATE, AuditEventListener.class);
        eventListenerRegistry.appendListeners(EventType.MERGE, AuditEventListener.class);

    }
}
