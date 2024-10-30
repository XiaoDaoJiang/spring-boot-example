package com.xiaodao.lock.service;

import cn.hutool.core.util.StrUtil;
import com.xiaodao.common.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class RedisLockAspect {

    @Autowired
    private RedisDistributedLockService lockService;

    private final ExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();


    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        // 获取方法和参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();

        // 解析 SpEL 表达式，生成锁键
        String lockKey;
        try {
            lockKey = parseKey(redisLock.key(), method, args);
        } catch (Exception e) {
            log.error("生成锁键失败", e);
            if (redisLock.isStrict()) {
                throw new ServerException("生成锁键失败");
            }
            // 不严格模式下，不加锁执行
            return joinPoint.proceed();
        }

        if (StrUtil.isEmpty(lockKey)) {
            log.error("生成锁键为空");
            if (redisLock.isStrict()) {
                throw new ServerException("生成锁键为空");
            }
            // 不严格模式下，不加锁执行
            return joinPoint.proceed();
        }

        long expireTime = redisLock.expire();

        // 获取锁
        String lockValue;
        try {
            lockValue = lockService.acquireLock(lockKey, expireTime);
        } catch (Exception e) {
            log.error("加锁失败", e);
            if (redisLock.isStrict()) {
                throw new ServerException("生成锁键为空");
            }
            // 不严格模式下，不加锁执行
            return joinPoint.proceed();
        }

        if (lockValue == null) {
            throw new ServerException(redisLock.errorCode(), redisLock.message());
        }

        try {
            // 执行目标方法
            return joinPoint.proceed();
        } finally {
            // 释放锁
            boolean released = lockService.releaseLock(lockKey, lockValue);
            if (!released) {
                // 记录日志或进行其他处理
                log.warn("{}未能成功释放锁: {}或者锁已过期自动释放", method.getName(), lockKey);
            }
        }
    }

    /**
     * 解析 SpEL 表达式，生成锁键
     *
     * @param key    SpEL 表达式
     * @param method 方法
     * @param args   方法参数
     * @return 解析后的锁键
     */
    private String parseKey(String key, Method method, Object[] args) {
        // 创建上下文并传入方法参数
        StandardEvaluationContext context = new MethodBasedEvaluationContext(null, method, args, nameDiscoverer);
        Expression expression = parser.parseExpression(key);
        return expression.getValue(context, String.class);

    }
}
