package com.xiaodao.antlr4.demo.tour;

import org.antlr.v4.runtime.TokenStream;

/**
 * @author xiaodaojiang
 * @Classname ExtractInterfaceListener
 * @Version 1.0.0
 * @Date 2024-04-27 21:44
 * @Created by xiaodaojiang
 */
public class ExtractInterfaceListener extends Java8ParserBaseListener {

    Java8Parser parser;

    public ExtractInterfaceListener(Java8Parser parser) {
        this.parser = parser;
    }

    @Override
    public void enterClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
        System.out.println("interface I" + ctx.normalClassDeclaration().Identifier() + "{");
    }

    @Override
    public void exitClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
        System.out.println("}");
    }


    @Override
    public void enterMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
        String type = ctx.methodHeader().result().getText();
        // String methodName = ctx.methodHeader().methodDeclarator().Identifier().getText();

        // 直接使用解析会跳过空白字符，导致无法正确输出参数列表：intx,Stringy != int x,String y
        // String args = ctx.methodHeader().methodDeclarator().formalParameterList().getText();

        TokenStream tokens = parser.getTokenStream();

        String args = tokens.getText(ctx.methodHeader().methodDeclarator());

        System.out.println("\t" + type + " "  + args + ";");

    }
}
