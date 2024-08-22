package com.xiaodao.batch.migrate.support;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaodaojiang
 * @Classname MyListItemWriter
 * @Version 1.0.0
 * @Date 2024-08-21 23:50
 * @Created by xiaodaojiang
 */
public class ExcelItemWriter<T> extends AbstractItemStreamItemWriter<T> {

    private boolean existErrorItem = false;

    private final List<T> validationResults = new ArrayList<>();


    public static final String VALID_RESULTS = "VALID_RESULTS";


    @Override
    public void open(ExecutionContext executionContext) {

    }


    @Override
    public void write(List<? extends T> items) throws Exception {
        validationResults.addAll(items);
    }

    /**
     * execute after write and first open
     */
    @Override
    public void update(ExecutionContext executionContext) {

    }

    @Override
    public void close() {
        super.close();
    }


}
