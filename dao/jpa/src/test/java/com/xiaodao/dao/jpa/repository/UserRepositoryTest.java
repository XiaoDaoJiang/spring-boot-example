package com.xiaodao.dao.jpa.repository;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.xiaodao.dao.jpa.SignListener;
import com.xiaodao.dao.jpa.config.EntityManagerConfig;
import com.xiaodao.dao.jpa.entity.User;
import com.xiaodao.dao.jpa.service.UserService;
import org.assertj.core.util.Lists;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.List;

// @DataJpaTest
// @Commit
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@Transactional
@SpringBootTest
@Import(EntityManagerConfig.class)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    @Qualifier("entityManagerPrimary")
    private EntityManager entityManager;


    @Autowired
    private UserService userService;

    @BeforeEach
    public void init() {
               // SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
               // sessionFactory.withOptions().interceptor(new SignInterceptor());
                SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
                final EventListenerRegistry service = sessionFactory
                        .getServiceRegistry()
                        .getService(EventListenerRegistry.class);
                final SignListener signListener = new SignListener();
                service.appendListeners(EventType.PRE_UPDATE, signListener);
                service.appendListeners(EventType.PRE_INSERT, signListener);
    }

    @Test
    void insert() {
        // 14acf85951c776f3b2b909d761f20636a97725297668db03212d1361e6501ea8
        User user = new User();
        user.setName(RandomUtil.randomString("user", 4));
        user.setAge(RandomUtil.randomInt(50));
        user.setGender(String.valueOf(RandomUtil.randomInt(3)));

        // userRepository.save(user);
        final User saveUser = userService.createUser(user);

        final User queryByEM = entityManager.find(User.class, saveUser.getId());
        Assertions.assertEquals(saveUser, queryByEM);

    }

    @Test
    void persist() {
        User user = new User();
        user.setName(RandomUtil.randomString("user", 4));
        user.setAge(RandomUtil.randomInt(50));
        user.setGender(String.valueOf(RandomUtil.randomInt(3)));

        entityManager.persist(user);
    }

    @Test
    void saveAll() {
        User user1 = new User();
        user1.setName(RandomUtil.randomString("user", 4));
        user1.setAge(RandomUtil.randomInt(50));
        user1.setGender(String.valueOf(RandomUtil.randomInt(3)));

        User user2 = new User();
        user2.setName(RandomUtil.randomString("user", 4));
        user2.setAge(RandomUtil.randomInt(50));
        user2.setGender(String.valueOf(RandomUtil.randomInt(3)));

        userRepository.saveAll(Lists.list(user1, user2));
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
                user.setAge(RandomUtil.randomInt(50));
            }
        }
        userRepository.saveAll(all);
    }


    @Test
    void query() {
        final List<User> all = userRepository.findAll();

        final List<User> resultList = entityManager.createQuery("from User ", User.class).getResultList();

        Assertions.assertIterableEquals(all, resultList);
        System.out.println(all);
    }

    @Test
    void json(){
        User user = new User();
        user.setId(3L);
        user.setName(RandomUtil.randomString("user", 4));
        user.setAge(RandomUtil.randomInt(50));
        user.setGender(String.valueOf(RandomUtil.randomInt(3)));
        user.setCreatedDate(new Date());

        System.out.println(JSON.toJSONString(user));
    }

}