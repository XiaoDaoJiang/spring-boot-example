package com.xiaodao.office.excel.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.poi.BorderStyleEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ContentRowHeight(26)
@ContentFontStyle(fontName = "仿宋_GB2312", fontHeightInPoints = 9)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER, borderLeft = BorderStyleEnum.THIN, borderRight = BorderStyleEnum.THIN, borderBottom = BorderStyleEnum.THIN, borderTop = BorderStyleEnum.THIN)
public class UploadData {

    @ExcelProperty(value = "*姓名", index = 0)
    private String name;


    @ExcelProperty(value = "性别", index = 1)
    private String sex;

    @ExcelProperty(value = "*身份证号码", index = 2)
    private String idCard;

    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "出生年月", index = 3)
    private String birthday;

    @ExcelProperty(value = "现工作单位及职务", index = 4)
    private String org;


    @ExcelProperty(value = "备注", index = 5)
    private String remark;

    @ExcelProperty(value = "错误信息", index = 6)
    private String errorMsg;

    public UploadData(String name, String sex, String idCard, String birthday, String org) {
        this.name = name;
        this.sex = sex;
        this.idCard = idCard;
        this.birthday = birthday;
        this.org = org;
    }
}
