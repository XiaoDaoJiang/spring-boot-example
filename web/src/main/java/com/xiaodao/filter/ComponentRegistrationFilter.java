package com.xiaodao.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 这样的过滤器 自动注册 拦截 /*
 * 扫描时机	Spring Boot 启动时，ServletWebServerApplicationContext#selfInitialize 初始化过程
 * 控制类	ServletContextInitializerBeans
 * 顺序控制	使用 @Order 或实现 Ordered 接口
 * 路径控制	自动为 /*，不能自定义路径，除非使用 FilterRegistrationBean
 */
@Slf4j
@Component
@Order(2)
public class ComponentRegistrationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("ComponentRegistrationFilter: Processing request for URI: {}", request.getRequestURI());
        filterChain.doFilter(request, response);
        log.info("ComponentRegistrationFilter: Finished processing request for URI: {}", request.getRequestURI());
    }
}
