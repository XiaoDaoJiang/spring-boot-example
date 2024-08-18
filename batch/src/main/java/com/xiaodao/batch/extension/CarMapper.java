package com.xiaodao.batch.extension;

import com.xiaodao.batch.model.Car;
import com.xiaodao.batch.model.CarDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperSpringConfig.class)
public interface CarMapper extends Converter<Car, CarDto> {
    @Mapping(target = "seats", source = "seatConfiguration")
    CarDto convert(Car car);


    @InheritInverseConfiguration
    @DelegatingConverter
    Car invertConvert(CarDto carDto);
}