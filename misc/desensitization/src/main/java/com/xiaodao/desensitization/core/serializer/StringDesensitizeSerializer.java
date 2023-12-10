package com.xiaodao.desensitization.core.serializer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.xiaodao.desensitization.core.annotation.Desensitize;
import com.xiaodao.desensitization.core.masker.Masker;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 脱敏序列化器
 *
 * 实现 JSON 返回数据时，使用 {@link Masker} 对声明脱敏注解的字段，进行脱敏处理。
 *
 * 
 */
@SuppressWarnings("rawtypes")
public class StringDesensitizeSerializer extends StdSerializer<String> implements ContextualSerializer {

    @Getter
    @Setter
    private Masker masker;

    protected StringDesensitizeSerializer() {
        super(String.class);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) {
        Desensitize annotation = beanProperty.getAnnotation(Desensitize.class);
        if (annotation == null) {
            return this;
        }
        // 创建一个 StringDesensitizeSerializer 对象，使用 DesensitizeBy 对应的处理器
        StringDesensitizeSerializer serializer = new StringDesensitizeSerializer();
        serializer.setMasker(Singleton.get(annotation.handler()));
        return serializer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        if (StrUtil.isBlank(value)) {
            gen.writeNull();
            return;
        }
        // 获取序列化字段
        Field field = getField(gen);

        // 自定义处理器
        Desensitize[] annotations = AnnotationUtil.getCombinationAnnotations(field, Desensitize.class);
        if (ArrayUtil.isEmpty(annotations)) {
            gen.writeString(value);
            return;
        }
        for (Annotation annotation : field.getAnnotations()) {
            if (AnnotationUtil.hasAnnotation(annotation.annotationType(), Desensitize.class)) {
                value = this.masker.mask(value, annotation);
                gen.writeString(value);
                return;
            }
        }
        gen.writeString(value);
    }

    /**
     * 获取字段
     *
     * @param generator JsonGenerator
     * @return 字段
     */
    private Field getField(JsonGenerator generator) {
        String currentName = generator.getOutputContext().getCurrentName();
        Object currentValue = generator.getCurrentValue();
        Class<?> currentValueClass = currentValue.getClass();
        return ReflectUtil.getField(currentValueClass, currentName);
    }

}
