package com.xiaodao.audit.service;

import com.xiaodao.audit.domain.AuditLogEntity;
import com.xiaodao.audit.domain.AuditLogEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
public class AuditLogService {

    private final AuditLogEntityRepository auditLogEntityRepository;

    public AuditLogService(AuditLogEntityRepository auditLogEntityRepository) {
        this.auditLogEntityRepository = auditLogEntityRepository;
    }


    @Transactional(rollbackFor = Exception.class)
    public void createOperateLog(String entityName, String operation) {
        log.info("tx createOperateLog ：{}", TransactionSynchronizationManager.getCurrentTransactionName());
        int i = 1 / 0;
        auditLogEntityRepository.save(new AuditLogEntity(entityName, operation));
    }

    @Transactional
    public void updateOperateLog(AuditLogEntity auditLogEntity) {
        log.info("tx updateOperateLog ：{}", TransactionSynchronizationManager.getCurrentTransactionName());
        auditLogEntityRepository.save(auditLogEntity);
    }

}
