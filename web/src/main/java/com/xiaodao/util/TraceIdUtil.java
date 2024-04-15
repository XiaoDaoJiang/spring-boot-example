package com.xiaodao.util;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;

public class TraceIdUtil {

    public final static String TRACE_ID_KEY = "tid";


    public static String createTraceIdNX(HttpServletRequest request) {

        String traceId = MDC.get(TRACE_ID_KEY);
        if (StrUtil.isEmpty(traceId)) {
            traceId = request.getHeader(TRACE_ID_KEY);
            if (StrUtil.isEmpty(traceId)) {
                Object traceIdReqAttr = request.getAttribute(TRACE_ID_KEY);
                if (traceIdReqAttr == null || StrUtil.isEmpty(traceId = StrUtil.toStringOrNull(traceIdReqAttr))) {
                    traceId = UUID.fastUUID().toString(true);
                    request.setAttribute(TRACE_ID_KEY, traceId);
                }
            }

            MDC.put(TRACE_ID_KEY, traceId);
        }

        return traceId;
    }


    public static void removeTraceId() {
        MDC.clear();
    }
}
