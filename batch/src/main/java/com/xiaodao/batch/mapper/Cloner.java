package com.xiaodao.batch.mapper;

import com.xiaodao.batch.dto.CustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.control.DeepClone;
import org.mapstruct.factory.Mappers;

/**
 * @author Sjaak Derksen
 * <p>
 * By defining all methods, we force MapStruct to generate new objects for each mapper in stead of
 * taking shortcuts by mapping an object directly.
 */
@Mapper(mappingControl = DeepClone.class)
public interface Cloner {

    Cloner MAPPER = Mappers.getMapper(Cloner.class);

    CustomerDto clone(CustomerDto customerDto);
}