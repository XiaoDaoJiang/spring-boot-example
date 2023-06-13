package com.xiaodao.dao.jpa.util;
import java.util.Date;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import com.xiaodao.dao.jpa.annotation.SignField;
import com.xiaodao.dao.jpa.entity.User;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SignUtils {

    public static <T> List<String> getSignProperties(T target) {

        Class<?> clazz = target.getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<String> sourceValues = new ArrayList<>();

        for (Field field : fields) {
            if (field.isAnnotationPresent(SignField.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(target);
                    sourceValues.add(String.valueOf(value));
                    // 处理获取到的字段值
                    System.out.println("Field: " + field.getName() + ", Value: " + value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return sourceValues;
    }

    public static String doSign(List<String> sources) {
        return MD5.create().digestHex(String.join("", sources));
    }

    public static void main(String[] args) {
        final User user = new User();
        user.setId(0L);
        user.setName("");
        user.setAge(0);
        user.setGender("");
        user.setSignature("");
        user.setLastModifiedDate(new Date());
        user.setCreatedDate(new Date());

        final List<String> signProperties = getSignProperties(user);
        System.out.println(doSign(signProperties));

    }

    public static void initField(){
        // 指定排除属性过滤器和包含属性过滤器
        String[] excludeProperties = {"country", "city"};
        String[] includeProperties = {"id", "username", "mobile"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludefilter = filters.addFilter();
        // 指定排除属性过滤器：转换成JSON字符串时，排除哪些属性
        excludefilter.addExcludes(excludeProperties);
        PropertyPreFilters.MySimplePropertyPreFilter includefilter = filters.addFilter();
        // 指定包含属性过滤器：转换成JSON字符串时，包含哪些属性
        includefilter.addIncludes(includeProperties);
    }
}
