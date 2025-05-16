package com.xiaodao.graalvm.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 并发测试控制器
 * 提供Web API来测试CompletableFuture和ThreadLocal在GraalVM Native Image中的行为
 */
@RestController
@RequestMapping("/api/test")
public class ConcurrencyTestController {

    /**
     * 测试所有CompletableFuture功能
     *
     * @return 测试结果
     */
    @GetMapping("/completable-future")
    public Map<String, Object> testCompletableFuture() {
        Map<String, Object> response = new HashMap<>();
        ConcurrencyTestUtils.CompletableFutureTest cfTest = new ConcurrencyTestUtils.CompletableFutureTest();

        try {
            Map<String, ConcurrencyTestUtils.TestResult> results = cfTest.runAllTests();
            response.put("status", "success");
            response.put("results", results);

            // 结果摘要
            Map<String, Boolean> summary = new HashMap<>();
            results.forEach((name, result) -> summary.put(name, result.isSuccess()));
            response.put("summary", summary);

            // 所有测试是否通过
            boolean allPassed = summary.values().stream().allMatch(passed -> passed);
            response.put("allPassed", allPassed);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "测试执行失败: " + e.getMessage());
        }

        return response;
    }

    /**
     * 测试所有ThreadLocal功能
     *
     * @return 测试结果
     */
    @GetMapping("/thread-local")
    public Map<String, Object> testThreadLocal() {
        Map<String, Object> response = new HashMap<>();
        ConcurrencyTestUtils.ThreadLocalTest tlTest = new ConcurrencyTestUtils.ThreadLocalTest();

        try {
            Map<String, ConcurrencyTestUtils.TestResult> results = tlTest.runAllTests();
            response.put("status", "success");
            response.put("results", results);

            // 结果摘要
            Map<String, Boolean> summary = new HashMap<>();
            results.forEach((name, result) -> summary.put(name, result.isSuccess()));
            response.put("summary", summary);

            // 所有测试是否通过
            boolean allPassed = summary.values().stream().allMatch(passed -> passed);
            response.put("allPassed", allPassed);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "测试执行失败: " + e.getMessage());
        }

        return response;
    }

    /**
     * 运行所有并发测试
     *
     * @return 汇总测试结果
     */
    @GetMapping("/all")
    public Map<String, Object> testAll() {
        Map<String, Object> response = new HashMap<>();

        try {
            // CompletableFuture测试
            Map<String, Object> cfResponse = testCompletableFuture();

            // ThreadLocal测试
            Map<String, Object> tlResponse = testThreadLocal();

            // 汇总结果
            response.put("completableFuture", cfResponse);
            response.put("threadLocal", tlResponse);

            // 整体状态
            boolean cfSuccess = "success".equals(cfResponse.get("status"));
            boolean tlSuccess = "success".equals(tlResponse.get("status"));
            boolean cfAllPassed = cfResponse.containsKey("allPassed") && (boolean) cfResponse.get("allPassed");
            boolean tlAllPassed = tlResponse.containsKey("allPassed") && (boolean) tlResponse.get("allPassed");

            response.put("status", cfSuccess && tlSuccess ? "success" : "error");
            response.put("allPassed", cfAllPassed && tlAllPassed);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "测试执行失败: " + e.getMessage());
        }

        return response;
    }

    /**
     * 简单健康检查接口
     *
     * @return 健康状态
     */
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "并发测试API可用");
        return response;
    }
}