package com.xiaodao.batch.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto implements Owner {

    private Long id;
    private String name;
    private String address;

    private Long age;

    @Override
    public Long getOwnerId() {
        return id;
    }
}
