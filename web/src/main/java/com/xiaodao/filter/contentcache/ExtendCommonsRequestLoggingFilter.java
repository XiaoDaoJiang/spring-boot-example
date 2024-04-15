package com.xiaodao.filter.contentcache;

import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.util.function.Predicate;

/**
 * 扩展 CommonsRequestLoggingFilter，实现它的可重复读取
 *
 * @see org.springframework.web.filter.CommonsRequestLoggingFilter
 * @see org.springframework.web.filter.AbstractRequestLoggingFilter
 * @see org.springframework.web.util.ContentCachingRequestWrapper
 *
 *
 * 只能处理请求，无法获取响应内容
 */
@Deprecated
// @WebFilter(filterName = "extendCommonsRequestLoggingFilter", urlPatterns = "/api/**")
public class ExtendCommonsRequestLoggingFilter extends CommonsRequestLoggingFilter {

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        // 只处理 json 请求内容
        return super.shouldLog(request) && request.getContentType().contains("application/json");
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
