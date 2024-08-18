package com.xiaodao.batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
public class ConverterConfig {

    @Bean
    ConfigurableConversionService myConversionService() {
        return new DefaultConversionService();
    }
}