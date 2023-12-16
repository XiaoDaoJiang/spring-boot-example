package com.xiaodao.bean.scope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service
@Scope(DefaultListableBeanFactory.SCOPE_PROTOTYPE)
public class ProtoTypeTestBean {

    @Autowired
    private ProtoTypeTestBeanB protoTypeTestBeanB;

    public ProtoTypeTestBeanB getProtoTypeTestBeanB() {
        return protoTypeTestBeanB;
    }

    public void setProtoTypeTestBeanB(ProtoTypeTestBeanB protoTypeTestBeanB) {
        this.protoTypeTestBeanB = protoTypeTestBeanB;
    }

    public void testA() {
        System.out.println(this);
        protoTypeTestBeanB.test();
    }
}
