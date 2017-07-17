parser grammar PropertyParser;
options { tokenVocab = PropertyLexer; }

prog: tag_existing;

tag_existing
   : TAG_EXISTING_KW L_PAREN (tag_item)+ R_PAREN formula R_PAREN
;

tag_item
   : L_PAREN ID ID R_PAREN
;

formula
   : and_operator
   | or_operator
   | not_operator
   | exists_operator
   | forall_operator
   | predicate
;

and_operator: AND_OPERATOR_KW formula (formula)+ R_PAREN;
or_operator: OR_OPERATOR_KW formula (formula)+ R_PAREN;
not_operator: NOT_OPERATOR_KW formula R_PAREN;
exists_operator: EXISTS_OPERATOR_KW ID ID formula R_PAREN;
forall_operator: FORALL_OPERATOR_KW ID ID formula R_PAREN;


predicate: L_PAREN ID (ID)* R_PAREN;
