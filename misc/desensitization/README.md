1. 利用 Jackson 注解 JacksonAnnotationsInside 扩展自定义 @ValueDesensitize 注解
   1. 通过 JacksonAnnotationsInside 注解，可以将自定义注解标记为 Jackson 注解的一部分，从而可以在序列化时生效
   2. 通过 Jackson 的序列化器@JsonSerialize，可以实现自定义的序列化逻辑
   3. 通过声明 handler 属性，可以指定自定义的脱敏处理器，实现脱敏规则
```java
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside // 此注解是其他所有 jackson 注解的元注解，打上了此注解的注解表明是 jackson 注解的一部分
@JsonSerialize(using = StringDesensitizeSerializer.class) // 指定序列化器
public @interface ValueDesensitize {

    /**
     * 脱敏处理器
     */
    @SuppressWarnings("rawtypes")
    Class<? extends Masker> handler();

}
```
2. 定义自定义脱敏规则注解（用ValueDesensitize进行注解），以此来进行扩展，每种脱敏规则都有相应的基础脱敏处理器
如：RegexDesensitize 注解，对应的基础脱敏处理器为 DefaultRegexMasker，规则定义在注解中，处理逻辑定义在处理器中
```java
@Documented
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@ValueDesensitize(handler = DefaultRegexMasker.class)
public @interface RegexDesensitize {

    /**
     * 匹配的正则表达式（默认匹配所有）
     */
    String regex() default "^[\\s\\S]*$";

    /**
     * 替换规则，会将匹配到的字符串全部替换成 replacer
     *
     * 例如：regex=123; replacer=******
     * 原始字符串 123456789
     * 脱敏后字符串 ******456789
     */
    String replacer() default "******";
}
```