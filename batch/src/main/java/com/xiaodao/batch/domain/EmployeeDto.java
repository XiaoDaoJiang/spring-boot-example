package com.xiaodao.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto extends PersonDto {

    private Long employeeId;

    @Override
    public Long getOwnerId() {
        return this.employeeId;
    }
}
