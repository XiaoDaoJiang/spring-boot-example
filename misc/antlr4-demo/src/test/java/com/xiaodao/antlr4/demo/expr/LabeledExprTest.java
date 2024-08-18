package com.xiaodao.antlr4.demo.expr;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

/**
 * @author xiaodaojiang
 * @Classname LabeledExprTest
 * @Version 1.0.0
 * @Date 2024-04-27 16:30
 * @Created by xiaodaojiang
 */
public class LabeledExprTest {

    public static void main(String[] args) throws IOException {
        ANTLRFileStream input = new ANTLRFileStream("E:\\project\\learn\\spring-boot-example\\misc\\antlr4-demo\\src\\main\\resources\\expr.text");
        LabeledExprLexer labeledExprLexer = new LabeledExprLexer(input);
        final CommonTokenStream tokenStream = new CommonTokenStream(labeledExprLexer);
        final LabeledExprParser labeledExprParser = new LabeledExprParser(tokenStream);
        final EvalVisitor evalVisitor = new EvalVisitor();
        evalVisitor.visit(labeledExprParser.prog());
    }
}
