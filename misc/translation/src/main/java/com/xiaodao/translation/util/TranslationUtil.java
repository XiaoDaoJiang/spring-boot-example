package com.xiaodao.translation.util;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.xiaodao.translation.core.TranslateResult;
import com.xiaodao.translation.core.annotation.Translate;
import com.xiaodao.translation.core.translator.Translator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

@Slf4j
public class TranslationUtil {

    public static void scan(Object obj) {
        // 根据@Translate注解的ignore属性判断是否翻译
        scan(obj, true);
    }

    /**
     * @param obj
     * @param checkIgnore 是否判断翻译注解是否需要跳过，true：判断是否跳过，false：不判断是否跳过，直接翻译
     *                    用于控制某些接口返回脱敏信息，不需要翻译的情况
     */
    public static void scan(Object obj, boolean checkIgnore) {
        if (null != obj) {
            if (obj instanceof Iterable) {
                for (Object o : (Iterable<?>) obj) {
                    scan(o, checkIgnore);
                }
            } else if (obj instanceof Map) {
                Set<?> set = ((Map<?, ?>) obj).entrySet();
                for (Object o : set) {
                    scan(o, checkIgnore);
                }
            }
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field == null) {
                    continue;
                }
                field.setAccessible(true);
                try {
                    final Translate translate = AnnotationUtils.findAnnotation(field, Translate.class);
                    if (translate != null) {

                        // 判断是否需要翻译
                        // !checkIgnore = true 时，不判断是否忽略（annotation.ignore()），直接翻译
                        if ((!checkIgnore || !translate.ignore()) && field.getType().equals(String.class)) {
                            String val = (String) field.get(obj);
                            final Annotation[] fieldAnnotations = field.getAnnotations();
                            for (Annotation fieldAnnotation : fieldAnnotations) {
                                if (AnnotationUtil.hasAnnotation(fieldAnnotation.annotationType(), Translate.class)) {
                                    final Translator translator = Singleton.get(translate.translator());
                                    TranslateResult translateResult = translator.translate(field.get(obj).toString(), fieldAnnotation);

                                    // 未指定翻译后的字段名
                                    if (StrUtil.isBlank(translateResult.getFieldName())) {
                                        field.set(obj, translateResult.getFieldValue());
                                    }
                                    // 设置指定翻译后字段
                                    else {
                                        if (ReflectUtil.hasField(obj.getClass(), translateResult.getFieldName())) {
                                            ReflectUtil.setFieldValue(obj, translateResult.getFieldName(), translateResult.getFieldValue());
                                        }
                                    }
                                }
                            }
                        }

                    } else if (field.getType().isInstance(obj) && null != field.get(obj)) {
                        scan(field.get(obj), checkIgnore);
                    }
                } catch (IllegalAccessException | SecurityException e) {
                    log.error("翻译数据异常", e);
                }
            }
        }
    }
}
