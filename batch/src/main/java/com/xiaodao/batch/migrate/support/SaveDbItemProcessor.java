package com.xiaodao.batch.migrate.support;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.core.convert.ConversionService;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author xiaodaojiang
 * @Classname SaveDbItemProcessor
 * @Version 1.0.0
 * @Date 2024-08-25 20:16
 * @Created by xiaodaojiang
 */
public class SaveDbItemProcessor<T, O> implements ItemProcessor<T, O> {

    private ConversionService conversionService;

    private Class<O> outputType;

    public SaveDbItemProcessor(ConversionService conversionService, Class<O> outputType) {
        this.conversionService = conversionService;
        this.outputType = outputType;
    }

    @Override
    public O process(T item) throws Exception {
        return conversionService.convert(item, outputType);
    }
}
