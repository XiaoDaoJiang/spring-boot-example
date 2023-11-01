package com.xiaodao.office.work.fake;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OutputDTO {
    @ExcelProperty(index = 0)
    private String index;

    @ExcelProperty(index = 1)
    private String name;

    @ExcelProperty(index = 2)
    private String time;

    @ExcelProperty(index = 3)
    private String unit;

    @ExcelProperty(index = 4)
    private String phone;


}
