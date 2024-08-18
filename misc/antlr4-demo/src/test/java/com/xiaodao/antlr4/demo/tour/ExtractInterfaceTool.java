package com.xiaodao.antlr4.demo.tour;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;

/**
 * @author xiaodaojiang
 * @Classname ExtractInterfaceTool
 * @Version 1.0.0
 * @Date 2024-04-27 21:55
 * @Created by xiaodaojiang
 */
public class ExtractInterfaceTool {

    public static void main(String[] args) throws IOException {
        CharStream input = CharStreams.fromFileName("E:\\project\\learn\\spring-boot-example\\misc\\antlr4-demo\\src\\test\\java\\com\\xiaodao\\antlr4\\demo\\tour\\Demo.java");
        Java8Lexer lexer = new Java8Lexer(input);
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final Java8Parser parser = new Java8Parser(tokenStream);
        final Java8Parser.CompilationUnitContext compilationUnitContext = parser.compilationUnit();

        final ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        final ExtractInterfaceListener extractInterfaceListener = new ExtractInterfaceListener(parser);
        parseTreeWalker.walk(extractInterfaceListener, compilationUnitContext);
    }

}
