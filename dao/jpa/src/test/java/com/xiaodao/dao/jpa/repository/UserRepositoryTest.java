package com.xiaodao.dao.jpa.repository;

import cn.hutool.core.util.RandomUtil;
import com.xiaodao.dao.jpa.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import java.util.List;

@DataJpaTest
@Commit
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void insert() {
        User user = new User();
        user.setName(RandomUtil.randomString("user", 4));
        user.setAge(RandomUtil.randomInt(50));
        user.setGender(String.valueOf(RandomUtil.randomInt(2)));

        userRepository.save(user);
    }


    @Test
    void delete() {
        final List<User> all = userRepository.findAll();
        if (!all.isEmpty()) {
            userRepository.delete(all.get(0));
        }
    }

    @Test
    void update() {
        final List<User> all = userRepository.findAll();
        if (!all.isEmpty()) {
            for (User user : all) {
            }
        }
    }


    @Test
    void query() {

    }

}