package com.xiaodao.office.excel.style;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * @author jianghaitao
 * @Classname MyCellStyleStrategy
 * @Version 1.0.0
 * @Date 2024-10-16 11:12
 * @Created by jianghaitao
 */
public class MyCellStyleStrategy {

    public static HorizontalCellStyleStrategy createCellStyleStrategy() {
        // 头部样式定义
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        // headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        // headWriteCellStyle.setBorderRight(BorderStyle.THIN);
        // headWriteCellStyle.setBorderTop(BorderStyle.THIN);
        // headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        headWriteCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        // 设置字体样式
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 12);
        headWriteFont.setBold(true);
        headWriteFont.setFontName("宋体");
        headWriteCellStyle.setWriteFont(headWriteFont);

        // 内容样式定义
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 背景
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());

        // 设置边框样式
        // contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        // contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        // contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        // contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);

        // 设置字体样式
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 11);
        contentWriteFont.setFontName("宋体");
        contentWriteCellStyle.setWriteFont(contentWriteFont);

        // 创建单元格样式策略
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }
}
