package com.xiaodao.cache.config.composite;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "spring.cache.composite")
public class CompositeCacheProperties {

    boolean enabled;

    /**
     * 可独立声明多个缓存类型：目前支持 caffeine,redis
     */
    CompositeCacheType[] cacheType;

}
