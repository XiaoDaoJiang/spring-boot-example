package com.xiaodao.validation.mapping;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import javax.validation.constraints.Min;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author jianghaitao
 * @Classname ValidationConfigTest
 * @Version 1.0.0
 * @Date 2024-10-30 09:21
 * @Created by jianghaitao
 */
@Slf4j
class ValidationConfigTest {

    @Test
    void loadFromFile() throws Exception {
        final ValidationConfig validationConfig = ValidationConfig.loadFromFile("E:\\IdeaProjects\\learn\\spring-boot-example\\misc\\validation\\src\\main\\resources\\validation-config.json");
        log.info("{}", JSONUtil.toJsonPrettyStr(validationConfig));
    }

    @Test
    public void test() {
        final Method[] publicMethods = ReflectUtil.getPublicMethods(Min.class);
        Integer integer = 1;
        Long al = 2L;
        for (Method publicMethod : publicMethods) {
            log.info("{}", publicMethod);
            final Class<?> returnType = publicMethod.getReturnType();
            final Type type = TypeUtil.getReturnType(publicMethod);
            if (type instanceof Class) {
                log.info("{}", type);
            }
            if (returnType.isPrimitive()) {
                log.info("{}", returnType);
                if (returnType == long.class) {
                    final Object converted = Convert.convert(returnType, integer);
                    log.info("{},type:{}", converted, converted.getClass());
                }
            }
        }


    }

    @Test
    public void testType() {
        final Method[] publicMethods = ReflectUtil.getPublicMethods(Min.class);
        for (Method publicMethod : publicMethods) {
            log.info("method:{}", publicMethod);
            log.info("returnType:{}", publicMethod.getReturnType());
            log.info("getGenericReturnType:{}", publicMethod.getGenericReturnType());
            final Type type = TypeUtil.getReturnType(publicMethod);
            log.info("getReturnType:{}", type);
            log.info("type.getClass:{}", type.getClass());
        }

    }

}