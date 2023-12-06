package com.xiaodao.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class LogFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 处理请求前获取请求参数等
        log.info("LogFilter doFilterInternal");
        try {
            filterChain.doFilter(request, response);
            // TODO 正常执行，记录日志
            log.info("正常执行，记录日志");
        } catch (Exception exception) {
            // TODO 异常执行，记录日志
            log.error("异常执行，记录日志", exception);
            throw exception;
        }
        // end
        log.info("==========LogFilter End=========");
    }


    /**
     * @return true: 不经过该过滤器，false 走过滤器
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return false;
    }
}
