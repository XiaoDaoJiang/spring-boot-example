package com.xiaodao.batch.mapper;

import com.xiaodao.batch.domain.*;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaodaojiang
 * @Classname CarMapper
 * @Version 1.0.0
 * @Date 2024-08-17 22:32
 * @Created by xiaodaojiang
 */
@Mapper(uses = {OwnerMapper.class})
public interface CarMapper {

    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    @Mapping(target = "employee", source = "owner")
    @Mapping(target = "numberOfSeats", source = "seatCount")
    Car carDtoToCar(CarDto carDto);


    @Mapping(target = "numberOfSeats", source = "seatCount")
    @Mapping(target = "employee", source = "owner", qualifiedByName = "ownerToEmployee")
    Car carDtoToCar(CarDto carDto, @Context CarType carType);

    // @ObjectFactory
    @Named("ownerToEmployee")
    default Employee ownerToEmployee(Owner owner, @Context CarType carType) {
        if (CarType.BUSINESS.equals(carType)) {
            final Employee employee = new Employee();
            employee.setPk(owner.getOwnerId());
            employee.setFirstName("Employee_" + owner.getOwnerId());
            return employee;
        }
        return null;
    }

}
