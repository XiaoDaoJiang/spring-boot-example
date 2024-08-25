package com.xiaodao.batch.migrate.support;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.xiaodao.batch.migrate.domain.CustomerRawDto;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.util.List;

/**
 * @author xiaodaojiang
 * @Classname ExcelItemWriterTest
 * @Version 1.0.0
 * @Date 2024-08-25 18:03
 * @Created by xiaodaojiang
 */
class ExcelItemWriterTest {


    @Test
    void write() {
        Resource resource = new FileSystemResource("customerRawDto_output.xlsx");
        final ExcelItemWriter<CustomerRawDto> customerRawDtoExcelItemWriter = new ExcelItemWriter<>(resource, CustomerRawDto.class);
        customerRawDtoExcelItemWriter.open(null);

        final CustomerRawDto rawDto = new CustomerRawDto();
        rawDto.setCustomerName("dfsfds");
        rawDto.setName("fsdfs");
        rawDto.setQuantity(2L);
        rawDto.setStockNumber(23);
        rawDto.setErrorMsg("3424324");

        List<? extends CustomerRawDto> items = List.of(rawDto, rawDto);
        customerRawDtoExcelItemWriter.write(items);

    }


    @Test
    public void test() {
        final CustomerRawDto rawDto = new CustomerRawDto();
        rawDto.setCustomerName("dfsfds");
        rawDto.setName("fsdfs");
        rawDto.setQuantity(2L);
        rawDto.setStockNumber(23);
        rawDto.setErrorMsg("3424324");

        List<? extends CustomerRawDto> items = List.of(rawDto, rawDto);

        // 这里 需要指定写用哪个class去写
        ExcelWriter excelWriter = EasyExcel.write("customerRawDto_output.xlsx", CustomerRawDto.class).build();
        // 这里注意 如果同一个sheet只要创建一次
        WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
        // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来
        for (int i = 0; i < 5; i++) {
            // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
            excelWriter.write(items, writeSheet);
        }

        excelWriter.finish();
    }

}