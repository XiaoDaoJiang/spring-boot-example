/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package com.xiaodao.batch.mapper;

import com.xiaodao.batch.dto.Source;
import com.xiaodao.batch.entities.Target;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface SourceTargetMapper {

    SourceTargetMapper MAPPER = Mappers.getMapper( SourceTargetMapper.class );

    @Mapping( source = "test", target = "testing" )
    Target toTarget(Source s );
}