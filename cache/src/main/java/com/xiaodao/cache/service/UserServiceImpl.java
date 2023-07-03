package com.xiaodao.cache.service;

import com.xiaodao.common.entity.User;
import com.xiaodao.common.respository.UserRepository;
import com.xiaodao.common.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

//这个注解表示类中共同放入到 user 模块中
@CacheConfig(cacheNames = "user")
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * @return
     * @Cacheable 1、先查缓存，
     * 2、若没有缓存，就执行方法
     * 3、若有缓存。则返回，不执行方法
     * <p>
     * 所以@Cacheable 不能使用result
     */
    @Override
    @Cacheable(key = "#root.methodName")
    public List<User> list() {
        return userRepository.findAll();
    }

    /**
     * @param user
     * @return
     * @CachePut 更新并刷新缓存
     * 1、先调用目标方法
     * 2、把结果缓存
     */
    @Override
    @CachePut(key = "#result.id", unless = "#result.id == null")
    public User save(User user) {
        user.setCreateBy(1L);
        user.setCreateTime(LocalDateTime.now());
        user.setModifyBy(1L);
        user.setModifyTime(LocalDateTime.now());
        userRepository.save(user);
        System.out.println("user=" + user);
        return user;
    }

    /**
     * 删除指定key 的缓存
     * beforeInvocation=false  缓存的清除是否在方法之前执行
     * 默认是在方法之后执行
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @CacheEvict(key = "#id", beforeInvocation = true)
    public void del(Long id) throws Exception {
        userRepository.deleteById(id);
    }

    /**
     * 删除所有缓存
     *
     * @return
     * @throws Exception
     */
    @Override
    @CacheEvict(allEntries = true)
    public int delAll() throws Exception {
        userRepository.deleteAll();
        return 1;
    }

    @Override
    @CachePut(key = "#result.id", unless = "#result.id == null")
    public User update(User user) {
        user.setModifyBy(1L);
        user.setModifyTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    @Cacheable(key = "#id", condition = "#id > 0")
    public User queryById(Long id) {
        return userRepository.findById(id).get();
    }

    /**
     * @param name
     * @return
     * @Caching复杂组合缓存注解
     */
    @Override
    @Caching(cacheable = {@Cacheable(key = "#title")},
            put = {@CachePut(key = "#result.id"),
//            @CachePut(key = "T(String).valueOf(#page).concat('-').concat(#pageSize)")
                    @CachePut(key = "T(String).valueOf('tag').concat('-').concat(#result.tagId)")
            })
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    @Cacheable(key = "T(String).valueOf('tag').concat('-').concat(#tagId)")
    public User queryByTag(Long tagId) {
        return null;
    }
}
