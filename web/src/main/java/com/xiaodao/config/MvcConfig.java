package com.xiaodao.config;

import com.xiaodao.intercepter.LogInterceptor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Map;

@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private LogInterceptor logInterceptor;

    @Bean
    public CommonsRequestLoggingFilter commonsRequestLoggingFilter() {
        return new CommonsRequestLoggingFilter();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor);
    }


    @Override
    protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(5000);

        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("async-mvc-thread-");
        taskExecutor.setTaskDecorator(new MdcTaskDecorator());
        taskExecutor.initialize();

        configurer.setTaskExecutor(taskExecutor);
        super.configureAsyncSupport(configurer);
    }

    private static class MdcTaskDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            final Map<String, String> contextMap = MDC.getCopyOfContextMap();
            return () -> {
                MDC.setContextMap(contextMap);
                try {
                    runnable.run();
                } finally {
                    MDC.clear();
                }
            };
        }
    }
}
