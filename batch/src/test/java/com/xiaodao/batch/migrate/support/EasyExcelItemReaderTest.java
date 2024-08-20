package com.xiaodao.batch.migrate.support;

import com.xiaodao.batch.migrate.domain.CustomerRawDto;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author jianghaitao
 * @Classname EasyExcelItemReaderTest
 * @Version 1.0.0
 * @Date 2024-08-19 16:09
 * @Created by jianghaitao
 */
class EasyExcelItemReaderTest {

    @Test
    void read() throws Exception {
        final Resource resource = new ClassPathResource("cutomerOrders.xlsx");
        final EasyExcelItemReader<CustomerRawDto> easyExcelItemReader
                = new EasyExcelItemReader<>(
                resource.getInputStream(), CustomerRawDto.class, null
        );
        System.out.println(easyExcelItemReader.read());
        assertNull(easyExcelItemReader.read());
    }

}