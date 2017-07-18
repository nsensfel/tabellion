parser grammar PropertyParser;
options { tokenVocab = PropertyLexer; }

prog: tag_existing;

tag_existing
   : TAG_EXISTING_KW L_PAREN (tag_item)+ R_PAREN (WS)* sl_formula (WS)* R_PAREN
;

tag_item
   : (WS)* L_PAREN (WS)* ID (WS)+ ID (WS)+ ID (WS)* R_PAREN (WS)*
;

predicate: (WS)* L_PAREN ID ((WS)+ ID)* (WS)* R_PAREN;

/******************************************************************************/
/** Structural Level **********************************************************/
/******************************************************************************/
/**** First Order Expressions *************************************************/
sl_and_operator: (WS)* AND_OPERATOR_KW sl_formula (sl_formula)+ (WS)* R_PAREN;
sl_or_operator: (WS)* OR_OPERATOR_KW sl_formula (sl_formula)+ (WS)* R_PAREN;
sl_not_operator: (WS)* NOT_OPERATOR_KW sl_formula (WS)* R_PAREN;
sl_implies_operator: (WS)* IMPLIES_OPERATOR_KW sl_formula sl_formula (WS)* R_PAREN;

/** Quantified Expressions ****************************************************/
sl_exists_operator:
   (WS)* EXISTS_OPERATOR_KW ID (WS)+ ID sl_formula (WS*) R_PAREN
;

sl_forall_operator:
   (WS)* FORALL_OPERATOR_KW ID (WS)+ ID sl_formula (WS*) R_PAREN
;

/** Special Expressions *******************************************************/
sl_ctl_verifies_operator:
   (WS)* CTL_VERIFIES_OPERATOR_KW ID (WS)* bl_formula (WS)* R_PAREN
;

/**** Formula Definition ******************************************************/
sl_formula:
   predicate
   | sl_and_operator
   | sl_or_operator
   | sl_not_operator
   | sl_implies_operator
   | sl_exists_operator
   | sl_forall_operator
   | sl_ctl_verifies_operator
;

/******************************************************************************/
/** Behavioral Level **********************************************************/
/******************************************************************************/
/**** First Order Expressions *************************************************/
bl_and_operator: (WS)* AND_OPERATOR_KW bl_formula (bl_formula)+ (WS)* R_PAREN;
bl_or_operator: (WS)* OR_OPERATOR_KW bl_formula (bl_formula)+ (WS)* R_PAREN;
bl_not_operator: (WS)* NOT_OPERATOR_KW bl_formula (WS)* R_PAREN;
bl_implies_operator: (WS)* IMPLIES_OPERATOR_KW bl_formula bl_formula (WS)* R_PAREN;

/**** Computation Tree Logic Expressions **************************************/
bl_ax_operator: (WS)* AX_OPERATOR_KW bl_formula (WS)* R_PAREN;
bl_ex_operator: (WS)* EX_OPERATOR_KW bl_formula (WS)* R_PAREN;
bl_ag_operator: (WS)* AG_OPERATOR_KW bl_formula (WS)* R_PAREN;
bl_eg_operator: (WS)* EG_OPERATOR_KW bl_formula (WS)* R_PAREN;
bl_af_operator: (WS)* AF_OPERATOR_KW bl_formula (WS)* R_PAREN;
bl_ef_operator: (WS)* EF_OPERATOR_KW bl_formula (WS)* R_PAREN;
bl_au_operator: (WS)* AU_OPERATOR_KW bl_formula bl_formula (WS)* R_PAREN;
bl_eu_operator: (WS)* EU_OPERATOR_KW bl_formula bl_formula (WS)* R_PAREN;

/**** Formula Definition ******************************************************/
bl_formula:
   predicate
   | bl_and_operator
   | bl_or_operator
   | bl_not_operator
   | bl_implies_operator
   | bl_ax_operator
   | bl_ex_operator
   | bl_ag_operator
   | bl_eg_operator
   | bl_af_operator
   | bl_ef_operator
   | bl_au_operator
   | bl_eu_operator
;
