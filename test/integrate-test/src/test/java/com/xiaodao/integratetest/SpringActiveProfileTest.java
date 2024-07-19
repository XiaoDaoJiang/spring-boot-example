package com.xiaodao.integratetest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import java.util.Arrays;
import java.util.Map;

/**
 * @author jianghaitao
 * @Classname SpringActiveProfileTest
 * @Version 1.0.0
 * @Date 2024-07-09 11:12
 * @Created by jianghaitao
 */
@Slf4j
@SpringBootTest
public class SpringActiveProfileTest {


    @Autowired
    private Environment env;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void listConfigProperties() {
        log.info("active profile：{}", Arrays.toString(env.getActiveProfiles()));
        final Environment env = applicationContext.getEnvironment();
        if (env instanceof ConfigurableEnvironment) {
            ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) env;
            for (PropertySource<?> propertySource : configurableEnvironment.getPropertySources()) {
                System.out.println("PropertySource: " + propertySource.getName());
                if (propertySource.getSource() instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) propertySource.getSource();
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        System.out.println("  " + entry.getKey() + ": " + entry.getValue());
                    }
                } else {
                    System.out.println("  Source: " + propertySource.getSource());
                }
            }
        }
    }

}
