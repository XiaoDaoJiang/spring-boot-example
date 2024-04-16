// Generated from E:/IdeaProjects/learn/spring-boot-example/misc/antlr4-demo/src/main/resources/Hello.g4 by ANTLR 4.13.1
 package com.xiaodao.antlr4.demo; 
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link HelloParser}.
 */
public interface HelloListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link HelloParser#s}.
	 * @param ctx the parse tree
	 */
	void enterS(HelloParser.SContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#s}.
	 * @param ctx the parse tree
	 */
	void exitS(HelloParser.SContext ctx);
}