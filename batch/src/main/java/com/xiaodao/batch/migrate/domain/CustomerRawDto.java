package com.xiaodao.batch.migrate.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author jianghaitao
 * @Classname CustomerRawDto
 * @Version 1.0.0
 * @Date 2024-08-19 15:18
 * @Created by jianghaitao
 */
@Data
public class CustomerRawDto {

    @ExcelProperty("顾客")
    private String customerName;

    @ExcelProperty("订单子项名")
    private String name;

    @ExcelProperty("单个订单购买数")
    private Long quantity;

    @ExcelProperty("库存数量")
    private long stockNumber;

}
