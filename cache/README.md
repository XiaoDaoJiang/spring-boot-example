## Redis

### 持久化（数据丢失）

* RDB（redis database backup file），也被叫做 redis 数据快照。将内存中的数据记录到磁盘上，用以服务重启、同步备份、故障恢复等，默认保存在当前运行目录，快照文件为RDB文件：dump.rdb

    1. 执行时机

        * 执行 save 命令，同步保存操作，阻塞主线程
        * 执行 bgsave 命令，fork 一个子进程执行，不阻塞主线程
        * redis server 停机时执行一次 save
        * 触发 rdb 配置的条件（redis.conf 中 save 60 2000 等）执行 bgsave

    2. 原理
       save：主进程执行RDB，这个过程中其它所有命令都会被阻塞。只有在数据迁移时可能用到。
       bgsave：开始时会fork主进程得到子进程，子进程共享主进程的内存数据。完成fork后读取内存数据并写入 RDB 文件。
       fork采用的是copy-on-write技术：
       ● 当主进程执行读操作时，访问共享内存；
       ● 当主进程执行写操作时，则会拷贝一份数据，执行写操作（不会影响到子进程执行 RDB，数据有丢失风险）。

    3. 总结
       RDB方式bgsave的基本流程？
       ● fork主进程得到一个子进程，共享内存空间
       ● 子进程读取内存数据并写入新的RDB文件
       ● 用新RDB文件替换旧的RDB文件
       RDB会在什么时候执行？save 60 1000代表什么含义？
       ● 默认是服务停止时
       ● 代表60秒内至少执行1000次修改则触发RDB
       RDB的缺点？
       ● RDB执行间隔时间长，两次RDB之间写入数据有丢失的风险
       ● fork子进程、压缩、写出RDB文件都比较耗时

    4. 相关配置(redis.conf)

       ```properties
       # 900秒内，如果至少有1个key被修改，则执行bgsave ， 如果是save "" 则表示禁用RDB
       save 900 1  
       save 300 10  
       save 60 10000
       
       # 是否压缩 ,建议不开启，压缩也会消耗cpu，磁盘的话不值钱
       rdbcompression yes
       
       # RDB文件名称
       dbfilename dump.rdb
       
       # 文件保存的路径目录
       dir ./
       ```

* AOF

    1. 相关配置
       AOF默认是关闭的，需要修改redis.conf配置文件来开启AOF：

       ```properties
          # 是否开启AOF功能，默认是no
          appendonly yes
          # AOF文件的名称
          appendfilename "appendonly.aof"
       ```

       AOF的命令记录的频率也可以通过redis.conf文件来配：

       ```properties
              # 表示每执行一次写命令，立即记录到AOF文件
              appendfsync always
              # 写命令执行完先放入AOF缓冲区，然后表示每隔1秒将缓冲区数据写到AOF文件，是默认方案
              appendfsync everysec
              # 写命令执行完先放入AOF缓冲区，由操作系统决定何时将缓冲区内容写回磁盘
              appendfsync no
       ```

    2. 原理
       AOF全称为Append Only File（追加文件）。Redis处理的每一个写命令都会记录在AOF文件，可以看做是命令日志文件。

       AOF 持久化功能的实现可以简单分为 5 步：

        1. **命令追加（append）**：所有的写命令会追加到 AOF 缓冲区中。
        2. **文件写入（write）**：将 AOF 缓冲区的数据写入到 AOF 文件中。这一步需要调用`write`函数（系统调用），`write`将数据写入到了系统内核缓冲区之后直接返回了（延迟写）。注意！！！此时并没有同步到磁盘。
        3. **文件同步（fsync）**：AOF 缓冲区根据对应的持久化方式（ `fsync` 策略）向硬盘做同步操作。这一步需要调用 `fsync` 函数（系统调用）， `fsync` 针对单个文件操作，对其进行强制硬盘同步，`fsync` 将阻塞直到写入磁盘完成后返回，保证了数据持久化。
        4. **文件重写（rewrite）**：随着 AOF 文件越来越大，需要定期对 AOF 文件进行重写，达到压缩的目的。
        5. **重启加载（load）**：当 Redis 重启时，可以加载 AOF 文件进行数据恢复。

    3. AOF文件重写

       随着运行时间过长，记录的命令越来越多，aof文件越来越大，可以通过重写将原来旧 aof 文件 替换为 “压缩”后的新 aof 文件。可通过 bgrewriteaof 命令或以下配置自动触发重写。

       ```properties
       # AOF文件比上次文件 增长超过多少百分比则触发重写（将此值设置为 0 将禁用自动 AOF 重写。默认值为 100）
       auto-aof-rewrite-percentage 100
       # AOF文件体积最小多大以上才触发重写 （如果 AOF 文件大小小于该值，则不会触发 AOF 重写。默认值为 64 MB）
       auto-aof-rewrite-min-size 64mb
       ```

       rdb + aof mixed

       由于 RDB 和 AOF 各有优势，于是，Redis 4.0 开始支持 RDB 和 AOF 的混合持久化（默认关闭，可以通过配置项 `aof-use-rdb-preamble` 开启）。

       如果把混合持久化打开，AOF 重写的时候就直接把 RDB 的内容写到 AOF 文件开头。这样做的好处是可以结合 RDB 和 AOF 的优点, 快速加载同时避免丢失过多的数据。当然缺点也是有的， AOF 里面的 RDB 部分是压缩格式不再是 AOF 格式，可读性较差。

    4.



### 主从

主从复制（读写分离）

replicaof ip port

### 哨兵


### 分片集群



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