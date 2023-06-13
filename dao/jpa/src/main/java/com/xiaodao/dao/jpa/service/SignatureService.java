package com.xiaodao.dao.jpa.service;


import java.util.List;

/**
 * @author jianght
 * @version 1.0, 2023-05-24 10:17:29
 */
public interface SignatureService {

    /**
     * 对单个字符串数据签名
     */
    String sign(String data);

    /**
     * 验证单个字符串数据签名
     */
    boolean verify(String data, String sign);


    /**
     * 对字符串数据列表签名
     *
     * @param dataList 数据列表
     */
    String sign(List<String> dataList);

    /**
     * 验证字符串数据列表签名
     *
     * @param dataList 数据列表
     * @param sign     签名
     * @return boolean true 验证通过，false 验证失败
     */
    boolean verify(List<String> dataList, String sign);

    /**
     * 对象签名
     *
     * @param target 目标对象实例
     */
    <T> String sign(T target);

    /**
     * 验证对象签名
     *
     * @param target 目标对象实例
     * @param sign   签名
     * @return boolean true 验证通过，false 验证失败
     */
    <T> boolean verify(T target, String sign);
}
