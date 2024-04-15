package com.xiaodao.bean.spi;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringSPI implements BeanClassLoaderAware {

    private ClassLoader classLoader;

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
        return nameProviders.stream().map(NameProvider::getName).toArray(String[]::new);
    }

}
