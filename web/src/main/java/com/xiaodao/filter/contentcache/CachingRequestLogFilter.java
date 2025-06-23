package com.xiaodao.filter.contentcache;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.slf4j.Logger;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;


/**
 * 支持异步请求
 * 需要配置 @ServletComponentScan
 */
// @Order(-1)
// @WebFilter 无法控制顺序
@WebFilter(filterName = "cachingRequestLogFilter", urlPatterns = "/*",
        dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.ASYNC}, asyncSupported = true)
public class CachingRequestLogFilter extends MDCLoggingFilter {


    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(CachingRequestLogFilter.class);


    private String beforeMessagePrefix = DEFAULT_BEFORE_MESSAGE_PREFIX;

    private String beforeMessageSuffix = DEFAULT_BEFORE_MESSAGE_SUFFIX;

    private String afterMessagePrefix = DEFAULT_AFTER_MESSAGE_PREFIX;

    private String afterMessageSuffix = DEFAULT_AFTER_MESSAGE_SUFFIX;


    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        // debug
        return logger.isDebugEnabled();
    }

    private boolean shouldLogResponse(HttpServletResponse response) {
        return logger.isDebugEnabled() && StrUtil.contains(response.getContentType(), "application/json");
    }

    /**
     * 处理异步回调请求（当异步请求处理完成后，需要经过过滤器）
     */
    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }


    /**
     * 重写父类方法，实现可重复读取响应内容
     */
    @Override
    protected void doLogFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isFirstRequest = !isAsyncDispatch(request);
        HttpServletRequest requestToUse = request;

        if (isIncludePayload() && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(request, getMaxPayloadLength());
        }


        HttpServletResponse responseToUse = response;

        if (isIncludePayload() && isFirstRequest && !(response instanceof ContentCachingResponseWrapper)) {
            responseToUse = new ContentCachingResponseWrapper(response);
        }

        boolean shouldLog = shouldLog(requestToUse);
        if (shouldLog && isFirstRequest) {
            beforeRequest(requestToUse, createMessage(requestToUse, this.beforeMessagePrefix, this.beforeMessageSuffix));
        }
        try {
            filterChain.doFilter(requestToUse, responseToUse);
        } finally {
            // beforeRequest(requestToUse, createMessage(requestToUse, this.beforeMessagePrefix, this.beforeMessageSuffix));

            // 第一次请求，并且不是异步请求，直接打印日志，返回响应
            if (isFirstRequest && !isAsyncStarted(requestToUse)) {
                boolean shouldLogResponse = shouldLogResponse(responseToUse);
                if (shouldLogResponse) {
                    afterRequest(requestToUse, createResponseMessage(requestToUse, responseToUse, this.afterMessagePrefix, this.afterMessageSuffix));
                }
                ContentCachingResponseWrapper wrapper =
                        WebUtils.getNativeResponse(responseToUse, ContentCachingResponseWrapper.class);
                Assert.notNull(wrapper, "ContentCachingResponseWrapper not found");
                wrapper.copyBodyToResponse();
            }
            // 异步请求重新调度，重新返回响应
            else if (!isFirstRequest) {
                afterRequest(requestToUse, createResponseMessage(requestToUse, responseToUse, this.afterMessagePrefix, this.afterMessageSuffix));
                ContentCachingResponseWrapper wrapper =
                        WebUtils.getNativeResponse(responseToUse, ContentCachingResponseWrapper.class);
                Assert.notNull(wrapper, "ContentCachingResponseWrapper not found");
                wrapper.copyBodyToResponse();
            }

        }
    }


    private String createResponseMessage(HttpServletRequest request, HttpServletResponse response, String prefix, String suffix) {

        StringBuilder msg = new StringBuilder();
        msg.append(prefix);

        msg.append(request.getMethod()).append(' ');
        msg.append(request.getRequestURI());

        if (isIncludeHeaders()) {
            Map<String, Collection<String>> headers = ServletUtil.getHeadersMap(response);
            if (getHeaderPredicate() != null) {
                for (String header : response.getHeaderNames()) {
                    if (!getHeaderPredicate().test(header)) {
                        headers.put(header, Collections.singleton("masked"));
                    }
                }
            }
            msg.append(", headers=").append(headers);
        }

        if (isIncludePayload()) {
            String payload = getMessagePayload(response);
            if (payload != null) {
                msg.append(", payload=").append(payload);
            }
        }

        msg.append(suffix);
        return msg.toString();

    }

    @Nullable
    protected String getMessagePayload(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper =
                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, getMaxPayloadLength());
                try {
                    return new String(buf, 0, length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    return "[unknown]";
                }
            }
        }
        return null;
    }


    @Override
    protected boolean isIncludeQueryString() {
        return true;
    }

    @Override
    protected boolean isIncludeClientInfo() {
        return true;
    }

    @Override
    protected boolean isIncludeHeaders() {
        return true;
    }

    @Override
    protected boolean isIncludePayload() {
        return true;
    }

    @Override
    protected Predicate<String> getHeaderPredicate() {
        return super.getHeaderPredicate();
    }

    @Override
    protected int getMaxPayloadLength() {
        return super.getMaxPayloadLength();
    }
}
