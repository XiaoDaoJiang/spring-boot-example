package com.xiaodao.bean.spi;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringSPI implements BeanClassLoaderAware, ApplicationContextAware {

    private ClassLoader classLoader;

    private ApplicationContext applicationContext;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * 参考 Spring boot 自动化装配使用 SpringFactoriesLoader
     * {@link AutoConfigurationImportSelector#getCandidateConfigurations(AnnotationMetadata, AnnotationAttributes)}
     */
    public String[] list() {
        final List<NameProvider> nameProviders = SpringFactoriesLoader.loadFactories(NameProvider.class, classLoader);

        return nameProviders.stream().map(b->{
            applicationContext.getAutowireCapableBeanFactory().autowireBean(b);
            return b.getName();
        }).toArray(String[]::new);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
