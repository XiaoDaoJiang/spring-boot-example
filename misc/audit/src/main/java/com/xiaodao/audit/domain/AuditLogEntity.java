package com.xiaodao.audit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity(name = "audit_log")
public class AuditLogEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String newContent;

    private String oldContent;

    private String tableName;

    private String type;

    public AuditLogEntity(String tableName, String type) {
        this.tableName = tableName;
        this.type = type;
    }

    @PostUpdate
    public void postUpdate() {
        System.out.println("postUpdate" + this);
    }
}
