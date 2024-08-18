package com.xiaodao.antlr4.demo.expr;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaodaojiang
 * @Classname EvalVisitor
 * @Version 1.0.0
 * @Date 2024-04-27 16:17
 * @Created by xiaodaojiang
 */
public class EvalVisitor extends LabeledExprBaseVisitor<Integer> {

    Map<String, Integer> memory = new HashMap<>();


    /* expr op=('*'|'/') expr */
    @Override
    public Integer visitMulDiv(LabeledExprParser.MulDivContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        if (ctx.op.getType() == LabeledExprParser.MUL)
            return left * right;
        return left / right;
    }

    /* expr op=('+'|'-') expr */
    @Override
    public Integer visitAddSub(LabeledExprParser.AddSubContext ctx) {
        Integer left = visit(ctx.expr(0));
        Integer right = visit(ctx.expr(1));
        if (ctx.op.getType() == LabeledExprParser.SUB) {
            return left - right;
        }

        return left + right;
    }

    /* ID '=' expr NEWLINE */
    @Override
    public Integer visitAssign(LabeledExprParser.AssignContext ctx) {
        String id = ctx.ID().getText();
        final Integer value = visit(ctx.expr());
        memory.put(id, value);
        return value;
    }

    /* expr NEWLINE */
    @Override
    public Integer visitPrintExpr(LabeledExprParser.PrintExprContext ctx) {
        final Integer value = visit(ctx.expr());
        System.out.println(value);

        return 0;
    }

    /* ID */
    @Override
    public Integer visitId(LabeledExprParser.IdContext ctx) {
        final String id = ctx.ID().getText();
        if (memory.containsKey(id)) {
            return memory.get(id);
        }

        return 0;
    }


    @Override
    public Integer visitInt(LabeledExprParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    @Override
    public Integer visitParens(LabeledExprParser.ParensContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Integer visitClear(LabeledExprParser.ClearContext ctx) {
        memory.clear();
        return 0;
    }
}
