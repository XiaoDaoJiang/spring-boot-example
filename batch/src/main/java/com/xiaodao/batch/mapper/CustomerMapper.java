/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package com.xiaodao.batch.mapper;

import com.xiaodao.batch.dto.Customer;
import com.xiaodao.batch.dto.CustomerDto;
import com.xiaodao.batch.dto.NoGetterSetterCustomer;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { OrderItemMapper.class })
public interface CustomerMapper {

    CustomerMapper MAPPER = Mappers.getMapper( CustomerMapper.class );

    @Mapping(source = "orders", target = "orderItems")
    @Mapping(source = "customerName", target = "name")
    Customer toCustomer(CustomerDto customerDto);

    @Mapping(source = "orders", target = "orderItems")
    @Mapping(source = "customerName", target = "name")
    NoGetterSetterCustomer toNoSetterCustomer(CustomerDto customerDto);

    @InheritInverseConfiguration
    CustomerDto fromCustomer(Customer customer);
}