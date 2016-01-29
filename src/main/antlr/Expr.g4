grammar Expr;

prog:
         block;

block:
         (statement NEWLINE)*(statement)?;

list:
         LISTBEGIN (expr SEPARATOR)*(expr)? LISTEND
    |    LISTBEGIN begin=expr colon=':' end=expr colon=':' step=expr LISTEND
    ;

statement:
         id=ID '=' right=expr
    |    id=ID '[' index=expr ']' '=' right=expr
    |    keyword=(PRINT|WRITE) sub=expr
    |    keyword=(IF|WHILE) condition=expr NEWLINE loop=block (elsedir=ELSE NEWLINE elseloop=block)? 'YOUR TIME IS UP'
    ;

expr:
         keyword=(READINT|READDOUBLE|READSTRING|RANDOM)
    |    keyword=FLOOR right=expr
    |    left=expr op=POW right=expr
    |    left=expr op=(TIMES|DIVIDE|MOD) right=expr
    |    left=expr op=(PLUS|MINUS) right=expr
    |    number=NUM
    |    character=CHAR
    |    string=STRING
    |    id=ID
    |    list_expr=list
    |    id=ID '[' index=expr ']'
    |    '(' sub=expr ')'
    |    left=expr comparator=(LT|LTE|GT|GTE|EQ) right=expr
    |    bang=BANG right=expr
    |    left=expr logicop=(AND|OR) right=expr
    ;

PRINT: 'SUPERSLAM';
WRITE: 'SLAM';
IF: 'HUSTLE';
WHILE: 'MY TIME IS NOW';
FLOOR: 'THE UNDERTAKER';
ELSE: 'WHAT NOW';
READINT: 'YOU CANT SEE ME';
READDOUBLE: 'THE CHAMP';
READSTRING: 'MAKE IT LOUD';
RANDOM: 'FIVE KNUCKLE SHUFFLE';
ID: [a-zA-Z_$][a-zA-Z_$0-9]*;
LISTBEGIN: '[';
SEPARATOR: ',';
LISTEND: ']';
NEWLINE: [\r\n]+;
NUM: [0-9]+([.][0-9]+)?;
CHAR: ['].['];
STRING: ["](.)*?["];
PLUS: '+';
MINUS: '-';
TIMES: '*';
DIVIDE: '/';
POW: '^';
MOD: '%';
LT: '<';
LTE: '<=';
GT: '>';
GTE: '>=';
EQ: '==';
WS: (' ' | '\t')+ -> skip;
BANG: '!';
AND: '&&';
OR: '||';
