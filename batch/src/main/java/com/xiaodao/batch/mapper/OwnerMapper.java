package com.xiaodao.batch.mapper;

import com.xiaodao.batch.domain.*;
import org.mapstruct.TargetType;

/**
 * @author xiaodaojiang
 * @Classname OwnerMapper
 * @Version 1.0.0
 * @Date 2024-08-17 22:42
 * @Created by xiaodaojiang
 */
public class OwnerMapper {

    public <T extends BaseEntity> T getOwner(Owner owner, @TargetType Class<T> ownerClass) {
        if (ownerClass.isAssignableFrom(Person.class) && owner instanceof PersonDto) {
            final PersonDto personDto = (PersonDto) owner;
            final Person person = new Person(personDto.getName(), null, personDto.getAddress(), personDto.getAge());
            person.setPk(personDto.getOwnerId());
            return (T) person;
        } else if (ownerClass.isAssignableFrom(Employee.class) && owner instanceof EmployeeDto) {
            final EmployeeDto employeeDto = (EmployeeDto) owner;
            final Employee employee = new Employee(employeeDto.getName(), null, employeeDto.getAddress(), employeeDto.getAge());
            employee.setPk(employeeDto.getOwnerId());
            return (T) employee;
        }

        return null;

    }
}
