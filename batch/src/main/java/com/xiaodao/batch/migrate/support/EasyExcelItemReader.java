package com.xiaodao.batch.migrate.support;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ReadListener;
import org.springframework.batch.item.*;
import org.springframework.core.io.Resource;

import java.io.IOException;
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

    private ReadListener<T> readListener;

    public EasyExcelItemReader(Resource resource, Class<T> clazz, ReadListener<T> readListener) throws IOException {
        this.readListener = readListener;
        EasyExcel.read(resource.getInputStream(), clazz, readListener).sheet(0).doRead();
    }

    public T read() throws Exception, UnexpectedInputException,
            NonTransientResourceException, ParseException {
        if (!items.isEmpty()) {
            return items.remove(0);
        }
        return null;
    }
}
