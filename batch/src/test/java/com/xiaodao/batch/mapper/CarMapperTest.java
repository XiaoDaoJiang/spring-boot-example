package com.xiaodao.batch.mapper;

import com.xiaodao.batch.domain.Car;
import com.xiaodao.batch.domain.CarDto;
import com.xiaodao.batch.domain.PersonDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xiaodaojiang
 * @Classname CarMapperTest
 * @Version 1.0.0
 * @Date 2024-08-17 23:03
 * @Created by xiaodaojiang
 */
class CarMapperTest {

    @Test
    void carDtoToCar() {

        final CarDto carDto = new CarDto();
        carDto.setMake("make");
        carDto.setSeatCount(2);
        carDto.setType("SPORTS");
        final PersonDto personDto = new PersonDto();
        personDto.setId(0L);
        personDto.setName("xx");
        personDto.setAddress("dfsfsf");
        personDto.setAge(42L);

        carDto.setOwner(personDto);

        final Car car = CarMapper.INSTANCE.carDtoToCar(carDto);

        assertNotNull(car);
        assertEquals(carDto.getMake(), car.getMake());
        assertEquals(carDto.getSeatCount(), car.getNumberOfSeats());
        assertEquals(carDto.getType(), car.getType().name());
        assertThat(car.getOwner()).isNotNull();
        assertEquals(carDto.getOwner().getOwnerId(), car.getOwner().getPk());


        assertNull(car.getEmployee());

    }
}