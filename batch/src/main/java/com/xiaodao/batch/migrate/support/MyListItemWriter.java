package com.xiaodao.batch.migrate.support;

import com.xiaodao.batch.migrate.domain.ValidationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaodaojiang
 * @Classname MyListItemWriter
 * @Version 1.0.0
 * @Date 2024-08-25 15:58
 * @Created by xiaodaojiang
 */
@Slf4j
public class MyListItemWriter<T extends ValidationDTO> extends AbstractItemStreamItemWriter<T> {

    private final ItemStreamWriter<T> delegate;


    // @Value("#{stepExecution}")
    // private StepExecution stepExecution;

    private boolean isError;

    private final List<T> tempItems = new ArrayList<>();

    public MyListItemWriter(ItemStreamWriter<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void open(ExecutionContext executionContext) {
        log.info("open..........");
        delegate.open(executionContext);
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        log.info("write..........");
        if (isError) {
            if (!tempItems.isEmpty()) {
                delegate.write(tempItems);
                tempItems.clear();
            }

            delegate.write(items);
            return;
        }
        tempItems.addAll(items);
    }

    /**
     * after open/write
     */
    @Override
    public void update(ExecutionContext executionContext) {
        log.info("update..........");

        if (!isError) {
            final ValidationRetainingItemProcessor.ValidationContext validationContext = (ValidationRetainingItemProcessor.ValidationContext) executionContext.get(ValidationRetainingItemProcessor.VALIDATION_CONTEXT_KEY);
            if (validationContext != null && validationContext.getInvalidCount() > 0) {
                isError = true;
            }
        }

        super.update(executionContext);
    }

    @Override
    public void close() {
        log.info("close..........");
        super.close();
        delegate.close();
    }
}
