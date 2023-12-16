package com.xiaodao.springbootjpa.config;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Order
@Aspect
@Component
public class ServiceLayerTransactionLogAspect {


    @Pointcut("execution(* com.xiaodao..service.impl.*.*(..))")
    public void serviceLayer() {
    }

    @Before("serviceLayer()")
    public void beforeServiceLayer(JoinPoint joinPoint) {
        final DefaultTransactionStatus transactionStatus = (DefaultTransactionStatus) TransactionAspectSupport.currentTransactionStatus();
        if (transactionStatus.hasTransaction()) {
            log.trace("curr method:{}, tx:{},\n" +
                            "hasSavepoint={}\n" +
                            "isNewTransaction={}\n" +
                            "isRollbackOnly={}\n" +
                            "isCompleted={}\n" +
                            "transaction={}\n" +
                            "suspended resource={}\n", joinPoint.getSignature(),
                    TransactionSynchronizationManager.getCurrentTransactionName(),
                    transactionStatus.hasSavepoint(), transactionStatus.isNewTransaction(),
                    transactionStatus.isRollbackOnly(), transactionStatus.isCompleted(),
                    transactionStatus.getTransaction(),
                    transactionStatus.getSuspendedResources());
        } else {
            log.trace("curr method:{}, No transaction active:{},\n" +
                            "hasSavepoint={}\n" +
                            "isNewTransaction={}\n" +
                            "isRollbackOnly={}\n" +
                            "isCompleted={}\n" +
                            "suspended resource={}\n", joinPoint.getSignature(),
                    TransactionSynchronizationManager.getCurrentTransactionName(),
                    transactionStatus.hasSavepoint(), transactionStatus.isNewTransaction(),
                    transactionStatus.isRollbackOnly(), transactionStatus.isCompleted(),
                    transactionStatus.getSuspendedResources());
        }

    }


}
