grammar LabeledExpr;
import CommonLexerRules;

/* 起始规则，语法分析的起点 */
prog: stat+;

stat: expr NEWLINE        # printExpr
    | ID '=' expr NEWLINE # assign
    | NEWLINE             # blank
    | CLEAR NEWLINE       # clear
    ;
expr: expr op=('*'|'/') expr # MulDiv
    | expr op=('+'|'-') expr # AddSub
    | INT                    # int
    | ID                     # id
    | '(' expr ')'           # parens
    ;
MUL:  '*';
DIV:  '/';
ADD:  '+';
SUB:  '-';
CLEAR : 'clear';