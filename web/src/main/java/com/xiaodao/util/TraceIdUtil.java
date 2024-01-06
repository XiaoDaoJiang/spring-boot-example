package com.xiaodao.util;

import cn.hutool.core.lang.UUID;
import org.slf4j.MDC;

public class TraceIdUtil {

    private final static String TRACE_ID_KEY = "tid";


    public static void createTraceId() {
        MDC.put(TRACE_ID_KEY, UUID.fastUUID().toString());
    }


    public static void removeTraceId() {
        MDC.clear();
    }
}
