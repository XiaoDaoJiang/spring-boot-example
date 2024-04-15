package com.xiaodao.jta.entity.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "t_order",schema = "db_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "total")
    private BigDecimal total;

    // spring mvc 日期时间参数绑定转换
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // json 日期字符串反序列化转换
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}