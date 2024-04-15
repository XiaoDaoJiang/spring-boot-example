package com.xiaodao.bean.spi;

/**
 * 自定义服务实现
 *
 * @author xiaodaojiang
 * @Classname MyNameProviderImpl
 * @Version 1.0.0
 * @Date 2024-04-15 22:52
 * @Created by xiaodaojiang
 */
public class MyNameProviderImpl implements NameProvider {

    @Override
    public String getName() {
        return "I am MyNameProviderImpl load by Spring SPI";
    }
}
