package com.xiaodao.bean.spi;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;

@Configuration(
        proxyBeanMethods = false
)
@Import({SpringSpiAutoConfiguration.SpiConfigurationImportSelector.class})
public class SpringSpiAutoConfiguration {


    /**
     * {@link ImportSelector} to add {@link NameProvider} classes.
     */
    static class SpiConfigurationImportSelector implements ImportSelector, BeanClassLoaderAware {

        private ClassLoader beanClassLoader;

        @Override
        public void setBeanClassLoader(ClassLoader classLoader) {
            this.beanClassLoader = classLoader;
        }

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            final Class<?>[] importSelectorTypes = {
                    NameProvider.class,
            };
            return Arrays.stream(importSelectorTypes).flatMap(selectorType ->
                            SpringFactoriesLoader.loadFactoryNames(selectorType, beanClassLoader).stream())
                    .toArray(String[]::new);
        }

    }
}
