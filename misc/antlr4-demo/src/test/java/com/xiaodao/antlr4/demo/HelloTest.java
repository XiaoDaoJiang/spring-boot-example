package com.xiaodao.antlr4.demo;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * 测试hello.g4
 *
 * @author jianghaitao
 * @Classname HelloTest
 * @Version 1.0.0
 * @Date 2024/3/26 9:50
 * @Created by jianghaitao
 */
public class HelloTest {
    public static void main(String[] args) throws Exception {
        HelloLexer lexer = new HelloLexer(CharStreams.fromString("hello 123"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HelloParser parser = new HelloParser(tokens);
        ParseTree tree = parser.s();
        System.out.println(tree.toStringTree(parser));
    }
}
