package com.xiaodao.audit.service;

import com.xiaodao.common.entity.User;
import com.xiaodao.common.respository.UserRepository;
import com.xiaodao.common.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService implements IUserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> list() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void del(Long id) {
        log.info("tx createOperateLog ：{}", TransactionSynchronizationManager.getCurrentTransactionName());
        userRepository.deleteById(id);
    }

    @Override
    public int delAll() {
        userRepository.deleteAll();
        return 0;
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public User queryById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

}
