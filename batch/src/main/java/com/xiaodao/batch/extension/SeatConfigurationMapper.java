package com.xiaodao.batch.extension;

import com.xiaodao.batch.model.SeatConfiguration;
import com.xiaodao.batch.model.SeatConfigurationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperSpringConfig.class)
public interface SeatConfigurationMapper extends Converter<SeatConfiguration, SeatConfigurationDto> {
    @Mapping(target = "seatCount", source = "numberOfSeats")
    @Mapping(target = "material", source = "seatMaterial")
    SeatConfigurationDto convert(SeatConfiguration seatConfiguration);
}