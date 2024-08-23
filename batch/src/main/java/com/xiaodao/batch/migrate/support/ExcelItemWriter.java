package com.xiaodao.batch.migrate.support;

import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

import java.util.List;

/**
 * @author jianghaitao
 * @Classname ExcelItemWriter
 * @Version 1.0.0
 * @Date 2024-08-22 10:35
 * @Created by jianghaitao
 */
public class ExcelItemWriter <T> extends AbstractItemStreamItemWriter<T> {



    @Override
    public void write(List<? extends T> items) throws Exception {

    }


}
