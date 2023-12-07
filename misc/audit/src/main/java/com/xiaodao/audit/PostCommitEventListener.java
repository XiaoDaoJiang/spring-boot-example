package com.xiaodao.audit;

import com.xiaodao.audit.service.AuditLogService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * PostCommitXXXEventListener + requiresPostCommitHanding 可以实现在事务提交后处理
 * 但是还是建议捕获异常，避免打印已提交无法回滚的异常日志造成困扰
 * 如：Caused by: org.hibernate.HibernateException: Unable to perform afterTransactionCompletion callback: *
 */
@Slf4j
@Component
public class PostCommitEventListener implements PostCommitDeleteEventListener {

    @Autowired
    private AuditLogService auditLogService;


    /**
     * 可能会调用两次，一次是业务操作发送删除给数据库，一次是完成删除事务后
     * 如果是情况1：由于删除后，事务还未提交，所以此处记录审计日志异常会影响到业务操作事务的正常提交
     */
    @Override
    public void onPostDelete(PostDeleteEvent event) {
        log.info("onPostDelete commit success event:{}", event);
        auditLogService.createOperateLog(event.getPersister().getEntityName(), "delete");
    }


    /**
     * 是否需要在事务提交后处理，将业务操作事务和审计日志记录隔离
     */
    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        log.info("requiresPostCommitHanding persister:{}", true);
        return true;
    }

    @Override
    public void onPostDeleteCommitFailed(PostDeleteEvent event) {
        log.info("onPostDeleteCommitFailed event:{}", event);
    }
}
