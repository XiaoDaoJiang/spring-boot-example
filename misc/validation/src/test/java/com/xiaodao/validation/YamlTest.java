package com.xiaodao.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.networknt.schema.*;
import com.networknt.schema.serialization.JsonNodeReader;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author jianghaitao
 * @Classname YamlTest
 * @Version 1.0.0
 * @Date 2025-01-14 15:37
 * @Created by jianghaitao
 */
public class YamlTest {

    @Test
    public void test() throws IOException {
        Yaml yaml = new Yaml();

        // 加载主文件
        Map<String, Object> mainData = yaml.load(new ClassPathResource("yml/main.yml").getInputStream());

        System.out.println(mainData);
    }


    @Test
    public void testConfig() {
        String schemaData = "---\r\n"
                + "\"$id\": 'https://schema/myschema'\r\n"
                + "properties:\r\n"
                + "  startDate:\r\n"
                + "    format: 'date'\r\n"
                + "    minLength: 6\r\n"
                + "";
        String inputData = "---\r\n"
                + "startDate: '1'\r\n"
                + "";
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        JsonNodeReader jsonNodeReader = JsonNodeReader.builder().yamlMapper(yamlMapper).build();
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012,
                builder -> builder.jsonNodeReader(jsonNodeReader).build());
        SchemaValidatorsConfig config = SchemaValidatorsConfig.builder().build();
        JsonSchema schema = factory.getSchema(schemaData, InputFormat.YAML, config);
        Set<ValidationMessage> messages = schema.validate(inputData, InputFormat.YAML, executionContext -> {
            executionContext.getExecutionConfig().setFormatAssertionsEnabled(true);
        });
        System.out.println(messages);
    }

}
