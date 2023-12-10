package com.xiaodao.translation.core.translator;

import com.xiaodao.translation.core.TranslateResult;

import java.lang.annotation.Annotation;

public interface Translator<T extends Annotation> {

    /**
     * @param src  要翻译的值
     * @param rule 翻译规则
     */
    TranslateResult translate(String src, T rule);

    /**
     * @param src 要翻译的值
     * @param type 翻译的类型项
     *//*
    String translateTo(String src, String type);

     *//**
     * @param src 要翻译的值
     * @param type 翻译的类型项
     *//*
    String translateFrom(String src, String type); */

}
