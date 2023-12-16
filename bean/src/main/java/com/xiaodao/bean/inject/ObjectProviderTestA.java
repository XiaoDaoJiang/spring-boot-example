package com.xiaodao.bean.inject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
public class ObjectProviderTestA {

    private final ObjectProviderTestB objectProviderTestB;

    public ObjectProviderTestA(ObjectProviderTestB objectProviderTestB) {
        this.objectProviderTestB = objectProviderTestB;
    }

    public void test() {
        log.info("testA");
        if (objectProviderTestB!=null) {
            objectProviderTestB.test();
        }
    }

    public static class ObjectProviderTestB {
        public void test() {
            log.info("testB");
        }
    }
}
