package com.xiaodao.unittest;

import com.xiaodao.common.entity.User;
import com.xiaodao.unittest.service.TestUserServiceImpl;
import com.xiaodao.unittest.service.UserHelper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author jianghaitao
 * @Classname UserBaseServiceTest
 * @Version 1.0.0
 * @Date 2024-04-28 13:55
 * @Created by jianghaitao
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserBaseServiceTest {

    /**
     * @SpyBean = @Spy + @Autowired
     */
    @SpyBean
    @Autowired
    private TestUserServiceImpl userBaseService;


    @MockBean
    private UserHelper userHelper;

    @Spy
    @InjectMocks
    private TestUserServiceImpl userBaseServiceSpy;

    @SpyBean
    private TestUserServiceImpl userBaseServiceSpyBean;

    @Spy
    @Autowired
    private TestUserServiceImpl userBaseServiceSpyAutowired;


    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void test() {
        final TestUserServiceImpl testUserService = applicationContext.getBean(TestUserServiceImpl.class);
        User user = new User();
        userBaseServiceSpy.update(user);
        verify(userBaseServiceSpy, times(1)).update(any());
        verify(userBaseServiceSpy, times(1)).queryById(any());
        verify(userBaseServiceSpy, times(1)).save(any());

        verify(userBaseServiceSpy, Exception)
    }

    @Test
    public void testSpyAndInjectMocks() {
        userBaseServiceSpy.list();
    }

}
