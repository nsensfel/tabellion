parser grammar PropertyParser;

options
{
   tokenVocab = PropertyLexer;
}

@header
{
   /* FIXME: Finer imports */
   import kodkod.ast.*;

   import kodkod.engine.*;

   import kodkod.instance.*;
}

@members
{
   /* of the class */
}

prog:
   tag_existing
;

tag_existing:
   (WS)* TAG_EXISTING_KW
      L_PAREN
         (tag_item)+
      R_PAREN
   (WS)* sl_formula
   (WS)* R_PAREN
;

tag_item:
   (WS)* L_PAREN
   (WS)* var=ID
   (WS)+ type=ID
   (WS)+ tag=ID
   (WS)* R_PAREN
   (WS)*
;

id_list
   returns [List<Variable> list]:

   @init
   {
      final List<Variable> result = new ArrayList<Variable>();
   }

   (
      (WS)+
      var=ID
      {
         result.add(VARIABLE_SET.get_variable($var.text));
      }
   )*

   {
      $list = result;
   }
;

/******************************************************************************/
/** Structural Level **********************************************************/
/******************************************************************************/

sl_predicate
   return [Formula result]:

   (WS)* L_PAREN
      ID
      id_list
   (WS)* R_PAREN

   {
      /* TODO */
   }
;

sl_non_empty_formula_list
   returns [List<Formula> list]:

   @init
   {
      final List<Formula> result = new ArrayList<Formula>();
   }

   (
      sl_formula

      {
         result.add($sl_formula);
      }
   )+

   {
      $list = result;
   }
;

/**** First Order Expressions *************************************************/
sl_and_operator
   returns [Formula result]:

   (WS)* AND_OPERATOR_KW
      sl_formula
      sl_non_empty_formula_list
   (WS)* R_PAREN

   {
      /* TODO */
      $result = $sl_formula.and($sl_non_empty_formula_list);
   }
;

sl_or_operator
   returns [Formula result]:

   (WS)* OR_OPERATOR_KW
      sl_formula
      sl_non_empty_formula_list
   (WS)* R_PAREN

   {
      /* TODO */
      $result = $sl_formula.or($sl_non_empty_formula_list);
   }
;

sl_not_operator
   returns [Formula result]:

   (WS)* NOT_OPERATOR_KW
      sl_formula
   (WS)* R_PAREN

   {
      /* TODO */
      $result = $sl_formula.not();
   }
;

sl_implies_operator
   returns [Formula result]:

   (WS)* IMPLIES_OPERATOR_KW
      a=sl_formula
      b=sl_formula
   (WS)* R_PAREN

   {
      /* TODO */
      $result = $a.implies($b);
   }
;

/** Quantified Expressions ****************************************************/
sl_exists_operator
   returns [Formula result]:

   (WS)* EXISTS_OPERATOR_KW
         var=ID
   (WS)+ type=ID
         f=sl_formula
   (WS*) R_PAREN

   {
      /* TODO */
      $result =
         $f.forSome
         (
            VARIABLE_SET.get_variable
            (
               $var
            ).oneOf
            (
               MODEL.get_type_as_relation($type)
            )
         );
   }
;

sl_forall_operator
   returns [Formula result]:

   (WS)* FORALL_OPERATOR_KW
         var=ID
   (WS)+ type=ID
         f=sl_formula
   (WS*) R_PAREN

   {
      /* TODO */
      $result =
         $f.forAll
         (
            VARIABLE_SET.get_variable
            (
               $var.text
            ).oneOf
            (
               MODEL.get_type_as_relation($type)
            )
         );
   }
;

/** Special Expressions *******************************************************/
sl_ctl_verifies_operator
   returns [Formula result]:

   @init
   {
      final Variable root_node;

      root_node = VARIABLE_SET.generate_new_state_variable();
   }

   (WS)* CTL_VERIFIES_OPERATOR_KW
         ps=ID
         f=bl_formula[root_node]
   (WS)* R_PAREN

   {
      /* TODO */

      $result =
         $f.forSome
         (
            root_node.oneOf
            (
               MODEL.get_type_as_relation("node").in
               (
                  VARIABLE_SET.get_variable($ps).join
                  (
                     MODEL.get_predicate_as_relation("start_node")
                  )
               )
            )
         );
   }
;

/**** Formula Definition ******************************************************/
sl_formula
   returns [Formula result]:

   sl_predicate
   {
      $result = $predicate;
   }

   | sl_and_operator
   {
      $result = $sl_and_operator;
   }

   | sl_or_operator
   {
      $result = $sl_or_operator;
   }

   | sl_not_operator
   {
      $result = $sl_not_operator;
   }

   | sl_implies_operator
   {
      $result = $sl_implies_operator;
   }

   | sl_exists_operator
   {
      $result = $sl_exists_operator;
   }

   | sl_forall_operator
   {
      $result = $sl_forall_operator;
   }

   | sl_ctl_verifies_operator
   {
      $result = $sl_ctl_verifies_operator;
   }
;

/******************************************************************************/
/** Behavioral Level **********************************************************/
/******************************************************************************/
bl_formula_list [Variable current_state]
   returns [List<Formula> list]:

   @init
   {
      final List<Formula> result = new ArrayList<Formula>();
   }

   (
      bl_formula[current_state]
      {
         result.add($bl_formula);
      }
   )+

   {
      $list = result;
   }
;

/**** First Order Expressions *************************************************/
bl_and_operator [Variable current_state]
   returns [Formula result]:

   (WS)* AND_OPERATOR_KW
      bl_formula[current_state]
      bl_formula_list[current_state]
   (WS)* R_PAREN

   {
      /* TODO */
      $result = $bl_formula.and($bl_formula_list);
   }
;

bl_or_operator [Variable current_state]
   returns [Formula result]:

   (WS)* OR_OPERATOR_KW
      bl_formula[current_state]
      bl_formula_list[current_state]
   (WS)* R_PAREN

   {
      /* TODO */
      $result = $bl_formula.or($bl_formula_list);
   }
;

bl_not_operator [Variable current_state]
   returns [Formula result]:

   (WS)* NOT_OPERATOR_KW
      bl_formula[current_state]
   (WS)* R_PAREN

   {
      /* TODO */
      $result = $bl_formula.not();
   }
;

bl_implies_operator [Variable current_state]
   returns [Formula result]:

   (WS)* IMPLIES_OPERATOR_KW
      a=bl_formula[current_state]
      b=bl_formula[current_state]
   (WS)* R_PAREN

   {
      /* TODO */
      $result = $a.implies($b);
   }
;

/**** Computation Tree Logic Expressions **************************************/
bl_ax_operator [Variable current_state]
   returns [Formula result]:

   @init
   {
      final Variable next_state;

      next_state = VARIABLE_SET.generate_new_state_variable();
   }

   (WS)* AX_OPERATOR_KW
      bl_formula[next_state]
   (WS)* R_PAREN

   {
      /* TODO */
      $result =
         $bl_formula.forAll
         (
            next_state.in
            (
               current_state.join
               (
                  MODEL.get_predicate_as_relation("node_connect")
               )
            );
         );
   }
;

bl_ex_operator [Variable current_state]
   returns [Formula result]:

   @init
   {
      final Variable next_state;

      next_state = VARIABLE_SET.generate_new_state_variable();
   }

   (WS)* EX_OPERATOR_KW
      bl_formula[next_state]
   (WS)* R_PAREN

   {
      /* TODO */
      $result =
         $bl_formula.forSome
         (
            next_state.in
            (
               current_state.join
               (
                  MODEL.get_predicate_as_relation("node_connect")
               )
            );
         );
   }
;


bl_ag_operator [Variable current_state]
   returns [Formula result]:

   @init
   {
      final Variable next_state;

      next_state = VARIABLE_SET.generate_new_state_variable();
   }

   (WS)* AG_OPERATOR_KW
      bl_formula[next_state]
   (WS)* R_PAREN

   {
      /* TODO */
      $result =
         Formula.replace /* That does not seem to exist. */
         (
            next_state,
            current_state,
            $bl_formula
         ).and
         (
            $bl_formula.forAll
            (
               {n | <p, n> \in contains_node}
            ).forAll
            (
               {p | <p, current_node> \in is_path_of}
            )
         )
   }
;

bl_eg_operator returns [Formula result]:
   (WS)* EG_OPERATOR_KW bl_formula (WS)* R_PAREN
   {
      /* TODO */
      $result =
         $bl_formula.forSome
         (
            current_node
         ).and
         (
            $bl_formula.forSome
            (
               {n | <p, n> \in contains_node}
            ).forAll
            (
               {p | <p, current_node> \in is_path_of}
            )
         );
   }
;

bl_af_operator returns [Formula result]:
   (WS)* AF_OPERATOR_KW bl_formula (WS)* R_PAREN
   {
      /* TODO */
      $result =
         $bl_formula.forSome
         (
            current_node
         ).or
         (
            $bl_formula.forAll
            (
               {n | <p, n> \in contains_node}
            ).forSome
            (
               {p | <p, current_node> \in is_path_of}
            )
         );
   }
;

bl_ef_operator returns [Formula result]:
   (WS)* EF_OPERATOR_KW bl_formula (WS)* R_PAREN
   {
      /* TODO */
      $result =
         $bl_formula.forSome
         (
            current_node
         ).or
         (
            $bl_formula.forSome
            (
               {n | <p, n> \in contains_node}
            ).forSome
            (
               {p | <p, current_node> \in is_path_of}
            )
         );
   }
;

bl_au_operator returns [Formula result]: (WS)* AU_OPERATOR_KW f1=bl_formula f2=bl_formula (WS)* R_PAREN
   {
      /* TODO */
      $result =
         $f1.forSome
         (
            current_node
         ).and
         (
            $f2.and
            (
               $f1.forAll
               (
                  {t | <p, t, n> \in is_before}
               )
            ).forSome
            (
               {n | <p, n> \in contains_node}
            ).forAll
            (
               {p | <p, current_node> \in is_path_of}
            )
         );
   }
;

bl_eu_operator returns [Formula result]:
   (WS)* EU_OPERATOR_KW f1=bl_formula f2=bl_formula (WS)* R_PAREN
   {
      /* TODO */
      $result =
         $f1.forSome
         (
            current_node
         ).and
         (
            $f2.and
            (
               $f1.forAll
               (
                  {t | <p, t, n> \in is_before}
               )
            ).forSome
            (
               {n | <p, n> \in contains_node}
            ).forSome
            (
               {p | <p, current_node> \in is_path_of}
            )
         );
   }
;

/**** Formula Definition ******************************************************/
bl_formula returns [Formula result]:
   predicate
   {
      $result = $predicate;
   }
   | bl_and_operator
   {
      $result = $bl_and_operator;
   }
   | bl_or_operator
   {
      $result = $bl_or_operator;
   }
   | bl_not_operator
   {
      $result = $bl_not_operator;
   }
   | bl_implies_operator
   {
      $result = $bl_implies_operator;
   }
   | bl_ax_operator
   {
      $result = $bl_ax_operator;
   }
   | bl_ex_operator
   {
      $result = $bl_ex_operator;
   }
   | bl_ag_operator
   {
      $result = $bl_ag_operator;
   }
   | bl_eg_operator
   {
      $result = $bl_eg_operator;
   }
   | bl_af_operator
   {
      $result = $bl_af_operator;
   }
   | bl_ef_operator
   {
      $result = $bl_ef_operator;
   }
   | bl_au_operator
   {
      $result = $bl_au_operator;
   }
   | bl_eu_operator
   {
      $result = $bl_eu_operator;
   }
;
