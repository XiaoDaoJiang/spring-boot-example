package com.xiaodao.batch.migrate.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItemDto {

    private String name;

    private Long quantity;

}