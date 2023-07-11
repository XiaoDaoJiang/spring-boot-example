package com.xiaodao.example.spring.boot.test.autoconfigure;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration
@TestPropertySource(locations = "classpath:test.properties")
public class EnableExampleConfig {

}
