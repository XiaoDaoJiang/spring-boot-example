package com.xiaodao.validation;

import com.xiaodao.validation.first.Bar;
import com.xiaodao.validation.first.Car;
import com.xiaodao.validation.first.Foor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Builder
@Data
public class User {
    private String name;
    private String email;
    private int age;

    private String sex;

    private String dateOfBirth;


    private Car car;

    private Foor foor;

    @Singular
    List<Bar> bars;

    // Getters and Setters
}
