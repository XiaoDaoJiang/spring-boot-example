package com.xiaodao.office.csv;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.text.csv.*;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.compress.utils.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CsvSplitter {

    public final Set<String> SPLIT_TABLE_NAME = Sets.newHashSet("DIC_ITEM",
            "DIC_TYPE",
            "PERSON_INFO",
            "PERSON");


    public void read(String schema) {
        CsvReader reader = CsvUtil.getReader();
        // 从文件中读取CSV数据
        final String path = "C:\\Users\\xiaodaojiang\\Desktop\\uat2.csv";
        CsvData data = reader.read(FileUtil.file(path));
        List<CsvRow> rows = data.getRows();
        // 遍历行
        List<List<String>> aTableData = new ArrayList<>(100);
        String tableName = null;
        for (CsvRow csvRow : rows) {
            final String curTableName = csvRow.get(0);
            List<String> rawList = csvRow.getRawList().stream().skip(1).collect(Collectors.toList());
            if (tableName == null) {
                tableName = StrUtil.removeAll(curTableName, '\uFEFF');
                tableName = StrUtil.unWrap(tableName, '\"');
            }
            // 换下张表
            else if (!tableName.equals(curTableName)) {
                this.write(aTableData, tableName, schema);
                aTableData.clear();
                System.out.println("============================================");
                tableName = curTableName;
                rawList = rawList.stream().map(it -> StrUtil.unWrap(it, '\"')).collect(Collectors.toList());
            } else {
                rawList = rawList.stream().map(it -> StrUtil.unWrap(it, '\'')).collect(Collectors.toList());
            }

            Console.log(rawList);
            aTableData.add(rawList);

        }

        if (!aTableData.isEmpty()) {
            this.write(aTableData, tableName, schema);
            aTableData.clear();
            System.out.println("============================================");
        }

    }

    public void write(List<List<String>> aTaleData, String tableName, String schema) {
        tableName = StrUtil.unWrap(tableName, '\"');
        tableName = StrUtil.unWrap(tableName, '\'');
        // 指定路径和编码
        CsvWriter writer = CsvUtil.getWriter("测试数据\\" + schema + "\\" + tableName + ".csv",
                CharsetUtil.systemCharset(), false);
        // 按行写出
        writer.write(aTaleData);
    }

    public static void main(String[] args) {
        new CsvSplitter().read("xxx_TEST_UAT");
    }
}
