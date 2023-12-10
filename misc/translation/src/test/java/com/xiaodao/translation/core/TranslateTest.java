package com.xiaodao.translation.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaodao.translation.core.annotation.DicField;
import com.xiaodao.translation.core.annotation.EnumDicField;
import com.xiaodao.translation.core.annotation.IndexDicField;
import com.xiaodao.translation.core.translator.DicEnum;
import com.xiaodao.translation.util.TranslationUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

public class TranslateTest {

    @Test
    public void test() throws JsonProcessingException {
        // 准备参数
        DesensitizeDemo desensitizeDemo = new DesensitizeDemo();
        desensitizeDemo.setSex("1");
        desensitizeDemo.setSex1("2");
        desensitizeDemo.setOrigin("32");

        DesensitizeDemo desensitizeDemo1 = new DesensitizeDemo();
        desensitizeDemo1.setSexName("女");
        desensitizeDemo1.setSexName1("男");
        desensitizeDemo1.setOrigin("322");


        DesensitizeDemo desensitizeDemo2 = new DesensitizeDemo();
        desensitizeDemo2.setSex("132");
        desensitizeDemo2.setSex1("232");
        desensitizeDemo2.setOrigin("322");

        ObjectMapper objectMapper = new ObjectMapper();

        // 调用
        final String content = objectMapper.writeValueAsString(desensitizeDemo);
        System.out.println(desensitizeDemo + "序列化结果" + content);

        String content1 = objectMapper.writeValueAsString(desensitizeDemo1);
        System.out.println(desensitizeDemo1 + "序列化结果" + content1);

        final String x = objectMapper.writeValueAsString(desensitizeDemo2);
        System.out.println(desensitizeDemo2 + "序列化结果" + x);


        // DesensitizeDemo d = objectMapper.readValue(content, DesensitizeDemo.class);
        // 断言
    }

    @Data
    public static class DesensitizeDemo {

        @EnumDicField(dicEnum = DicEnum.SexDicEnum.class, dicField = @DicField(ref = "sexName",defaultVal = "未知"))
        private String sex;
        private String sexName;


        @IndexDicField(dicKeys = {"1", "2"}, dicValues = {"男", "女"},dicField = @DicField(ref = "sexName1",defaultVal = "未知"))
        private String sex1;
        private String sexName1;


        private String origin;
    }


    @Test
    public void testUtil() {
        // 准备参数
        DesensitizeDemo desensitizeDemo = new DesensitizeDemo();
        desensitizeDemo.setSex("1");
        desensitizeDemo.setSex1("2");
        desensitizeDemo.setOrigin("32");

        TranslationUtil.scan(desensitizeDemo);

        System.out.println(desensitizeDemo);
    }
}
