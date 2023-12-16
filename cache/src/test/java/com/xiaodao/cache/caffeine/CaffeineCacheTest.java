package com.xiaodao.cache.caffeine;

import com.github.benmanes.caffeine.cache.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * 驱逐策略在创建缓存的时候进行指定。常用的有基于容量的驱逐和基于时间的驱逐。
 * <p>
 * 基于容量的驱逐需要指定缓存容量的最大值，当缓存容量达到最大时，
 * Caffeine将使用LRU策略对缓存进行淘汰；
 * <p>
 * 基于时间的驱逐策略如字面意思，可以设置在最后访问/写入一个缓存经过指定时间后，自动进行淘汰。
 * <p>
 * 驱逐策略可以组合使用，任意驱逐策略生效后，该缓存条目即被驱逐。
 * <p>
 * LRU 最近最少使用，淘汰最长时间没有被使用的页面。
 * LFU 最不经常使用，淘汰一段时间内使用次数最少的页面
 * FIFO 先进先出
 * Caffeine有4种缓存淘汰设置
 * 大小（缓存个数） （LFU算法进行淘汰）
 * 权重 （大小与权重 只能二选一）
 * 时间
 * 引用 （不常用，本文不介绍）
 */
@Slf4j
public class CaffeineCacheTest {

    @Test
    public void manualLoadTest() {
        Cache<Object, Object> cache = Caffeine.newBuilder()
                //初始数量
                .initialCapacity(10)
                //最大条数
                .maximumSize(10)
                //expireAfterWrite和expireAfterAccess同时存在时，以expireAfterWrite为准
                //最后一次写操作后经过指定时间过期
                .expireAfterWrite(1, TimeUnit.SECONDS)
                //最后一次读或写操作后经过指定时间过期
                .expireAfterAccess(1, TimeUnit.SECONDS)
                //监听缓存被移除
                .removalListener((key, val, removalCause) -> { })
                //记录命中
                .recordStats()
                .build();

        cache.put("1","张三");
        //张三
        System.out.println(cache.getIfPresent("1"));
        //存储的是默认值
        System.out.println(cache.get("2",o -> "默认值"));
    }

    @Test
    public void loadTest() {
        LoadingCache<String, String> loadingCache = Caffeine.newBuilder()
                //创建缓存或者最近一次更新缓存后经过指定时间间隔，刷新缓存；refreshAfterWrite仅支持LoadingCache
                .refreshAfterWrite(10, TimeUnit.SECONDS)
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .expireAfterAccess(10, TimeUnit.SECONDS)
                .maximumSize(10)
                //根据key查询数据库里面的值，这里是个lamba表达式
                .build(key -> new Date().toString());

        loadingCache.put("1","张三");
        //张三
        System.out.println(loadingCache.getIfPresent("1"));
        //存储的是默认值
        System.out.println(loadingCache.get("2",o -> "默认值"));
    }


    @Test
    public void  asyncLoadingCacheTest() {
        AsyncLoadingCache<String, String> asyncLoadingCache = Caffeine.newBuilder()
                //创建缓存或者最近一次更新缓存后经过指定时间间隔刷新缓存；仅支持LoadingCache
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .expireAfterAccess(1, TimeUnit.SECONDS)
                .maximumSize(10)
                //根据key查询数据库里面的值
                .buildAsync(key -> {
                    Thread.sleep(1000);
                    return new Date().toString();
                });

        //异步缓存返回的是CompletableFuture
        CompletableFuture<String> future = asyncLoadingCache.get("1");
        future.thenAccept(System.out::println);
    }

    /**
     * 缓存大小（个数）淘汰
     */
    @Test
    public void maximumSizeTest() throws InterruptedException {
        Cache<Integer, Integer> cache = Caffeine.newBuilder()
                // 超过10个后会使用W-TinyLFU算法进行淘汰
                .maximumSize(10)
                .evictionListener((key, val, removalCause) -> {
                    log.info("淘汰缓存：key:{} val:{}，cause：{}", key, val, removalCause);

                })
                .build();

        for (int i = 1; i < 20; i++) {
            cache.put(i, i);
        }
        Thread.sleep(500);// 缓存淘汰是异步的

        // 打印还没被淘汰的缓存
        final ConcurrentMap<@NonNull Integer, @NonNull Integer> cacheMap = cache.asMap();
        log.info("没被淘汰的缓存:{},{}", cacheMap.size(), cacheMap);

    }

    /**
     * 权重淘汰，不能与maximumSize一起使用
     */
    @Test
    public void maximumWeightTest() throws InterruptedException {
        Cache<Integer, Integer> cache = Caffeine.newBuilder()
                // 限制总权重，若所有缓存的权重加起来>总权重就会驱逐条目
                // （请注意，权重仅用于确定缓存是否超过容量；它对选择下一个应该驱逐哪个条目没有影响。）
                .maximumWeight(100)
                .weigher((Weigher<Integer, Integer>) (key, value) -> key)
                .evictionListener((key, val, removalCause) -> {
                    log.info("淘汰缓存：key:{} val:{}，cause：{}", key, val, removalCause);

                })
                .build();

        // 总权重其实是=所有缓存的权重加起来
        int maximumWeight = 0;
        for (int i = 1; i < 20; i++) {
            cache.put(i, i);
            maximumWeight += i;
        }
        System.out.println("总权重=" + maximumWeight);
        Thread.sleep(500);// 缓存淘汰是异步的

        // 打印还没被淘汰的缓存
        log.info("没被淘汰的缓存:{}", cache.asMap());
    }


    /**
     * 访问后到期（每次访问都会重置时间，也就是说如果一直被访问就不会被淘汰）
     */
    @Test
    public void expireAfterAccessTest() throws InterruptedException {
        Cache<Integer, Integer> cache = Caffeine.newBuilder()
                .expireAfterAccess(1, TimeUnit.SECONDS)
                // 可以指定调度程序来及时删除过期缓存项，而不是等待Caffeine触发定期维护
                // 若不设置scheduler，则缓存会在下一次调用get的时候才会被动删除
                .scheduler(Scheduler.systemScheduler())
                .evictionListener((key, val, removalCause) -> {
                    log.info("淘汰缓存：key:{} val:{}，cause：{}", key, val, removalCause);
                })
                .build();
        cache.put(1, 2);
        System.out.println(cache.getIfPresent(1));
        Thread.sleep(3000);
        System.out.println(cache.getIfPresent(1));// null
    }

    /**
     * 写入后到期
     */
    @Test
    public void expireAfterWriteTest() throws InterruptedException {
        Cache<Integer, Integer> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.SECONDS)
                // 可以指定调度程序来及时删除过期缓存项，而不是等待Caffeine触发定期维护
                // 若不设置scheduler，则缓存会在下一次调用get的时候才会被动删除
                .scheduler(Scheduler.systemScheduler())
                .evictionListener((key, val, removalCause) -> {
                    log.info("淘汰缓存：key:{} val:{}，cause：{}", key, val, removalCause);

                })
                .build();
        cache.put(1, 2);
        Thread.sleep(3000);
        System.out.println(cache.getIfPresent(1));// null
    }

}
