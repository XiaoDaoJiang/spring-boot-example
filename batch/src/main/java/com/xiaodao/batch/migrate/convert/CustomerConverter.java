package com.xiaodao.batch.migrate.convert;

import com.xiaodao.batch.extension.MapperSpringConfig;
import com.xiaodao.batch.migrate.domain.Customer;
import com.xiaodao.batch.migrate.domain.CustomerDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

/**
 * @author jianghaitao
 * @Classname CustomerConverter
 * @Version 1.0.0
 * @Date 2024-08-19 14:18
 * @Created by jianghaitao
 */
@Mapper(config = MapperSpringConfig.class)
public interface CustomerConverter extends Converter<Customer, CustomerDto> {

    @Mapping(target = "orders", source = "orderItems")
    @Mapping(target = "customerName", source = "name")
    CustomerDto convert(Customer customer);

    @InheritInverseConfiguration
    @DelegatingConverter
    Customer invertConvert(CustomerDto customerDto);
}
