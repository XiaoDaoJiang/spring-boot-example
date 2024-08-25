package com.xiaodao.batch.migrate.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xiaodaojiang
 * @Classname InvalidDto
 * @Version 1.0.0
 * @Date 2024-08-25 16:12
 * @Created by xiaodaojiang
 */
@Setter
@Getter
public class ValidationDTO {

    @ExcelProperty(value = "错误信息")
    public String errorMsg;
}
