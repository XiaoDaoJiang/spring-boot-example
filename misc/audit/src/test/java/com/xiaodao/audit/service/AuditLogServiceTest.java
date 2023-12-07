package com.xiaodao.audit.service;

import com.xiaodao.audit.domain.AuditLogEntity;
import com.xiaodao.audit.domain.AuditLogEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuditLogServiceTest {

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private AuditLogEntityRepository auditLogEntityRepository;

    @Test
    void updateOperateLog() {
        final List<AuditLogEntity> all = auditLogEntityRepository.findAll();
        final AuditLogEntity auditLogEntity = all.get(0);
        auditLogEntity.setModifyBy(102L);
        auditLogService.updateOperateLog(auditLogEntity);
    }
}