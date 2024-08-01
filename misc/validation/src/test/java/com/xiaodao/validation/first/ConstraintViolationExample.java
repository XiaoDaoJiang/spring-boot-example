package com.xiaodao.validation.first;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Set;

public class ConstraintViolationExample {

    public static class User {

        @NotNull(message = "Username cannot be null")
        @Size(min = 5, message = "Username must be at least 5 characters")
        private String username;

        public User(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }


    /**
     * getRootBean():
     * 返回被校验对象的根对象。
     * <p>
     * getRootBeanClass():
     * 返回被校验对象的根对象的类。
     * <p>
     * getLeafBean():
     * 返回发生约束违例的叶对象。如果违例发生在根对象上，则返回根对象。
     * <p>
     * getPropertyPath():
     * 返回一个 Path 对象，该对象表示发生违例的属性路径。
     * <p>
     * getMessage():
     * 返回错误消息。
     * <p>
     * getMessageTemplate():
     * 返回错误消息模板（未解析的消息模板）。
     * <p>
     * getConstraintDescriptor():
     * 返回描述违反的约束的 ConstraintDescriptor。
     * <p>
     * getInvalidValue():
     * 返回导致约束违例的无效值。
     * <p>
     * getExecutableParameters():
     * 如果违例发生在方法或构造函数参数上，则返回参数的值数组；否则返回 null。
     * <p>
     * getExecutableReturnValue():
     * 如果违例发生在方法或构造函数返回值上，则返回返回值；否则返回 null。
     * <p>
     * unwrap(Class<T> type):
     * 将 ConstraintViolation 对象转换为所提供类型的实例。
     */
    public static void main(String[] args) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        User user = new User("abc"); // Invalid username

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        for (ConstraintViolation<User> violation : violations) {
            System.out.println("Root Bean: " + violation.getRootBean());
            System.out.println("Root Bean Class: " + violation.getRootBeanClass());
            System.out.println("Leaf Bean: " + violation.getLeafBean());
            System.out.println("Property Path: " + violation.getPropertyPath());
            System.out.println("Message: " + violation.getMessage());
            System.out.println("Message Template: " + violation.getMessageTemplate());
            // violation.getConstraintDescriptor() 会返回约束的详细信息，比如
            // @Size(min = 5, message = "Username must be at least 5 characters")
            // 可以获取payload,判断不同校验属性的定制上下文
            System.out.println("Constraint Descriptor: " + violation.getConstraintDescriptor());
            System.out.println("Invalid Value: " + violation.getInvalidValue());
            System.out.println("Executable Parameters: " + Arrays.toString(violation.getExecutableParameters()));
            System.out.println("Executable Return Value: " + violation.getExecutableReturnValue());
        }

        // 假设用户对象的 username 属性值为 "abc"（不符合 @Size 约束），输出结果如下：
        //
        // getRootBean(): 返回整个被校验的 User 对象。
        // getRootBeanClass(): 返回 User 类。
        // getLeafBean(): 返回 User 对象，因为违例发生在根对象上。
        // getPropertyPath(): 返回 username，即发生违例的属性路径。
        // getMessage(): 返回解析后的错误消息，例如 "Username must be at least 5 characters".
        //         getMessageTemplate(): 返回未解析的消息模板，例如 "{javax.validation.constraints.Size.message}".
        //         getConstraintDescriptor(): 返回描述 @Size 约束的 ConstraintDescriptor 对象。
        // getInvalidValue(): 返回导致违例的无效值 "abc".
        //         getExecutableParameters(): 返回 null，因为违例不发生在方法或构造函数参数上。
        // getExecutableReturnValue(): 返回 null，因为违例不发生在方法或构造函数返回值上。
    }
}
