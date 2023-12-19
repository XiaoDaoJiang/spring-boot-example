## 单节点分布式锁

1. 加锁
    ```redis
    SET resource_name my_random_value NX EX 300
    ```

2. 解锁
    ```shell 
        if redis.call("get",KEYS[1]) == ARGV[1] 
        then
            return redis.call("del",KEYS[1])
        else
            return 0
        end
    ```