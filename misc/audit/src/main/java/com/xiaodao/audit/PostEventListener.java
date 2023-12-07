package com.xiaodao.audit;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;

/**
 * 当发送给数据库的操作完成后，会触发 PostActionEventListener 回调方法
 * onPostInsert/onPostUpdate/onPostDelete
 * 若 requiresPostCommitHanding 返回 true 时，会根据事务是否成功提交
 * 所有 PostActionEventListener 回调方法都会执行两次，一次是业务操作发送给数据库，一次是完成事务后
 *
 */
@Slf4j
@Component
public class PostEventListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {


    /**
     * 可能会调用两次，一次是业务操作发送删除给数据库，一次是完成删除事务后
     * 如果是情况1：由于删除后，事务还未提交，所以此处记录审计日志异常会影响到业务操作事务的正常提交
     */
    @Override
    public void onPostDelete(PostDeleteEvent event) {
        log.info("onPostDelete event:{}", event);
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        log.info("onPostInsert event:{}", event);
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        log.info("onPostUpdate event:{}", event);
    }


    /**
     * 是否需要在事务提交后处理，将业务操作事务和审计日志记录隔离
     */
    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        log.info("requiresPostCommitHanding persister:{}", true);
        return true;
    }

}
