package com.xiaodao.filter.contentcache;

import com.xiaodao.util.TraceIdUtil;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 支持 MDC 日志打印请求处理链路跟踪
 */

public abstract class MDCLoggingFilter extends CommonsRequestLoggingFilter {


    /**
     * 重写父类方法，实现可重复读取响应内容
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 创建 traceId（后端生成或根据前端参数/header放入上下文中）
        final String traceId = TraceIdUtil.createTraceIdNX(request);
        try {

            final String header = response.getHeader(TraceIdUtil.TRACE_ID_KEY);
            if (header == null) {
                response.addHeader(TraceIdUtil.TRACE_ID_KEY, traceId);
            }
            doLogFilter(request, response, filterChain);

        } finally {
            TraceIdUtil.removeTraceId();
        }
    }

    protected abstract void doLogFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException;

}
