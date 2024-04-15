package com.xiaodao.config;

import com.xiaodao.intercepter.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private LogInterceptor logInterceptor;

    @Autowired
    private MdcTaskDecorator mdcTaskDecorator;

    // @Bean
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
        taskExecutor.setTaskDecorator(mdcTaskDecorator);
        taskExecutor.initialize();

        configurer.setTaskExecutor(taskExecutor);

        configurer.registerDeferredResultInterceptors(new MDCDeferredResultInterceptor());
        super.configureAsyncSupport(configurer);
    }


}
