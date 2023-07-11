package com.xiaodao.example.spring.boot.test.autoconfigure;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = {EnableExampleConfig.class},webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = "classpath:test.properties")
public class SimpleTest {

    @Value("${myapp.url}")
    private String url;

    @Autowired
    private Environment environment;

    @Test
    public void testPropertySourceEnv() {
        Assertions.assertEquals("http://localhost:8080",
                environment.getProperty("myapp.url"));
    }

    @Test
    public void testPropertySourceValue() {
        Assertions.assertEquals("http://localhost:8080",
                url);
    }

}
