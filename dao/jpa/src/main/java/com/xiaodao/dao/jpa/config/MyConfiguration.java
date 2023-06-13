package com.xiaodao.dao.jpa.config;

import com.xiaodao.dao.jpa.service.SignatureService;
import com.xiaodao.dao.jpa.service.impl.SM3SignatureServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {

    @Bean
    @ConditionalOnMissingBean(SignatureService.class)
    public SignatureService signatureService() {
        return new SM3SignatureServiceImpl();
    }
}
