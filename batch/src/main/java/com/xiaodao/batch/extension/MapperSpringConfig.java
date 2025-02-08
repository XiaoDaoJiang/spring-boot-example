package com.xiaodao.batch.extension;

import org.mapstruct.MapperConfig;
import org.mapstruct.extensions.spring.SpringMapperConfig;

@MapperConfig(componentModel = "spring", uses = com.xiaodao.batch.extension.MyConversionServiceAdapter.class)
@SpringMapperConfig(
        conversionServiceAdapterClassName = "MyConversionServiceAdapter",
        conversionServiceBeanName = "myConversionService",
        generateConverterScan = true)
public interface MapperSpringConfig {
}