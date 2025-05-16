package com.xiaodao.graalvm.controller;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * 并发测试工具类 - 可直接在Web应用中调用测试
 */
public class ConcurrencyTestUtils {
    private static final Logger log = LoggerFactory.getLogger(ConcurrencyTestUtils.class);

    /**
     * CompletableFuture测试包装类
     */
    public static class CompletableFutureTest {
        private static final Logger log = LoggerFactory.getLogger(CompletableFutureTest.class);

        /**
         * 执行所有CompletableFuture测试用例
         *
         * @return 测试结果Map，键为测试名称，值为测试结果
         */
        public Map<String, TestResult> runAllTests() {
            log.info("开始执行所有 CompletableFuture 测试");
            Map<String, TestResult> results = new HashMap<>();

            log.debug("执行基本异步操作测试");
            results.put("basicAsyncOperation", testWithTimer(this::basicAsyncOperation));

            log.debug("执行链式操作测试");
            results.put("chainOperations", testWithTimer(this::chainOperations));

            log.debug("执行多Future组合测试");
            results.put("combineMultipleFutures", testWithTimer(this::combineMultipleFutures));

            log.debug("执行异常处理测试");
            results.put("exceptionHandling", testWithTimer(this::exceptionHandling));

            log.debug("执行自定义线程池测试");
            results.put("customExecutor", testWithTimer(this::customExecutor));

            log.info("所有 CompletableFuture 测试完成，结果: {}", results);
            return results;
        }

        /**
         * 基本的异步操作测试
         */
        public TestResult basicAsyncOperation() {
            log.info("开始基本异步操作测试");
            TestResult result = new TestResult();
            String mainThread = Thread.currentThread().getName();
            log.debug("主线程: {}", mainThread);

            try {
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    String asyncThread = Thread.currentThread().getName();
                    log.info("异步任务开始执行，线程: {}", asyncThread);
                    try {
                        log.debug("模拟耗时操作，休眠100ms");
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        log.error("异步任务被中断", e);
                        Thread.currentThread().interrupt();
                    }
                    log.info("异步任务执行完成");
                    return "任务完成";
                }, ThreadPoolUtil.getExecutorService());

                log.debug("主线程等待异步任务完成");
                String taskResult = future.get();
                log.info("异步任务返回结果: {}", taskResult);

                result.setSuccess(true);
                result.setOutput("结果: " + taskResult);
            } catch (InterruptedException | ExecutionException e) {
                log.error("异步操作测试失败", e);
                result.setSuccess(false);
                result.setOutput("错误: " + e.getMessage());
            }

            return result;
        }

        /**
         * 链式操作测试
         */
        public TestResult chainOperations() {
            log.info("开始链式操作测试");
            TestResult result = new TestResult();
            String mainThread = Thread.currentThread().getName();
            log.debug("主线程: {}", mainThread);

            try {
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    String asyncThread = Thread.currentThread().getName();
                    log.info("第一步操作开始，线程: {}", asyncThread);
                    return "Hello";
                }).thenApply(s -> {
                    String asyncThread = Thread.currentThread().getName();
                    log.info("第二步操作开始，线程: {}", asyncThread);
                    return s + " World";
                }).thenApply(s -> {
                    String asyncThread = Thread.currentThread().getName();
                    log.info("第三步操作开始，线程: {}", asyncThread);
                    return s.toUpperCase();
                });

                log.debug("主线程等待链式操作完成");
                String taskResult = future.get();
                log.info("链式操作完成，最终结果: {}", taskResult);

                result.setSuccess(true);
                result.setOutput("链式操作结果: " + taskResult);
            } catch (InterruptedException | ExecutionException e) {
                log.error("链式操作测试失败", e);
                result.setSuccess(false);
                result.setOutput("错误: " + e.getMessage());
            }

            return result;
        }

        /**
         * 组合多个CompletableFuture测试
         */
        public TestResult combineMultipleFutures() {
            log.info("开始多Future组合测试");
            TestResult result = new TestResult();
            StringBuilder output = new StringBuilder();
            String mainThread = Thread.currentThread().getName();
            log.debug("主线程: {}", mainThread);

            try {
                CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
                    String asyncThread = Thread.currentThread().getName();
                    log.info("Future1 开始执行，线程: {}", asyncThread);
                    try {
                        log.debug("Future1 休眠50ms");
                        TimeUnit.MILLISECONDS.sleep(50);
                    } catch (InterruptedException e) {
                        log.error("Future1 被中断", e);
                        Thread.currentThread().interrupt();
                    }
                    log.info("Future1 执行完成");
                    return "Future1";
                });

                CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
                    String asyncThread = Thread.currentThread().getName();
                    log.info("Future2 开始执行，线程: {}", asyncThread);
                    try {
                        log.debug("Future2 休眠70ms");
                        TimeUnit.MILLISECONDS.sleep(70);
                    } catch (InterruptedException e) {
                        log.error("Future2 被中断", e);
                        Thread.currentThread().interrupt();
                    }
                    log.info("Future2 执行完成");
                    return "Future2";
                });

                log.debug("开始合并两个Future的结果");
                CompletableFuture<String> combinedFuture = future1.thenCombine(future2, (s1, s2) -> {
                    String combineThread = Thread.currentThread().getName();
                    log.info("合并操作执行，线程: {}", combineThread);
                    return s1 + " + " + s2;
                });

                String combinedResult = combinedFuture.get();
                log.info("合并结果: {}", combinedResult);
                output.append("组合结果: ").append(combinedResult).append("\n");

                log.debug("等待所有Future完成");
                CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2);
                allFutures.get();
                log.info("所有Future已完成");
                output.append("所有Future已完成");

                result.setSuccess(true);
                result.setOutput(output.toString());
            } catch (InterruptedException | ExecutionException e) {
                log.error("多Future组合测试失败", e);
                result.setSuccess(false);
                result.setOutput("错误: " + e.getMessage());
            }

            return result;
        }

        /**
         * 异常处理测试
         */
        public TestResult exceptionHandling() {
            log.info("开始异常处理测试");
            TestResult result = new TestResult();
            String mainThread = Thread.currentThread().getName();
            log.debug("主线程: {}", mainThread);

            try {
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    String asyncThread = Thread.currentThread().getName();
                    log.info("异步任务开始执行，线程: {}", asyncThread);
                    if (ThreadLocalRandom.current().nextBoolean()) {
                        log.warn("模拟异常发生");
                        throw new RuntimeException("模拟异常");
                    }
                    log.info("异步任务正常完成");
                    return "正常结果";
                }).exceptionally(ex -> {
                    String recoveryThread = Thread.currentThread().getName();
                    log.info("异常恢复处理，线程: {}", recoveryThread);
                    return "异常后的恢复值: " + ex.getMessage();
                });

                log.debug("主线程等待结果");
                String taskResult = future.get();
                log.info("最终结果: {}", taskResult);

                result.setSuccess(true);
                result.setOutput("异常处理结果: " + taskResult);
            } catch (InterruptedException | ExecutionException e) {
                log.error("异常处理测试失败", e);
                result.setSuccess(false);
                result.setOutput("错误: " + e.getMessage());
            }

            return result;
        }

        /**
         * 自定义线程池测试
         */
        public TestResult customExecutor() {
            log.info("开始自定义线程池测试");
            TestResult result = new TestResult();
            String mainThread = Thread.currentThread().getName();
            log.debug("主线程: {}", mainThread);

            try {
                log.debug("创建固定大小为2的线程池");
                try (var executor = Executors.newFixedThreadPool(2)) {
                    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                        String asyncThread = Thread.currentThread().getName();
                        log.info("自定义线程池中的任务执行，线程: {}", asyncThread);
                        return "自定义线程池执行: " + asyncThread;
                    }, executor);

                    log.debug("主线程等待任务完成");
                    String taskResult = future.get();
                    log.info("任务执行完成，结果: {}", taskResult);

                    result.setSuccess(true);
                    result.setOutput("自定义线程池结果: " + taskResult);
                }
                log.debug("线程池已关闭");
            } catch (InterruptedException | ExecutionException e) {
                log.error("自定义线程池测试失败", e);
                result.setSuccess(false);
                result.setOutput("错误: " + e.getMessage());
            }

            return result;
        }
    }

    /**
     * ThreadLocal测试包装类
     */
    public static class ThreadLocalTest {
        private static final Logger log = LoggerFactory.getLogger(ThreadLocalTest.class);

        // 简单的ThreadLocal
        private static final ThreadLocal<String> userContext = new ThreadLocal<>();

        // 使用初始值的ThreadLocal
        private static final ThreadLocal<Integer> counter = ThreadLocal.withInitial(() -> 0);

        // 使用InheritableThreadLocal (可以被子线程继承)
        private static final InheritableThreadLocal<String> inheritableContext = new InheritableThreadLocal<>();

        /**
         * 执行所有ThreadLocal测试用例
         *
         * @return 测试结果Map，键为测试名称，值为测试结果
         */
        public Map<String, TestResult> runAllTests() {
            log.info("开始执行所有 ThreadLocal 测试");
            Map<String, TestResult> results = new HashMap<>();

            log.debug("执行基本用法测试");
            results.put("basicUsage", testWithTimer(this::basicUsage));

            log.debug("执行多线程使用测试");
            results.put("multiThreadUsage", testWithTimer(this::multiThreadUsage));

            log.debug("执行可继承ThreadLocal测试");
            results.put("inheritableThreadLocalTest", testWithTimer(this::inheritableThreadLocalTest));

            log.debug("执行内存泄漏预防测试");
            results.put("memoryLeakPrevention", testWithTimer(this::memoryLeakPrevention));

            log.info("所有 ThreadLocal 测试完成，结果: {}", results);
            return results;
        }

        /**
         * 基本用法测试
         */
        public TestResult basicUsage() {
            log.info("开始 ThreadLocal 基本用法测试");
            TestResult result = new TestResult();
            StringBuilder output = new StringBuilder();
            String threadName = Thread.currentThread().getName();
            log.debug("当前线程: {}", threadName);

            try {
                log.debug("设置 userContext 值");
                userContext.set("当前用户: admin");
                String userValue = userContext.get();
                log.info("userContext 值: {}", userValue);
                output.append("ThreadLocal值: ").append(userValue).append("\n");

                log.debug("获取并增加计数器值");
                int initialValue = counter.get();
                log.info("初始计数器值: {}", initialValue);
                output.append("初始计数器值: ").append(initialValue).append("\n");

                counter.set(initialValue + 1);
                int newValue = counter.get();
                log.info("增加后计数器值: {}", newValue);
                output.append("增加后计数器值: ").append(newValue).append("\n");

                log.debug("移除 userContext 值");
                userContext.remove();
                String afterRemove = userContext.get();
                log.info("移除后 userContext 值: {}", afterRemove);
                output.append("移除后值: ").append(afterRemove == null ? "null" : afterRemove);

                result.setSuccess(true);
                result.setOutput(output.toString());
            } catch (Exception e) {
                log.error("ThreadLocal 基本用法测试失败", e);
                result.setSuccess(false);
                result.setOutput("错误: " + e.getMessage());
            } finally {
                log.debug("清理 ThreadLocal 值");
                userContext.remove();
                counter.remove();
            }

            return result;
        }

        /**
         * 多线程环境测试
         */
        public TestResult multiThreadUsage() {
            log.info("开始多线程使用测试");
            TestResult result = new TestResult();
            StringBuilder output = new StringBuilder();
            String mainThread = Thread.currentThread().getName();
            log.debug("主线程: {}", mainThread);

            try {
                log.debug("主线程设置 ThreadLocal 值");
                userContext.set("主线程用户");
                counter.set(100);
                log.info("主线程 userContext: {}, counter: {}", userContext.get(), counter.get());

                log.debug("创建新线程");
                CompletableFuture<String> threadFuture = CompletableFuture.supplyAsync(() -> {
                    String childThread = Thread.currentThread().getName();
                    log.info("子线程开始执行: {}", childThread);
                    StringBuilder threadOutput = new StringBuilder();

                    log.debug("子线程获取 ThreadLocal 值");
                    String userValue = userContext.get();
                    int counterValue = counter.get();
                    log.info("子线程初始值 - userContext: {}, counter: {}", userValue, counterValue);
                    threadOutput.append("新线程中的userContext: ")
                            .append(userValue == null ? "null" : userValue).append("\n");
                    threadOutput.append("新线程中的counter: ")
                            .append(counterValue).append("\n");

                    log.debug("子线程设置新值");
                    userContext.set("子线程用户");
                    counter.set(200);
                    log.info("子线程设置后 - userContext: {}, counter: {}", userContext.get(), counter.get());
                    threadOutput.append("子线程设置后的userContext: ")
                            .append(userContext.get()).append("\n");
                    threadOutput.append("子线程设置后的counter: ")
                            .append(counter.get());

                    log.debug("子线程清理 ThreadLocal 值");
                    userContext.remove();
                    counter.remove();

                    return threadOutput.toString();
                });

                log.debug("主线程等待子线程完成");
                String threadResult = threadFuture.get();
                log.info("子线程执行完成");
                output.append(threadResult).append("\n\n");

                log.debug("主线程检查 ThreadLocal 值");
                log.info("主线程最终值 - userContext: {}, counter: {}", userContext.get(), counter.get());
                output.append("主线程中的userContext: ").append(userContext.get()).append("\n");
                output.append("主线程中的counter: ").append(counter.get());

                result.setSuccess(true);
                result.setOutput(output.toString());
            } catch (Exception e) {
                log.error("多线程使用测试失败", e);
                result.setSuccess(false);
                result.setOutput("错误: " + e.getMessage());
            } finally {
                log.debug("清理主线程 ThreadLocal 值");
                userContext.remove();
                counter.remove();
            }

            return result;
        }

        /**
         * 可继承的ThreadLocal测试
         */
        public TestResult inheritableThreadLocalTest() {
            log.info("开始可继承ThreadLocal测试");
            TestResult result = new TestResult();
            StringBuilder output = new StringBuilder();
            String mainThread = Thread.currentThread().getName();
            log.debug("主线程: {}", mainThread);

            AtomicInteger threadNumber = new AtomicInteger(1);

            try {
                log.debug("主线程设置 inheritableContext 值");
                inheritableContext.set("可继承的上下文");
                log.info("主线程 inheritableContext: {}", inheritableContext.get());

                log.debug("创建子线程");
                final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        final String name = "子线程" + threadNumber.getAndIncrement();
                        log.debug("创建线程池子线程：{}", name);
                        return new Thread(r, name);
                    }
                });
                CompletableFuture<String> threadFuture = CompletableFuture.supplyAsync(() -> {
                            String childThread = Thread.currentThread().getName();
                            log.info("子线程开始执行: {}", childThread);
                            StringBuilder threadOutput = new StringBuilder();

                            log.debug("子线程获取 inheritableContext 值");
                            String inheritedValue = inheritableContext.get();
                            log.info("子线程继承的值: {}", inheritedValue);
                            threadOutput.append("子线程继承的值: ").append(inheritedValue).append("\n");

                            log.debug("子线程修改 inheritableContext 值");
                            inheritableContext.set(Thread.currentThread().getName() + "修改的上下文");
                            log.info("子线程修改后的值: {}", inheritableContext.get());
                            threadOutput.append("子线程修改后的值: ").append(inheritableContext.get());

                            log.debug("子线程清理 inheritableContext 值");
                            inheritableContext.remove();

                            return threadOutput.toString();
                        },

                        threadPoolExecutor);

                log.debug("主线程等待子线程完成");
                String threadResult = threadFuture.get();
                log.info("子线程执行完成");
                output.append(threadResult).append("\n\n");

                log.debug("主线程检查 inheritableContext 值");
                log.info("主线程最终值: {}", inheritableContext.get());
                output.append("父线程中的值: ").append(inheritableContext.get());

                result.setSuccess(true);
                result.setOutput(output.toString());
            } catch (Exception e) {
                log.error("可继承ThreadLocal测试失败", e);
                result.setSuccess(false);
                result.setOutput("错误: " + e.getMessage());
            } finally {
                log.debug("清理主线程 inheritableContext 值");
                inheritableContext.remove();
            }

            return result;
        }

        /**
         * 内存泄漏预防测试
         */
        public TestResult memoryLeakPrevention() {
            log.info("开始内存泄漏预防测试");
            TestResult result = new TestResult();
            StringBuilder output = new StringBuilder();

            output.append("ThreadLocal内存泄漏预防最佳实践:\n");
            output.append("- 总是在使用完ThreadLocal后调用remove()方法\n");
            output.append("- 避免使用static ThreadLocal变量在长生命周期线程中\n");
            output.append("- 考虑使用try-finally确保清理\n\n");

            try {
                log.debug("演示使用try-finally确保清理");
                try {
                    log.debug("设置临时上下文");
                    userContext.set("临时上下文");
                    String tempValue = userContext.get();
                    log.info("临时上下文值: {}", tempValue);
                    output.append("使用临时上下文: ").append(tempValue).append("\n");
                } finally {
                    log.debug("清理临时上下文");
                    userContext.remove();
                    String afterCleanup = userContext.get();
                    log.info("清理后上下文值: {}", afterCleanup);
                    output.append("清理后上下文: ").append(afterCleanup == null ? "null" : afterCleanup);
                }

                result.setSuccess(true);
                result.setOutput(output.toString());
            } catch (Exception e) {
                log.error("内存泄漏预防测试失败", e);
                result.setSuccess(false);
                result.setOutput("错误: " + e.getMessage());
            }

            return result;
        }
    }

    /**
     * 测试结果类
     */
    @Setter
    @Getter
    public static class TestResult {
        private boolean success;
        private String output;
        private long executionTimeMs;

        @Override
        public String toString() {
            return "成功: " + success +
                    "\n执行时间: " + executionTimeMs + "ms" +
                    "\n输出:\n" + output;
        }
    }

    /**
     * 使用计时器执行测试
     *
     * @param testSupplier 测试函数
     * @return 带执行时间的测试结果
     */
    private static TestResult testWithTimer(Supplier<TestResult> testSupplier) {
        long startTime = System.currentTimeMillis();
        log.info("开始执行测试");
        TestResult result = testSupplier.get();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        result.setExecutionTimeMs(executionTime);
        log.info("测试执行完成，耗时: {}ms", executionTime);
        return result;
    }
}