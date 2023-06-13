package com.xiaodao.dao.jpa.service.impl;

import cn.hutool.crypto.SmUtil;
import com.xiaodao.dao.jpa.service.SignatureService;
import com.xiaodao.dao.jpa.util.SignUtils;

import java.util.List;

/**
 * 国密SM3签名服务实现
 */
public class SM3SignatureServiceImpl implements SignatureService {

    @Override
    public String sign(String data) {
        return SmUtil.sm3(data);
    }

    @Override
    public boolean verify(String data, String sign) {
        return SmUtil.sm3(data).equals(sign);
    }

    @Override
    public String sign(List<String> dataList) {
        return SmUtil.sm3(String.join(",", dataList));
    }

    @Override
    public boolean verify(List<String> dataList, String sign) {
        return SmUtil.sm3(String.join(",", dataList)).equals(sign);
    }

    @Override
    public <T> String sign(T target) {
        return SmUtil.sm3(String.join(",", SignUtils.getSignProperties(target)));
    }

    @Override
    public <T> boolean verify(T target, String sign) {
        return SmUtil.sm3(String.join(",", SignUtils.getSignProperties(target))).equals(sign);
    }
}
