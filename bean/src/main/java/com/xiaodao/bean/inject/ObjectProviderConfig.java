package com.xiaodao.bean.inject;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectProviderConfig {

    @Bean
    public ObjectProviderTestA objectProviderTestA(ObjectProvider<ObjectProviderTestA.ObjectProviderTestB> testBObjectProvider) {
        return new ObjectProviderTestA(testBObjectProvider.getIfAvailable());
    }
}
