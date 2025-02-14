package com.xiaodao.cache.config.composite;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;

import java.util.Arrays;

@Slf4j
class CompositeCacheCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String sourceClass = "";
        if (metadata instanceof ClassMetadata) {
            sourceClass = ((ClassMetadata) metadata).getClassName();
        }
        ConditionMessage.Builder message = ConditionMessage.forCondition("Composite Cache", sourceClass);
        Environment environment = context.getEnvironment();
        try {
            log.info("CompositeCacheCondition getMatchOutcome:{}", sourceClass);
            BindResult<CompositeCacheType[]> specifieds = Binder.get(environment).bind("spring.cache.composite.cache-type", CompositeCacheType[].class);
            if (!specifieds.isBound()) {
                // return ConditionOutcome.noMatch(message.because("unknown cache type"));
                return ConditionOutcome.match(message.because("automatic cache type"));
            }

            // 根据缓存类型与判断当前缓存配置类判断是否匹配
            // 如：cacheType=REDIS，且当前缓存配置类为 RedisCompositeCacheConfiguration，则匹配
            CompositeCacheType required = CompositeCacheConfigurations.getType(((AnnotationMetadata) metadata).getClassName());
            if (Arrays.stream(specifieds.get()).anyMatch(sp -> sp == required)) {
                return ConditionOutcome.match(message.because(Arrays.toString(specifieds.get()) + " cache type"));
            }
        } catch (BindException ex) {
        }
        return ConditionOutcome.noMatch(message.because("unknown cache type"));
    }
}
