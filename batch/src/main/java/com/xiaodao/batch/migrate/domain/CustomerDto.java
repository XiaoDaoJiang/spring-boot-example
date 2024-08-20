package com.xiaodao.batch.migrate.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class CustomerDto {

    private Long id;

    private String customerName;

    private List<OrderItemDto> orders;

    private Map<OrderItemKeyDto, OrderItemDto> stock;


}