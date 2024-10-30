package com.xiaodao.lock.service;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {

    // 锁的键，支持 SpEL 表达式
    String key();

    // 默认过期时间 5s
    long expire() default 5000;

    /**
     * 是否严格模式，即若在生成key或加锁时出现异常，是否抛出异常，不继续执行业务
     * true 结束执行，抛出异常，false 继续不加锁执行业务
     */
    boolean isStrict();

    /**
     * 错误码
     */
    int errorCode();

    /**
     * 未获取到锁时的提示信息
     */
    String message() default "请勿重复提交";
}
