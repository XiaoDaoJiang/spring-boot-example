package com.xiaodao.batch.extension;

import com.xiaodao.batch.model.Wheel;
import com.xiaodao.batch.model.WheelDto;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperSpringConfig.class)
public interface WheelMapper extends Converter<Wheel, WheelDto> {
    @Override
    WheelDto convert(Wheel source);
}