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

tag_existing
   returns [Formula result]:

   (WS)* TAG_EXISTING_KW
      L_PAREN
         tag_list
      R_PAREN
   (WS)* sl_formula
   (WS)* R_PAREN

   {
      final List<String[]> tags;

      $result = ($sl_formula.result);

      tags = ($tag_list.list);

      for (final String[] tag_var: tags)
      {
         $result =
            $result.forSome
            (
               Main.get_variable_manager().get_variable
               (
                  tag_var[0]
               ).oneOf
               (
                  Main.get_model().get_type_as_relation(tag_var[1])
               )
            );

         Main.get_variable_manager().tag_variable(tag_var[0], tag_var[2]);
      }
   }
;

tag_list
   returns [List<String[]> list]

   @init
   {
      final List<String[]> result = new ArrayList<String[]>();
   }

   :
   (
      tag_item
      {
         result.add(($tag_item.result));
      }
   )+

   {
      $list = result;
   }
;

tag_item
   returns [String[] result]:

   (WS)* L_PAREN
   (WS)* var=ID
   (WS)+ type=ID
   (WS)+ tag=ID
   (WS)* R_PAREN
   (WS)*

   {
      $result = new String[3];

      $result[0] = ($var.text);
      $result[1] = ($type.text);
      $result[2] = ($tag.text);
   }
;

id_list
   returns [List<Variable> list]

   @init
   {
      final List<Variable> result = new ArrayList<Variable>();
   }

   :
   (
      (WS)+
      ID
      {
         result.add(Main.get_variable_manager().get_variable(($ID.text)));
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
   returns [Formula result]:

   (WS)* L_PAREN
      ID
      id_list
   (WS)* R_PAREN

   {
      /* TODO */
      $result =
         Expression.product
         (
            ($id_list.list)
         ).in
         (
            Main.get_model().get_predicate_as_relation(($ID.text))
         );
   }
;

sl_non_empty_formula_list
   returns [List<Formula> list]

   @init
   {
      final List<Formula> result = new ArrayList<Formula>();
   }

   :
   (
      sl_formula

      {
         result.add(($sl_formula.result));
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
      $result = ($sl_formula.result).and(($sl_non_empty_formula_list.list));
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
      $result = ($sl_formula.result).or(($sl_non_empty_formula_list.list));
   }
;

sl_not_operator
   returns [Formula result]:

   (WS)* NOT_OPERATOR_KW
      sl_formula
   (WS)* R_PAREN

   {
      /* TODO */
      $result = ($sl_formula.result).not();
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
      $result = ($a.result).implies(($b.result));
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
         ($f.result).forSome
         (
            Main.get_variable_manager().get_variable
            (
               ($var.text)
            ).oneOf
            (
               Main.get_model().get_type_as_relation(($type.text))
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
         ($f.result).forAll
         (
            Main.get_variable_manager().get_variable
            (
               ($var.text)
            ).oneOf
            (
               Main.get_model().get_type_as_relation(($type.text))
            )
         );
   }
;

/** Special Expressions *******************************************************/
sl_ctl_verifies_operator
   returns [Formula result]

   @init
   {
      final Variable root_node;

      root_node = Main.get_variable_manager().generate_new_variable();
   }

   :
   (WS)* CTL_VERIFIES_OPERATOR_KW
         ps=ID
         f=bl_formula[root_node]
   (WS)* R_PAREN

   {
      /* TODO */

      $result =
         ($f.result).forSome
         (
            root_node.oneOf
            (
               Main.get_variable_manager().get_variable(($ps.text)).join
               (
                  Main.get_model().get_predicate_as_relation("is_start_node").transpose()
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
      $result = ($sl_predicate.result);
   }

   | sl_and_operator
   {
      $result = ($sl_and_operator.result);
   }

   | sl_or_operator
   {
      $result = ($sl_or_operator.result);
   }

   | sl_not_operator
   {
      $result = ($sl_not_operator.result);
   }

   | sl_implies_operator
   {
      $result = ($sl_implies_operator.result);
   }

   | sl_exists_operator
   {
      $result = ($sl_exists_operator.result);
   }

   | sl_forall_operator
   {
      $result = ($sl_forall_operator.result);
   }

   | sl_ctl_verifies_operator
   {
      $result = ($sl_ctl_verifies_operator.result);
   }
;

/******************************************************************************/
/** Behavioral Level **********************************************************/
/******************************************************************************/
bl_predicate [Variable current_node]
   returns [Formula result]:

   (WS)* L_PAREN
      ID
      id_list
   (WS)* R_PAREN

   {
      /* TODO */
      $result =
         current_node.product
         (
            Expression.product
            (
               ($id_list.list)
            )
         ).in
         (
            Main.get_model().get_predicate_as_relation(($ID.text))
         );

   }
;

bl_formula_list [Variable current_node]
   returns [List<Formula> list]

   @init
   {
      final List<Formula> result = new ArrayList<Formula>();
   }

   :
   (
      bl_formula[current_node]
      {
         result.add(($bl_formula.result));
      }
   )+

   {
      $list = result;
   }
;

/**** First Order Expressions *************************************************/
bl_and_operator [Variable current_node]
   returns [Formula result]:

   (WS)* AND_OPERATOR_KW
      bl_formula[current_node]
      bl_formula_list[current_node]
   (WS)* R_PAREN

   {
      /* TODO */
      $result = ($bl_formula.result).and(($bl_formula_list.list));
   }
;

bl_or_operator [Variable current_node]
   returns [Formula result]:

   (WS)* OR_OPERATOR_KW
      bl_formula[current_node]
      bl_formula_list[current_node]
   (WS)* R_PAREN

   {
      /* TODO */
      $result = ($bl_formula.result).or(($bl_formula_list.list));
   }
;

bl_not_operator [Variable current_node]
   returns [Formula result]:

   (WS)* NOT_OPERATOR_KW
      bl_formula[current_node]
   (WS)* R_PAREN

   {
      /* TODO */
      $result = ($bl_formula.result).not();
   }
;

bl_implies_operator [Variable current_node]
   returns [Formula result]:

   (WS)* IMPLIES_OPERATOR_KW
      a=bl_formula[current_node]
      b=bl_formula[current_node]
   (WS)* R_PAREN

   {
      /* TODO */
      $result = ($a.result).implies(($b.result));
   }
;

/**** Computation Tree Logic Expressions **************************************/
bl_ax_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable next_node;

      next_node = Main.get_variable_manager().generate_new_variable();
   }

   :
   (WS)* AX_OPERATOR_KW
      bl_formula[next_node]
   (WS)* R_PAREN

   {
      /* TODO */
      $result =
         ($bl_formula.result).forAll
         (
            next_node.oneOf
            (
               current_node.join
               (
                  Main.get_model().get_predicate_as_relation("node_connect")
               )
            )
         );
   }
;

bl_ex_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable next_node;

      next_node = Main.get_variable_manager().generate_new_variable();
   }

   :
   (WS)* EX_OPERATOR_KW
      bl_formula[next_node]
   (WS)* R_PAREN

   {
      /* TODO */
      $result =
         ($bl_formula.result).forSome
         (
            next_node.oneOf
            (
               current_node.join
               (
                  Main.get_model().get_predicate_as_relation("node_connect")
               )
            )
         );
   }
;

bl_ag_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable next_node;

      next_node = Main.get_variable_manager().generate_new_variable();
   }

   :
   (WS)* AG_OPERATOR_KW
      bl_formula[next_node]
   (WS)* R_PAREN

   {
      /* TODO */
      $result =
         ($bl_formula.result).forAll
         (
            next_node.oneOf
            (
               current_node.join
               (
                  Main.get_model().get_predicate_as_relation
                  (
                     "is_path_of"
                  ).transpose() /* (is_path_of path node), we want the path. */
               ).join
               (
                  Main.get_model().get_predicate_as_relation("contains_node")
               )
            )
         );
   }
;

bl_eg_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable next_node, chosen_path;

      next_node = Main.get_variable_manager().generate_new_variable();
      chosen_path = Main.get_variable_manager().generate_new_variable();
   }

   :
   (WS)* EG_OPERATOR_KW
      bl_formula[next_node]
   (WS)* R_PAREN

   {
      /* TODO */
      $result =
         ($bl_formula.result).forAll
         (
            next_node.oneOf
            (
               chosen_path.join
               (
                  Main.get_model().get_predicate_as_relation("contains_node")
               )
            )
         ).forSome
         (
            chosen_path.oneOf
            (
               current_node.join
               (
                  Main.get_model().get_predicate_as_relation
                  (
                     "is_path_of"
                  ).transpose() /* (is_path_of path node), we want the path. */
               )
            )
         );
   }
;

bl_af_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable next_node, chosen_path;

      next_node = Main.get_variable_manager().generate_new_variable();
      chosen_path = Main.get_variable_manager().generate_new_variable();
   }

   :
   (WS)* AF_OPERATOR_KW
      bl_formula[next_node]
   (WS)* R_PAREN

   {
      /* TODO */
      $result =
         ($bl_formula.result).forSome
         (
            next_node.oneOf
            (
               chosen_path.join
               (
                  Main.get_model().get_predicate_as_relation("contains_node")
               )
            )
         ).forAll
         (
            chosen_path.oneOf
            (
               current_node.join
               (
                  Main.get_model().get_predicate_as_relation
                  (
                     "is_path_of"
                  ).transpose() /* (is_path_of path node), we want the path. */
               )
            )
         );
   }
;

bl_ef_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable next_node, chosen_path;

      next_node = Main.get_variable_manager().generate_new_variable();
      chosen_path = Main.get_variable_manager().generate_new_variable();
   }

   :
   (WS)* EF_OPERATOR_KW
      bl_formula[next_node]
   (WS)* R_PAREN

   {
      /* TODO */
      $result =
         ($bl_formula.result).forSome
         (
            next_node.oneOf
            (
               chosen_path.join
               (
                  Main.get_model().get_predicate_as_relation("contains_node")
               )
            )
         ).forSome
         (
            chosen_path.oneOf
            (
               current_node.join
               (
                  Main.get_model().get_predicate_as_relation
                  (
                     "is_path_of"
                  ).transpose() /* (is_path_of path node), we want the path. */
               )
            )
         );
   }
;

bl_au_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable f1_node, f2_node, chosen_path;

      f1_node = Main.get_variable_manager().generate_new_variable();
      f2_node = Main.get_variable_manager().generate_new_variable();
      chosen_path = Main.get_variable_manager().generate_new_variable();
   }

   :
   (WS)* AU_OPERATOR_KW
      f1=bl_formula[f1_node]
      f2=bl_formula[f2_node]
   (WS)* R_PAREN

   {
      /* TODO */
      $result =
         ($f1.result).forAll
         (
            f1_node.oneOf
            (
               f2_node.join
               (
                  chosen_path.join
                  (
                     Main.get_model().get_predicate_as_relation("is_before")
                  ).transpose()
               )
            )
         ).and
         (
            ($f2.result)
         ).forSome
         (
            f2_node.oneOf
            (
               chosen_path.join
               (
                  Main.get_model().get_predicate_as_relation("contains_node")
               )
            )
         ).forAll
         (
            chosen_path.oneOf
            (
               current_node.join
               (
                  Main.get_model().get_predicate_as_relation("is_path_of").transpose()
               )
            )
         );
   }
;

bl_eu_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable f1_node, f2_node, chosen_path;

      f1_node = Main.get_variable_manager().generate_new_variable();
      f2_node = Main.get_variable_manager().generate_new_variable();
      chosen_path = Main.get_variable_manager().generate_new_variable();
   }

   :
   (WS)* EU_OPERATOR_KW
      f1=bl_formula[f1_node]
      f2=bl_formula[f2_node]
   (WS)* R_PAREN

   {
      /* TODO */
      $result =
         ($f1.result).forAll
         (
            f1_node.oneOf
            (
               f2_node.join
               (
                  chosen_path.join
                  (
                     Main.get_model().get_predicate_as_relation("is_before")
                  ).transpose()
               )
            )
         ).and(
            ($f2.result)
         ).forSome
         (
            f2_node.oneOf
            (
               chosen_path.join
               (
                  Main.get_model().get_predicate_as_relation("contains_node")
               )
            )
         ).forSome
         (
            chosen_path.oneOf
            (
               current_node.join
               (
                  Main.get_model().get_predicate_as_relation("is_path_of").transpose()
               )
            )
         );
   }
;

/**** Formula Definition ******************************************************/
bl_formula [Variable current_node]
   returns [Formula result]:

   bl_predicate[current_node]
   {
      $result = ($bl_predicate.result);
   }
   | bl_and_operator[current_node]
   {
      $result = ($bl_and_operator.result);
   }
   | bl_or_operator[current_node]
   {
      $result = ($bl_or_operator.result);
   }
   | bl_not_operator[current_node]
   {
      $result = ($bl_not_operator.result);
   }
   | bl_implies_operator[current_node]
   {
      $result = ($bl_implies_operator.result);
   }
   | bl_ax_operator[current_node]
   {
      $result = ($bl_ax_operator.result);
   }
   | bl_ex_operator[current_node]
   {
      $result = ($bl_ex_operator.result);
   }
   | bl_ag_operator[current_node]
   {
      $result = ($bl_ag_operator.result);
   }
   | bl_eg_operator[current_node]
   {
      $result = ($bl_eg_operator.result);
   }
   | bl_af_operator[current_node]
   {
      $result = ($bl_af_operator.result);
   }
   | bl_ef_operator[current_node]
   {
      $result = ($bl_ef_operator.result);
   }
   | bl_au_operator[current_node]
   {
      $result = ($bl_au_operator.result);
   }
   | bl_eu_operator[current_node]
   {
      $result = ($bl_eu_operator.result);
   }
;
