package com.xiaodao.desensitization.core.handler;


import com.xiaodao.desensitization.core.DesensitizeTest;
import com.xiaodao.desensitization.core.annotation.Address;
import com.xiaodao.desensitization.core.masker.Masker;

/**
 * {@link Address} 的脱敏处理器
 *
 * 用于 {@link DesensitizeTest} 测试使用
 */
public class AddressHandler implements Masker<Address> {

    @Override
    public String mask(String origin, Address annotation) {
        return origin + annotation.replacer();
    }

}
