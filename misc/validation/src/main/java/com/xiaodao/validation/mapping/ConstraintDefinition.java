package com.xiaodao.validation.mapping;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.cfg.GenericConstraintDef;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConstraintDefinition<T extends Annotation> extends GenericConstraintDef<T> {

    private Class<T> type;

    private Map<String, Object> parameters = new HashMap<>();

    /**
     * 处理不同 Validation constraint annotation 的属性
     * 如@Size(min=1, max=10)中的min和max
     */
    @JsonAnySetter
    public void add(String key, Object value) {
        final Method method = ReflectUtil.getMethodByName(this.getType(), key);
        // 目前只支持普通类型的属性转换
        if (method != null) {
            if (ClassUtil.isSimpleTypeOrArray(method.getReturnType())) {
                final Object converted = Convert.convert(method.getReturnType(), value);
                super.param(key, converted);
                log.debug("Method found convert: {},{},{}", key, method, method.getReturnType());
            } else {
                log.warn("不兼容的注解属性，无法转换: {}", key);
            }
        } else {
            log.warn("Method not found: {}", key);
            super.param(key, value);
        }
        parameters.put(key, value);
        // AnnotationUtils.getAnnotationAttributes()
        // TypeUtil.getReturnType(this.getType(),"")
    }

    @JsonCreator
    public ConstraintDefinition(@JsonProperty("type") String type) {
        super((Class<T>) SupportConstraint.resolveConstraintType(type));
        this.type = (Class<T>) SupportConstraint.resolveConstraintType(type);
    }


}
