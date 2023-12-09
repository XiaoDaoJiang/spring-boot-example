package com.xiaodao.desensitization.core.masker;

import java.lang.annotation.Annotation;

/**
 * 脱敏处理器接口
 *
 */
public interface Masker<T extends Annotation> {

    /**
     * 脱敏
     *
     * @param origin     原始字符串
     * @param annotation 脱敏规则注解：
     *                   1、如固定规则的邮箱、手机等脱敏规则，
     *                   2、根据正则表达式
     *                   3、简易指定前几位，后几位
     * @return 脱敏后的字符串
     */
     String  mask(String origin, T annotation);

}
