package com.xiaodao.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {

    private String make;
    private int seatCount;
    private String type;

    private Owner owner;

}
