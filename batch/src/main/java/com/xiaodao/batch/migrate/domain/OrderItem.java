package com.xiaodao.batch.migrate.domain;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_order_item")
public class OrderItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 双向
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "customer_id", nullable = false,
    //         foreignKey = @ForeignKey(name = "FK_ORDER_CUSTOMER"))
    // private Customer customer;

    // 只保留单向关系
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

}