package com.xiaodao.integratetest.mockito;

import com.xiaodao.unittest.util.Foo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

/**
 * mockito static method test
 *
 * @author jianghaitao
 * @Classname MockStaticMethodTest
 * @Version 1.0.0
 * @Date 2024-06-14 10:49
 * @Created by jianghaitao
 * @since 3.4.0
 */
@ExtendWith(MockitoExtension.class)
public class MockStaticMethodTest {


    @Test
    public void testMockStaticMethod() {
        assertEquals("foo", Foo.method());
        try (MockedStatic<Foo> mocked = mockStatic(Foo.class)) {
            mocked.when(Foo::method).thenReturn("bar");
            assertEquals("bar", Foo.method());
            mocked.verify(Foo::method);
        }
        assertEquals("foo", Foo.method());
    }

    MockedStatic<Foo> fooMockedStatic;

    @BeforeEach
    public void setup() {
        fooMockedStatic = mockStatic(Foo.class);
    }

    @Test
    public void testMockStaticMethodBefore() {
        fooMockedStatic.when(Foo::method).thenReturn("bar");
        assertEquals("bar", Foo.method());
        fooMockedStatic.verify(Foo::method);
    }

    @AfterEach
    public void tearDown() {
        if (!fooMockedStatic.isClosed()) {
            fooMockedStatic.close();
        }
        assertEquals("foo", Foo.method());
    }

}
