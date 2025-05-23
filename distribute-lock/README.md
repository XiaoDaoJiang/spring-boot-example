## 分布式系统中的锁
互斥
高可用
高性能
可重入
非阻塞

## 实现方案
1. 数据库排他锁，主键索引/唯一索引等确保互斥性，缺点是性能差，不具有锁失效机制
2. **基于redis的分布式锁**
3. 基于zookeeper的分布式锁
4. 基于etcd的分布式锁

## 使用场景
保证业务的幂等性（防止重复提交）
保证数据并发场景下的一致性，正确性（秒杀）

## 基于redis的分布式锁
1. 加锁
2. 解锁
3. 锁失效机制与锁续期
4. 锁重入机制