## caffeine
https://github.com/ben-manes/caffeine
### 简单介绍
caffeine 是一个高性能的本地缓存库， 使用上是受 Guava Cache API 启发，借鉴了 GuavaCache的设计
### 特性
1. 自动加载缓存条目（支持异步）
2. 基于容量大小淘汰
3. 基于时间（上次写入或上次访问,last write or last access）淘汰
4. 缓存的Key可以包装为弱引用
5. 缓存的Value可以包装为弱引用或软引用 
6. 异步刷新（当访问的缓存过期时）
7. 缓存条目过期的通知
8. 传播到外部资源的写入 （write propagated/compute）
9. 统计信息（访问缓存统计指标信息）

## 淘汰算法 W-TinyLFU
1. 通过 Count-min Sketch 降低频率信息的内存消耗
2. 维护一个PK机制保障新上的热点数据能够缓存


LRU（Window Deque） | Segment-LRU

Segment-LRU 分为两部分，一部分为 Protected Deque，一部分为 Probation Deque。
数据首先放入 Window Deque，当 Window Deque 满了之后，会将数据放入 Probation Deque，
当数据再次访问时，会将数据从 Probation Deque 提升到 Protected Deque，当 Protected Deque 满了之后，会有数据降级到 Probation Deque。
数据淘汰时从 Probation Deque 中淘汰，
具体淘汰机制：取ProbationDeque 中的队首和队尾进行 PK，队首数据是最先进入队列的，称为受害者，队尾的数据称为攻击者，比较两者 频率大小，大胜小汰。


## 高性能读写


## spring cache redis 序列化
`GenericJackson2JsonRedisSerializer` 和 `Jackson2JsonRedisSerializer` 都是 Spring Data Redis 中用于序列化和反序列化对象到 Redis 的序列化器。

1. `GenericJackson2JsonRedisSerializer`：
    - 它是基于 Jackson 库的序列化器。
    - 它可以序列化任意类型的对象，并在序列化时包含对象的类信息（类型信息）。
    - 在反序列化时，它使用类信息来恢复对象的类型，以便正确地将数据转换回原始对象类型。
    - 这意味着可以在 Redis 存储中存储多种类型的对象，并在反序列化时正确地还原它们的类型。

2. `Jackson2JsonRedisSerializer`：
    - 也是基于 Jackson 库的序列化器。
    - 它主要用于序列化和反序列化指定的类类型（例如，特定的实体类）。
    - 它不包含对象的类信息（类型信息），因此在反序列化时，必须明确指定要转换的目标类型。

两者的区别主要在于序列化和反序列化时是否包含对象的类信息。`GenericJackson2JsonRedisSerializer` 包含类信息，可以处理多种类型的对象，而 `Jackson2JsonRedisSerializer` 则需要明确指定目标类型。

选择使用哪种序列化器取决于你的具体需求。如果需要在 Redis 存储中存储多种类型的对象，并且希望能够正确地还原对象的类型，那么 `GenericJackson2JsonRedisSerializer` 是一个不错的选择。如果只需要处理特定的类类型，并且可以在反序列化时明确指定目标类型，那么 `Jackson2JsonRedisSerializer` 可能更适合。

## 扩展 Spring Cache
* 替换默认 Cache Manager
* 设置自定义 CacheConfiguration ，使用 RedisCacheManagerBuilderCustomizer
两种方法都是设置对应 CacheName 的 CacheConfiguration 以实现Cache 的特定功能；
* 扩展二级缓存（基于 CompositeCacheManager）
  * caffeine
  * redis
* 扩展 @CacheXXX 注解
  * 实现 cache Key 自定义生成
  * 实现缓存过期时间