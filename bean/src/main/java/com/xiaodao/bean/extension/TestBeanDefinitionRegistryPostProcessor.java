package com.xiaodao.bean.extension;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

/**
 * 这个接口在读取项目中的beanDefinition之后执行，提供一个补充的扩展点
 * <p>
 * 使用场景：你可以在这里动态注册自己的beanDefinition，可以加载classpath之外的bean
 */
@Slf4j
@Component
public class TestBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for (String beanDefinitionName : registry.getBeanDefinitionNames()) {
            final BeanDefinition beanDefinition = registry.getBeanDefinition(beanDefinitionName);

            if (beanDefinition.getBeanClassName() == null) {
                continue;
            }
            Class<?> beanClass;
            try {
                // 获取 Bean 的 Class 对象
                beanClass = Class.forName(beanDefinition.getBeanClassName());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Failed to load bean class", e);
            }

            // 检查 Bean 是否含有 @PostConstruct 方法
            boolean hasPostConstruct = false;
            for (Method method : beanClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(PostConstruct.class)) {
                    hasPostConstruct = true;
                    break;
                }
            }

            // 如果含有 @PostConstruct 方法，则设置为 Lazy
            if (hasPostConstruct) {
                beanDefinition.setLazyInit(true); // 设置 Bean 为延迟初始化
                log.trace("Set bean [{}] lazy init", beanDefinitionName);
            }

        }
        log.trace("[BeanDefinitionRegistryPostProcessor] postProcessBeanDefinitionRegistry");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.trace("[BeanDefinitionRegistryPostProcessor] postProcessBeanFactory");
    }
}