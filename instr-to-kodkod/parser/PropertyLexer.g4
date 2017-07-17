lexer grammar PropertyLexer;

TAG_EXISTING_KW: '(tag_existing';
AND_OPERATOR_KW: '(and';
OR_OPERATOR_KW: '(or';
NOT_OPERATOR_KW: '(not';
EXISTS_OPERATOR_KW: '(exists';
FORALL_OPERATOR_KW: '(forall';
ID: [a-zA-Z0-9_]+;
L_PAREN : '(';
R_PAREN : ')';

WS: [ \t\r\n]+ -> skip;
