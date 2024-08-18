package com.xiaodao.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person extends BaseEntity {

    private String firstName;
    private String lastName;
    private String address;
    private Long age;


}
