package com.xiaodao.batch.extension;

import java.util.List;

import com.xiaodao.batch.model.WheelDto;
import com.xiaodao.batch.model.Wheels;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperSpringConfig.class)
public interface WheelsDtoListMapper extends Converter<List<WheelDto>, Wheels> {
    @Override
    Wheels convert(List<WheelDto> source);
}