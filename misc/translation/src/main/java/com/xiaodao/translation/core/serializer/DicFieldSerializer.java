package com.xiaodao.translation.core.serializer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.xiaodao.translation.core.TranslateResult;
import com.xiaodao.translation.core.annotation.Translate;
import com.xiaodao.translation.core.translator.Translator;
import lombok.Setter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Setter
public class DicFieldSerializer extends StdSerializer<String> implements ContextualSerializer {

    private Translator translator;

    protected DicFieldSerializer() {
        super(String.class);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        // 统一入口，只能拿到最顶层的公共翻译注解
        Translate annotation = property.getAnnotation(Translate.class);
        if (annotation == null) {
            return this;
        }

        // 这里每次返回一个新的对象（这种指定属性序列化的方式（@JsonSerialize）每次都是新的
        // 可能这就是与直接使用 BeanPropertyWriter 来处理的区别，若返回同一个，不确定会不会有问题）
        // DicFieldSerializer serializer = new DicFieldSerializer();
        // serializer.setTranslator(Singleton.get(annotation.translator()));
        // System.out.println(this);
        // System.out.println(serializer);
        this.setTranslator(Singleton.get(annotation.translator()));
        return this;
    }

    @Override

    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        /* if (StrUtil.isBlank(value)) {
            gen.writeNull();
            return;
        } */

        // 获取序列化字段
        Field field = getField(gen);

        // 过滤掉不需要翻译的字段
        Translate[] annotations = AnnotationUtil.getCombinationAnnotations(field, Translate.class);
        if (ArrayUtil.isEmpty(annotations)) {
            gen.writeString(value);
            return;
        }
        // 确定要翻译的字段，和目标接受翻译的字段




        // 获取字段上的二级注解（指定特定解析规则的，被一级注解注释）
        for (Annotation annotation : field.getAnnotations()) {
            if (AnnotationUtil.hasAnnotation(annotation.annotationType(), Translate.class)) {
                TranslateResult translateResult = this.translator.translate(value, annotation);
                if (StrUtil.isBlank(translateResult.getFieldName())) {
                    gen.writeString(translateResult.getFieldValue());
                } else {
                    gen.writeString(value);

                    // 指定字段存在，不设置值
                    if (!ReflectUtil.hasField(gen.currentValue().getClass(), translateResult.getFieldName())) {
                        gen.writeStringField(translateResult.getFieldName(), translateResult.getFieldValue());
                    }

                }
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
