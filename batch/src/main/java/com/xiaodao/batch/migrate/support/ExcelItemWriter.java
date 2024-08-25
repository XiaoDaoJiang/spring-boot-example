package com.xiaodao.batch.migrate.support;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.WriteFailedException;
import org.springframework.batch.item.WriterNotOpenException;
import org.springframework.batch.item.support.AbstractFileItemWriter;
import org.springframework.batch.item.util.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author jianghaitao
 * @Classname ExcelItemWriter
 * @Version 1.0.0
 * @Date 2024-08-22 10:35
 * @Created by jianghaitao
 */
@Slf4j
public class ExcelItemWriter<T> extends AbstractFileItemWriter<T> {

    private final Resource resource;

    private MyExcelOutputState outputState;

    private final Class<T> type;

    private String encoding = DEFAULT_CHARSET;


    public ExcelItemWriter(Resource resource, Class<T> type) {
        this.resource = resource;
        this.type = type;
        this.setExecutionContextName(ClassUtils.getShortName(ExcelItemWriter.class));
    }


    @Override
    public void open(ExecutionContext executionContext) {
        Assert.notNull(resource, "The resource must be set");

        if (!getOutputState().isInitialized()) {
            doOpen();
        }


    }

    private void doOpen() throws ItemStreamException {
        MyExcelOutputState outputState = getOutputState();

        try {
            outputState.initSheet();
        } catch (IOException e) {
            throw new ItemStreamException("Failed to initialize writer", e);
        }
    }

    @Override
    public void write(List<? extends T> items) {
        if (!getOutputState().isInitialized()) {
            throw new WriterNotOpenException("Writer must be open before it can be written to");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Writing to file with " + items.size() + " items.");
        }

        MyExcelOutputState state = getOutputState();

        try {
            state.write(items);
        } catch (Exception e) {
            throw new WriteFailedException("Could not write data. The file may be corrupt.", e);
        }
        state.setLinesWritten(state.getLinesWritten() + items.size());
    }

    @Override
    protected String doWrite(List<? extends T> items) {
        return "";
    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void close() {
        super.close();
        if (outputState != null) {
            outputState.excelWriter.close();
        }
    }

    @Override
    protected MyExcelOutputState getOutputState() {
        if (outputState == null) {
            File file;
            try {
                file = resource.getFile();
            } catch (IOException e) {
                throw new ItemStreamException("Could not convert resource to file: [" + resource + "]", e);
            }
            Assert.state(!file.exists() || file.canWrite(), "Resource is not writable: [" + resource + "]");
            outputState = new MyExcelOutputState();
            outputState.setDeleteIfExists(shouldDeleteIfExists);
            outputState.setAppendAllowed(append);
            outputState.setEncoding(encoding);
        }
        return outputState;
    }


    public class MyExcelOutputState extends AbstractFileItemWriter<T>.OutputState {

        private ExcelWriter excelWriter;

        private WriteSheet writeSheet;

        boolean initialized = false;

        public MyExcelOutputState() {
            super();
        }

        public void write(List<? extends T> lines) {
            // 这里是写入数据的逻辑
            excelWriter.write(lines, writeSheet);
        }

        public void initSheet() throws IOException {
            File file = resource.getFile();
            FileUtils.setUpOutputFile(file, false, append, shouldDeleteIfExists);

            this.excelWriter = EasyExcel.write(resource.getFile(), type).excelType(ExcelTypeEnum.XLSX).build();
            // 这里注意 如果同一个sheet只要创建一次
            this.writeSheet = EasyExcel.writerSheet(0).build();
            this.initialized = true;
        }

        public boolean isInitialized() {
            return this.initialized;
        }
    }


}
