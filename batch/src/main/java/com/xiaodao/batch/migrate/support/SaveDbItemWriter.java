package com.xiaodao.batch.migrate.support;

import com.xiaodao.batch.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * @author xiaodaojiang
 * @Classname SaveDbItemWriter
 * @Version 1.0.0
 * @Date 2024-08-25 20:16
 * @Created by xiaodaojiang
 */
@Slf4j
public class SaveDbItemWriter<T> implements ItemWriter<T> {

    private final CustomerService customerService;

    public SaveDbItemWriter(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        // save to db
        log.info("save to db:{}", items);
        customerService.saveAll(items);
    }
}
