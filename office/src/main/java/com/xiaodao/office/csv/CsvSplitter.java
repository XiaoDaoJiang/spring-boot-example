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

    public final Set<String> SPLIT_TABLE_NAME = Sets.newHashSet("PERSON_INFO", "PERSON_ATTACH");


    public void read() {
        CsvReader reader = CsvUtil.getReader();
        // 从文件中读取CSV数据
        final String path = "C:\\Users\\xiaodao\\Downloads\\ree2.csv";
        CsvData data = reader.read(FileUtil.file(path));
        List<CsvRow> rows = data.getRows();
        // 遍历行
        List<List<String>> aTableData = new ArrayList<>(100);
        String tableName = null;
        for (CsvRow csvRow : rows) {
            List<String> rawList = csvRow.getRawList();
            final String curTableName = csvRow.get(0);
            if (tableName == null) {
                tableName = StrUtil.removeAll(curTableName,'\uFEFF');
            }
            // 换下张表
            else if (!tableName.equals(curTableName)) {
                this.write(aTableData, tableName);
                aTableData.clear();
                System.out.println("============================================");
                tableName = curTableName;
            } else {
                rawList = rawList.stream().map(it -> StrUtil.unWrap(it, '\'')).collect(Collectors.toList());
            }

            Console.log(rawList);
            aTableData.add(rawList);

        }

        if (!aTableData.isEmpty()) {
            this.write(aTableData, tableName);
            aTableData.clear();
            System.out.println("============================================");
        }

    }

    public void write(List<List<String>> aTaleData, String tableName) {
        // 指定路径和编码
        CsvWriter writer = CsvUtil.getWriter("C:\\Users\\xiaodao\\Downloads\\" + tableName + ".csv", CharsetUtil.CHARSET_GBK);
        // 按行写出
        writer.write(aTaleData);
    }

    public static void main(String[] args) {
        new CsvSplitter().read();
    }
}
