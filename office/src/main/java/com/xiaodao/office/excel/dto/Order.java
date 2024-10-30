package com.xiaodao.office.excel.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {
    @ExcelProperty("订单号")
    private String orderNo;

    @ExcelProperty("订单金额")
    private BigDecimal amount;

    // Getters and setters...
}