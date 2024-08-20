package com.xiaodao.batch.migrate.support;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ReadListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.io.InputStream;
import java.util.List;

/**
 * @author jianghaitao
 * @Classname ExcelItemReader
 * @Version 1.0.0
 * @Date 2024-08-19 14:32
 * @Created by jianghaitao
 */
public class EasyExcelItemReader<T> implements ItemReader<T> {

    List<T> items;

    public EasyExcelItemReader(InputStream inputStream, Class<T> clazz, ReadListener<?> readListener) {
        this.items = EasyExcel.read(inputStream, clazz, readListener).sheet(0).doReadSync();
    }

    public T read() throws Exception, UnexpectedInputException,
            NonTransientResourceException, ParseException {
        if (!items.isEmpty()) {
            return items.remove(0);
        }
        return null;
    }
}
