package com.xiaodao.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.stream.Collectors;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class ReReadableFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        StreamRequestWrapper streamRequestWrapper = new StreamRequestWrapper((HttpServletRequest) request);
        log.info(streamRequestWrapper.getReader().lines().collect(Collectors.joining()));
        chain.doFilter(streamRequestWrapper, response);
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
                    return bais.available()==0;
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
