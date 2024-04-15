package com.xiaodao.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Mapped Diagnostic Context (MDC) 任务装饰器
 */
@Slf4j
@Component
public class MdcTaskDecorator implements TaskDecorator {

    public Runnable decorate(Runnable runnable, boolean finallyClear) {
        final Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        return () -> {
            if (copyOfContextMap != null) {
                log.debug("[decorate][将 MDC 的内容复制到子线程中]");
                MDC.setContextMap(copyOfContextMap);
                try {
                    runnable.run();
                } finally {
                    if (finallyClear) {
                        log.debug("[decorate][清空子线程的 MDC 内容]");
                        MDC.clear();
                    }
                }
            } else {
                runnable.run();
            }

        };
    }

    @Override
    public Runnable decorate(Runnable runnable) {
        return decorate(runnable, true);
    }
}
