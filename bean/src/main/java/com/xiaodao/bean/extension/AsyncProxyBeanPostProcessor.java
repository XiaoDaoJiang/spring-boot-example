/*
package com.xiaodao.bean.extension;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;

public class AsyncProxyBeanPostProcessor implements BeanPostProcessor {


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if (!beanFactory.containsBeanDefinition(beanName)) {
            return bean;
        }

        String methodName = AsyncInitBeanFinder.getAsyncInitMethodName(beanName, beanFactory.getBeanDefinition(beanName));

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTargetClass(bean.getClass());
        proxyFactory.setProxyTargetClass(true);

        AsyncInitializeBeanMethodInvoker invoker = new AsyncInitializeBeanMethodInvoker(bean, beanName, methodName);

        proxyFactory.addAdvice(invoker);

        return proxyFactory.getProxy();

    }

    class AsyncInitializeBeanMethodInvoker implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            if (AsyncTaskExecutor.isFinished()) {
                return invocation.getMethod().invoke(targetObject, invocation.getArguments());
            }

            Method method = invocation.getMethod();
            String methodName = method.getName();

            if (this.asyncMethodName.equals(methodName)) {
                logger.info("async-init-bean, beanName: {}, async init method: {}", beanName, asyncMethodName);
                AsyncTaskExecutor.submitTask(() -> {
                    invocation.getMethod().invoke(targetObject, invocation.getArguments());
                });

                return null;
            }

            return invocation.getMethod().invoke(targetObject, invocation.getArguments());
        }
    }
}*/
