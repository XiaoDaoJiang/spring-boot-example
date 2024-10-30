package com.xiaodao.office.excel.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.List;

@Data
public class User {
    @ExcelProperty("用户ID")
    private Long userId;

    @ColumnWidth(20)
    @ExcelProperty("用户名")
    private String userName;

    @ExcelIgnore
    private List<Address> addresses;

    @ExcelIgnore
    private List<Order> orders;

    // Getters and setters...
}




