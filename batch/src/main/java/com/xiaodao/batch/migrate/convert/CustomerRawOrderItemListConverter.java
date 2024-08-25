package com.xiaodao.batch.migrate.convert;

import com.xiaodao.batch.extension.MapperSpringConfig;
import com.xiaodao.batch.migrate.domain.CustomerRawDto;
import com.xiaodao.batch.migrate.domain.OrderItemDto;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * @author jianghaitao
 * @Classname CustomerConverter
 * @Version 1.0.0
 * @Date 2024-08-19 14:18
 * @Created by jianghaitao
 */
@Mapper(config = MapperSpringConfig.class)
public interface CustomerRawOrderItemListConverter extends Converter<CustomerRawDto, List<OrderItemDto>> {

    default List<OrderItemDto> convert(CustomerRawDto source) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setName(source.getName());
        orderItemDto.setQuantity(source.getQuantity());
        return List.of(orderItemDto);
    }
}
