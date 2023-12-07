package com.xiaodao.audit.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogEntityRepository extends JpaRepository<AuditLogEntity, Long> {
}