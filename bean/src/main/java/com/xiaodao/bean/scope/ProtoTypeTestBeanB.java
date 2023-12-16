package com.xiaodao.bean.scope;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(DefaultListableBeanFactory.SCOPE_PROTOTYPE)
public class ProtoTypeTestBeanB {


    public void test() {
        System.out.println(this);
    }

}
