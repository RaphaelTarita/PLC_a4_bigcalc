grammar BigCalcProg;

program
    : statement+ EOF
    ;

statement
    : expression ';' # stmtexpr
    | assignment ';' # stmtasgn
    ;

assignment
    : ID '=' expression
    ;

expression
        : '(' expression ')'                   # parentheses
        | expression op=('*' | '/') expression # mulDiv
        | expression op=('+' | '-') expression # addSub
        | ID                                   # var
        | Number                               # num
        ;

Number
        : Digit* '.' Digit+
        | Digit+
        ;

Digit
        : [0-9]
        ;

ID
        : [a-zA-Z] Digit*
        ;

WS      : [ \t\r\n\u000C]+ -> skip
        ;

COMMENT
        :   '/*' .*? '*/' -> skip
        ;

LINE_COMMENT
        : '//' ~[\r\n]* -> skip
        ;