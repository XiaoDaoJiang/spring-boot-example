package com.xiaodao.office.excel;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.xiaodao.office.excel.dto.Address;
import com.xiaodao.office.excel.dto.Order;
import com.xiaodao.office.excel.dto.User;
import com.xiaodao.office.excel.style.MyCellStyleStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * @author jianghaitao
 * @Classname ComplexDTOWriterTest
 * @Version 1.0.0
 * @Date 2024-10-16 10:21
 * @Created by jianghaitao
 */
public class ComplexDTOWriterTest {


    @Test
    public void testWrite() {
        User user = new User();
        user.setUserId(0L);
        user.setUserName("jianghaitao");
        user.setAddresses(ListUtil.of(createRandomAddresses(1), createRandomAddresses(2), createRandomAddresses(3)));
        user.setOrders(ListUtil.of(createRandomOrders(1), createRandomOrders(2), createRandomOrders(3)));


        EasyExcel.write("complexWrite.xlsx", User.class)
                .sheet("用户数据")
                // 自定义样式策略
                .registerWriteHandler(MyCellStyleStrategy.createCellStyleStrategy())
                // 自适应列宽
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(ListUtil.of(user));
    }

    private Address createRandomAddresses(int i) {
        Address address = new Address();
        address.setCity("Shanghai");
        address.setStreet("Pudong" + i + "th Street");
        return address;
    }

    private Order createRandomOrders(int i) {
        Order order = new Order();
        order.setOrderNo("202410160" + i);
        order.setAmount(new BigDecimal("100.00").add(new BigDecimal(i)));
        return order;
    }

}
