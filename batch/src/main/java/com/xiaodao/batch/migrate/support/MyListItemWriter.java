package com.xiaodao.batch.migrate.support;

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
public class MyListItemWriter<T> extends AbstractItemStreamItemWriter<T> {

    private final List<T> validationResults = new ArrayList<>();


    public static final String VALID_RESULTS = "VALID_RESULTS";

    @Override
    public void write(List<? extends T> items) {
        validationResults.addAll(items);
    }

    @Override
    public void open(ExecutionContext executionContext) {
        if (!executionContext.containsKey(VALID_RESULTS)) {
            executionContext.put(VALID_RESULTS, validationResults);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) {

    }
}
