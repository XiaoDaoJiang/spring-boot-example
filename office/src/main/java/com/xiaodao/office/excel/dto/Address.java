package com.xiaodao.office.excel.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Address {
    @ExcelProperty("城市")
    private String city;

    @ExcelProperty("街道")
    private String street;

    // Getters and setters...
}