package com.xiaodao.audit.spi;

import com.xiaodao.audit.PostEventListener;
import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuditIntegrator implements Integrator {


    @Autowired
    private PostEventListener postEventListener;

    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        EventListenerRegistry eventListenerRegistry = serviceRegistry.getService( EventListenerRegistry.class );
        eventListenerRegistry.appendListeners( EventType.POST_INSERT, postEventListener );
        eventListenerRegistry.appendListeners( EventType.POST_DELETE, postEventListener );
        eventListenerRegistry.appendListeners( EventType.POST_UPDATE, postEventListener );
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {

    }
}
