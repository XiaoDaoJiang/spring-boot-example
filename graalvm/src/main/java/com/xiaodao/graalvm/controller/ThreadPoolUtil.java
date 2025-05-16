package com.xiaodao.graalvm.controller;

import cn.hutool.core.thread.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池工具类
 * <p>
 * 使用懒加载单例模式创建线程池
 */
@Slf4j
public class ThreadPoolUtil {

    private ThreadPoolUtil() {
    }

    private static class ThreadPoolHolder {
        static {
            log.info("ThreadPoolHolder static loaded");
        }

        private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
        private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool((AVAILABLE_PROCESSORS + 1) * 2,
                new NamedThreadFactory("graalvm-pool-thread-", null, false,
                        (t, e) -> {
                            // 异常处理
                            log.error("线程池执行任务异常", e);
                        }));
    }

    public static ExecutorService getExecutorService() {
        return ThreadPoolHolder.EXECUTOR_SERVICE;
    }
}