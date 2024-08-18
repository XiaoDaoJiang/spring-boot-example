package com.xiaodao.batch.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    private String make;

    private int numberOfSeats;

    private CarType type;

    private Person owner;

    private Employee employee;

}
