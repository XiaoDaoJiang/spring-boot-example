package com.xiaodao.cache.config.composite;

import org.springframework.boot.autoconfigure.cache.CacheType;
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

public class CompositeCacheCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String sourceClass = "";
        if (metadata instanceof ClassMetadata) {
            sourceClass = ((ClassMetadata) metadata).getClassName();
        }
        ConditionMessage.Builder message = ConditionMessage.forCondition("Cache", sourceClass);
        Environment environment = context.getEnvironment();
        try {
            BindResult<CompositeCacheType> specified = Binder.get(environment).bind("spring.cache.composite.cache-type", CompositeCacheType.class);
            if (!specified.isBound()) {
                return ConditionOutcome.noMatch(message.because("unknown cache type"));
                // return ConditionOutcome.match(message.because("automatic cache type"));
            }

            CompositeCacheType required = CompositeCacheConfigurations.getType(((AnnotationMetadata) metadata).getClassName());
            if (specified.get() == required) {
                return ConditionOutcome.match(message.because(specified.get() + " cache type"));
            }
        }
        catch (BindException ex) {
        }
        return ConditionOutcome.noMatch(message.because("unknown cache type"));
    }
}
