package com.xiaodao;

import com.xiaodao.common.entity.User;
import com.xiaodao.common.respository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EventListenerTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    void onPostDelete() {

    }

    @Test
    void onPostInsert() {
        final User user = new User();
        user.setName("jacky");
        user.setAge(23);
        user.setEmail("sxa");

        userRepository.save(user);
    }

    @Test
    void onPostUpdate() {
        userRepository.findById(8L).ifPresent(user -> {
            user.setName("jacky");
            user.setAge(32);
            user.setEmail("xiaodao");
            userRepository.save(user);
        });
    }

}