package com.xiaodao.cache.config.composite;

import org.springframework.util.Assert;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class CompositeCacheConfigurations {


    private static final Map<CompositeCacheType, String> MAPPINGS;

    static {
        Map<CompositeCacheType, String> mappings = new EnumMap<>(CompositeCacheType.class);
        mappings.put(CompositeCacheType.REDIS, RedisCompositeCacheConfiguration.class.getName());
        mappings.put(CompositeCacheType.CAFFEINE, CaffeineCompositeCacheConfiguration.class.getName());
        MAPPINGS = Collections.unmodifiableMap(mappings);
    }

    private CompositeCacheConfigurations() {
    }

    static String getConfigurationClass(CompositeCacheType CompositeCacheType) {
        String configurationClassName = MAPPINGS.get(CompositeCacheType);
        Assert.state(configurationClassName != null, () -> "Unknown cache type " + CompositeCacheType);
        return configurationClassName;
    }

    static CompositeCacheType getType(String configurationClassName) {
        for (Map.Entry<CompositeCacheType, String> entry : MAPPINGS.entrySet()) {
            if (entry.getValue().equals(configurationClassName)) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Unknown configuration class " + configurationClassName);
    }

}
