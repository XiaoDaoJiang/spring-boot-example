package com.xiaodao.batch.migrate.convert;

import com.xiaodao.batch.extension.MapperSpringConfig;
import com.xiaodao.batch.migrate.domain.CustomerDto;
import com.xiaodao.batch.migrate.domain.CustomerRawDto;
import com.xiaodao.batch.migrate.domain.OrderItemDto;
import com.xiaodao.batch.migrate.domain.OrderItemKeyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

import java.util.Map;

/**
 * @author jianghaitao
 * @Classname CustomerConverter
 * @Version 1.0.0
 * @Date 2024-08-19 14:18
 * @Created by jianghaitao
 */
// 非 直接使用 convert 方法的转换器不能通过 uses = {ConversionServiceAdapter.class} 自动注入
//  要么直接 uses = {CustomerRawOrderItemListConverter.class}
// 要么直接在当前转换器中使用定义对象的转换方法
// @Mapper(config = MapperSpringConfig.class, uses = {ConversionServiceAdapter.class})
@Mapper(config = MapperSpringConfig.class, uses = {CustomerRawOrderItemListConverter.class})
public interface CustomerRawConverter extends Converter<CustomerRawDto, CustomerDto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerName", source = "customerName")
    @Mapping(target = "orders", source = "source")
    @Mapping(target = "stock", source = "source")
    CustomerDto convert(CustomerRawDto source);

    /**
     * 自定义转换
     */
    /* default List<OrderItemDto> map(CustomerRawDto source) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setName(source.getName());
        orderItemDto.setQuantity(source.getQuantity());
        return List.of(orderItemDto);
    } */
    default Map<OrderItemKeyDto, OrderItemDto> map(CustomerRawDto source) {
        return Map.of(new OrderItemKeyDto(source.getStockNumber()),
                new OrderItemDto(source.getName(), source.getQuantity()));
    }

}
