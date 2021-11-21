package com.xiaodao.office.excel.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "person", indexes = {@Index(name = "idx_person_idcard", columnList = "id_card")})
public class Person extends MappedAuditableBase{
    /*@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;*/
    @Id
    @GeneratedValue(generator = "UUIDGenerator")
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "name", nullable = false,length = 20)
    private String name;

    @Column(name = "sex", nullable = false,length = 1)
    private String sex;

    @Column(name = "id_card", nullable = false,length = 30)
    private String idCard;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "org", nullable = false,length = 50)
    private String org;
}

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
class MappedAuditableBase {
    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime modified;
}