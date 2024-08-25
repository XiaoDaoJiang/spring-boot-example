package com.xiaodao.batch.migrate.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author jianghaitao
 * @Classname CustomerRawDto
 * @Version 1.0.0
 * @Date 2024-08-19 15:18
 * @Created by jianghaitao
 */
@Data
public class CustomerRawDto extends ValidationDTO {

    @NotBlank
    @ExcelProperty("顾客")
    private String customerName;

    @NotBlank
    @ExcelProperty("订单子项名")
    private String name;

    @NotNull
    @Max(value = 200)
    @Min(value = 1)
    @ExcelProperty("单个订单购买数")
    private Long quantity;

    @Min(value = 1)
    @ExcelProperty("库存数量")
    private long stockNumber;

}
