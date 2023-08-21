package com.xiaodao.common.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_account", nullable = false, length = 100)
    private String userAccount;

    @Column(name = "registed_date", nullable = false)
    private Instant registerDate;

    @Column(name = "role", nullable = false, length = 50)
    private String role;

}