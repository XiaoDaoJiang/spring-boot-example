package com.xiaodao.batch.slice;

import cn.hutool.core.thread.NamedThreadFactory;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @author xiaodaojiang
 * @Classname BatchProcess
 * @Version 1.0.0
 * @Date 2024-12-15 12:10
 * @Created by xiaodaojiang
 */
@Slf4j
@Service
public class BatchProcess {

    /**
     * 迁移任务数据
     */
    @Data
    @Builder
    static class MigrateData {
        private Long id;
        private String migrateCode;
        private Long migrateSourceId;
        private Long migrateTargetId;
        /**
         * 迁移状态, 0:未迁移, 1:迁移成功, 2:迁移失败
         */
        private Integer status;
        private Date createTime;
    }

    ExecutorService threadPool = Executors.newFixedThreadPool(20, new NamedThreadFactory("batch-process", true));


    public void distributeData(List<Integer> data) {
        log.info("开始分发数据");
        for (Integer item : data) {
            CompletableFuture.runAsync(() -> {

                try {
                    processData(item);
                } catch (Exception e) {
                    log.error("处理数据异常: {}", e.getMessage());
                }
            }, threadPool);
        }
    }


    /**
     * 模拟转换数据并迁移保存
     */
    public void processData(Integer data) {
        log.info("开始处理数据");
        try {
            log.info("迁移数据: {}", data);
            doProcessData(data, (result) -> saveProcessResult(data, result, 1));
            log.info("处理数据完成");
        } catch (Exception e) {
            log.error("处理数据异常: {}", e.getMessage());
            saveProcessResult(data, null, 2);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void saveProcessResult(Integer data, Integer savedResult, int status) {
        log.info("保存迁移进度: {}", MigrateData.builder()
                .migrateCode("code")
                .migrateSourceId(Long.valueOf(data))
                .migrateTargetId(savedResult == null ? null : Long.valueOf(savedResult))
                .status(status)
                .build());
    }


    @Transactional(rollbackFor = Exception.class)
    public void doProcessData(Integer data, Consumer<Integer> consumer) {
        log.info("保存迁移数据: {}", data);
        Integer result = data;
        consumer.accept(result);
    }

}
