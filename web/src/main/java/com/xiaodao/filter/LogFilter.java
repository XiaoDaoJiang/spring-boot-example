package com.xiaodao.filter;

import cn.hutool.extra.servlet.ServletUtil;
import com.xiaodao.util.TraceIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Slf4j
@Order(-1)
@WebFilter(urlPatterns = "/*", asyncSupported = true)
public class LogFilter extends OncePerRequestFilter {

    /**
     * 具体获取请求参数 @see
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 处理请求前获取请求参数等
        TraceIdUtil.createTraceId();
        log.info("LogFilter doFilterInternal");
        final HttpServletRequestWrapper wrapper = new StreamRequestWrapper(request);
        log.info("请求param map:{}", ServletUtil.getParamMap(wrapper));
        log.info("请求body:{}", ServletUtil.getBody(wrapper));
        try {
            filterChain.doFilter(wrapper, response);
            // TODO 正常执行，记录日志
            log.info("正常执行，记录日志");
        } catch (Exception exception) {
            // TODO 异常执行，记录日志
            log.error("异常执行，记录日志", exception);
            throw exception;
        } finally {
            // end
            log.info("==========LogFilter End=========");
            TraceIdUtil.removeTraceId();
        }
    }

    private static class StreamRequestWrapper extends HttpServletRequestWrapper {

        private final byte[] body;

        public StreamRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(request.getInputStream(), output);
            body = output.toByteArray();
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream bais = new ByteArrayInputStream(body);
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return bais.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                @Override
                public int read() throws IOException {
                    return bais.read();
                }
            };


        }
    }


}
