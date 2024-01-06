package com.xiaodao.audit;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;

@Service
public class AuditEventListenerRegistry implements InitializingBean{

    public final EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private AuditEventListener auditEventListener;

    @Autowired
    private PostEventListener postEventListener;

    @Autowired
    private PostCommitEventListener postCommitEventListener;

    public AuditEventListenerRegistry(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }


    @Override
    public void afterPropertiesSet() {
        final SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        final EventListenerRegistry eventListenerRegistry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);

        eventListenerRegistry.appendListeners(EventType.PERSIST, auditEventListener);
        eventListenerRegistry.appendListeners(EventType.PRE_INSERT, auditEventListener);
        eventListenerRegistry.appendListeners(EventType.SAVE, auditEventListener);

        eventListenerRegistry.appendListeners(EventType.SAVE_UPDATE, auditEventListener);
        eventListenerRegistry.appendListeners(EventType.UPDATE, auditEventListener);
        eventListenerRegistry.appendListeners(EventType.PRE_UPDATE, auditEventListener);
        eventListenerRegistry.appendListeners(EventType.MERGE, auditEventListener);


        eventListenerRegistry.appendListeners(EventType.POST_UPDATE, postEventListener);
        eventListenerRegistry.appendListeners(EventType.POST_DELETE, postEventListener);
        eventListenerRegistry.appendListeners(EventType.POST_COMMIT_DELETE, postCommitEventListener);

    }
}
