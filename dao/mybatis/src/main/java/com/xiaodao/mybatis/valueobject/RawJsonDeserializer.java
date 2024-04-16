package com.xiaodao.mybatis.valueobject;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Deserializing JSON property as String with Jackson<br>
 * 实现将有{@link com.fasterxml.jackson.annotation.JsonRawValue}注解的
 * 内容为JSON的String类型字段反序列化为String的反序列化器实现
 * copy from <a ref="https://cassiomolin.com/2017/01/24/deserializing-json-property-as-string-with-jackson/">《Deserializing JSON property as String with Jackson》</a>
 *
 */
public class RawJsonDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt)
           throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);
        return mapper.writeValueAsString(node);
    }
}