package com.xiaodao.bean.spi;

import org.springframework.beans.factory.annotation.Autowired;

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


    @Autowired
    private OtherBeanC otherBeanC;

    @Override
    public String getName() {
        otherBeanC.print();
        return "I am MyNameProviderImpl load by Spring SPI";
    }
}
