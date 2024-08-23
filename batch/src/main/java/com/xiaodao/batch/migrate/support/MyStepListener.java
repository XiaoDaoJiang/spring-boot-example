package com.xiaodao.batch.migrate.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;

import java.util.List;

/**
 * @author jianghaitao
 * @Classname StepListener
 * @Version 1.0.0
 * @Date 2024-08-22 11:10
 * @Created by jianghaitao
 */
@Slf4j
public class MyStepListener<T,S> extends StepListenerSupport<T,S> {


    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("execute ExitStatus");
        return super.afterStep(stepExecution);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("execute beforeStep");
        super.beforeStep(stepExecution);
    }

    @Override
    public void afterChunk(ChunkContext context) {
        log.info("execute afterChunk");
        super.afterChunk(context);
    }

    @Override
    public void beforeChunk(ChunkContext context) {
        log.info("execute beforeChunk");
        super.beforeChunk(context);
    }

    @Override
    public void afterRead(T item) {
        log.info("execute afterRead");
        super.afterRead(item);
    }

    @Override
    public void beforeRead() {
        log.info("execute beforeRead");
        super.beforeRead();
    }

    @Override
    public void onReadError(Exception ex) {
        log.info("execute onReadError");
        super.onReadError(ex);
    }

    @Override
    public void afterWrite(List<? extends S> items) {
        log.info("execute afterWrite");
        super.afterWrite(items);
    }

    @Override
    public void beforeWrite(List<? extends S> items) {
        log.info("execute beforeWrite");
        super.beforeWrite(items);
    }

    @Override
    public void onWriteError(Exception exception, List<? extends S> items) {
        log.info("execute onWriteError");
        super.onWriteError(exception, items);
    }

    @Override
    public void afterProcess(T item, S result) {
        log.info("execute afterProcess");
        super.afterProcess(item, result);
    }

    @Override
    public void beforeProcess(T item) {
        log.info("execute beforeProcess");
        super.beforeProcess(item);
    }

    @Override
    public void onProcessError(T item, Exception e) {
        log.info("execute onProcessError");
        super.onProcessError(item, e);
    }

    @Override
    public void onSkipInProcess(T item, Throwable t) {
        log.info("execute onSkipInProcess");
        super.onSkipInProcess(item, t);
    }

    @Override
    public void onSkipInRead(Throwable t) {
        log.info("execute onSkipInRead");
        super.onSkipInRead(t);
    }

    @Override
    public void onSkipInWrite(S item, Throwable t) {
        log.info("execute onSkipInWrite");
        super.onSkipInWrite(item, t);
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        log.info("execute afterChunkError");
        super.afterChunkError(context);
    }
}
