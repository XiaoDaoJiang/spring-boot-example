package com.xiaodao.batch.extension;

import java.util.List;

import com.xiaodao.batch.model.Wheel;
import com.xiaodao.batch.model.WheelDto;
import com.xiaodao.batch.model.Wheels;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperSpringConfig.class, imports = Wheel.class)
public interface WheelsMapper extends Converter<Wheels, List<WheelDto>> {
    @Override
    List<WheelDto> convert(Wheels source);
}