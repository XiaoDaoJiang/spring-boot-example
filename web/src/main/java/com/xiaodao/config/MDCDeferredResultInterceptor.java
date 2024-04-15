package com.xiaodao.config;

import com.xiaodao.util.TraceIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
public class MDCDeferredResultInterceptor implements DeferredResultProcessingInterceptor {


    @Override
    public <T> void beforeConcurrentHandling(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
        log.info("beforeConcurrentHandling");
    }

    @Override
    public <T> void preProcess(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
        log.info("preProcess");
    }

    @Override
    public <T> void postProcess(NativeWebRequest request, DeferredResult<T> deferredResult, Object concurrentResult) throws Exception {
        TraceIdUtil.createTraceIdNX(Objects.requireNonNull(request.getNativeRequest(HttpServletRequest.class)));
        log.info("postProcess");
    }

    @Override
    public <T> boolean handleTimeout(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
        TraceIdUtil.createTraceIdNX(Objects.requireNonNull(request.getNativeRequest(HttpServletRequest.class)));
        log.info("handleTimeout");
        return DeferredResultProcessingInterceptor.super.handleTimeout(request, deferredResult);
    }

    @Override
    public <T> boolean handleError(NativeWebRequest request, DeferredResult<T> deferredResult, Throwable t) throws Exception {
        TraceIdUtil.createTraceIdNX(Objects.requireNonNull(request.getNativeRequest(HttpServletRequest.class)));
        log.info("handleError");
        return DeferredResultProcessingInterceptor.super.handleError(request, deferredResult, t);
    }

    @Override
    public <T> void afterCompletion(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
        log.info("afterCompletion");
        MDC.clear();

    }
}
