package com.xiaodao.validation.schema;

import com.typesafe.config.*;
import org.junit.jupiter.api.Test;

/**
 * @author jianghaitao
 * @Classname ConfigTest
 * @Version 1.0.0
 * @Date 2025-01-14 18:25
 * @Created by jianghaitao
 */
public class CommonConfig {

    private Config config;


    // 打印
    public void printSetting(String path) {
        System.out.println("The setting '" + path + "' is: " + config.getString(path));
    }


    @Test
    public void testApplication() {
        config = ConfigFactory.load();
        this.printSetting("simple-app.answer");
    }


    @Test
    public void testJson() {
        config = ConfigFactory.parseResources("schema/mapping.json");
        final ConfigObject root = config.root();
        System.out.println(root);
    }


    @Test
    public void testJson2Hocon() {
        // 1. 加载 JSON 文件
        Config config = ConfigFactory.parseResources("schema/mapping.json");

        // 2. 渲染为 HOCON 格式
        ConfigRenderOptions options = ConfigRenderOptions.defaults()
                .setOriginComments(false)
                .setComments(true)
                .setFormatted(true)  // 格式化输出
                .setJson(false);     // 禁用 JSON 格式，确保输出为 HOCON
        String hoconString = config.root().render(options);

        // 3. 打印 HOCON 字符串
        System.out.println("HOCON Output:");
        System.out.println(hoconString);
    }


    @Test
    public void testHocon() {
        config = ConfigFactory.parseResources("schema/hocon/mapping.conf");
        final ConfigObject root = config.root();
        System.out.println(root);
    }

    @Test
    public void testEqual() {
        Config configJson = ConfigFactory.parseResources("schema/mapping.json");
        final ConfigObject root1 = configJson.root();

        Config configHocon = ConfigFactory.parseResources("schema/hocon/mapping.conf");
        final ConfigObject root2 = configHocon.root();

        System.out.println(root1.equals(root2));
    }


    @Test
    public void testInclude() {
        // 自定义解析选项
        ConfigParseOptions parseOptions = ConfigParseOptions.defaults()
                .setAllowMissing(false); // 允许引用的文件缺失

        ConfigResolveOptions resolveOptions = ConfigResolveOptions.defaults()
                .setAllowUnresolved(false); // 允许未解析的变量


        Config configJson =ConfigFactory.parseResourcesAnySyntax("schema/hocon/main.conf", parseOptions);
        //Config configJson = ConfigFactory.parseResources("schema/hocon/main.conf", parseOptions);
        final ConfigObject root1 = configJson.root();

        System.out.println(root1);
    }

}
