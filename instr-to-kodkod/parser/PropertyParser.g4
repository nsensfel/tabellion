parser grammar PropertyParser;

options
{
   tokenVocab = PropertyLexer;
}

@header
{
   import kodkod.ast.Formula;
   import kodkod.ast.Variable;
   import kodkod.ast.Relation;
   import kodkod.ast.Expression;
   import kodkod.ast.IntExpression;
   import kodkod.ast.IntConstant;
}

@members
{
   /* of the class */
}

tag_existing
   returns [Formula result]:

   (WS)* TAG_EXISTING_KW
      L_PAREN
      (tag_item)+
      R_PAREN
   (WS)* formula[null]
   (WS)* R_PAREN

   {
      $result = ($formula.result);
   }
;

tag_item:

   (WS)* L_PAREN
   (WS)* var=ID
   (WS)+ type=ID
   (WS)+ tag=ID
   (WS)* R_PAREN
   (WS)*

   {
      try
      {
         Main.get_variable_manager().add_tag
         (
            ($var.text),
            ($type.text),
            ($tag.text)
         );
      }
      catch (final Exception e)
      {
         System.err.println
         (
            "[F] The following exception was raised during the parsing of the"
            + " property (l."
            + ($var.getLine())
            + " c."
            + ($var.getCharPositionInLine())
            + "):\n"
            + e.getMessage()
         );

         System.exit(-1);
      }

      if (!Main.get_model().type_exists(($type.text)))
      {
         System.err.println
         (
            "[F] The following exception was raised during the parsing of the"
            + " property (l."
            + ($var.getLine())
            + " c."
            + ($var.getCharPositionInLine())
            + "):\n[F] No such type \""
            + ($type.text)
            + "\"."
         );

         System.exit(-1);
      }
   }
;

id_or_string_or_fun [Variable current_node]
   returns [Expression value]

   :
   ID
   {
      if (($ID.text).equals("_"))
      {
         $value = null;
      }
      else
      {
         try
         {
            $value = Main.get_variable_manager().get_variable(($ID.text));
         }
         catch (final Exception e)
         {
            System.err.println
            (
               "[F] The following exception was raised during the parsing of"
               + " the property (l."
               + ($ID.getLine())
               + " c."
               + ($ID.getCharPositionInLine())
               + "):\n"
               + e.getMessage()
            );

            System.exit(-1);
         }
      }
   }

   |
   STRING
   {
      $value = Main.get_string_manager().get_string_as_relation(($STRING.text));
   }

   |
   function[current_node]
   {
      $value = ($function.result);
   }
;

id_list [Variable current_node]
   returns [List<Expression> list, boolean has_joker]

   @init
   {
      final List<Expression> result = new ArrayList<Expression>();
      boolean used_joker = false;
   }

   :
   (
      (WS)+
      id_or_string_or_fun[current_node]
      {
         if (($id_or_string_or_fun.value) == null)
         {
            used_joker = true;
         }

         result.add(($id_or_string_or_fun.value));
      }
   )*

   {
      $list = result;
      $has_joker = used_joker;
   }
;

predicate [Variable current_node]
   returns [Formula result]:

   (WS)* L_PAREN
      ID
      id_list[current_node]
   (WS)* R_PAREN

   {
      final Expression predicate;
      final List<Expression> ids;
      final Relation predicate_as_relation;

      predicate_as_relation =
         Main.get_model().get_predicate_as_relation
         (
            ($ID.text)
         );

      if (predicate_as_relation == (Relation) null)
      {
         System.err.println
         (
            "[F] The property uses an unknown predicate: \""
            + ($ID.text)
            + "\" (l."
            + ($ID.getLine())
            + " c."
            + ($ID.getCharPositionInLine())
            + ")."
         );

         System.exit(-1);
      }

      if (($id_list.has_joker))
      {
         final List<IntExpression> columns;
         final int params_length;
         final int offset;

         ids = new ArrayList<Expression>();
         columns = new ArrayList<IntExpression>();

         params_length = ($id_list.list).size();

         if (current_node == null)
         {
            offset = 0;
         }
         else
         {
            offset = 1;

            ids.add(current_node);
            columns.add(IntConstant.constant(0));
         }

         for (int i = 0; i < params_length; ++i)
         {
            if (($id_list.list).get(i) != (Expression) null)
            {
               columns.add(IntConstant.constant(i + offset));
               ids.add(($id_list.list).get(i));
            }
         }

         predicate =
            predicate_as_relation.project
            (
               columns.toArray(new IntExpression[columns.size()])
            );
      }
      else
      {
         predicate = predicate_as_relation;
         ids = ($id_list.list);

         if (current_node != null)
         {
            ids.add(0, current_node);
         }
      }

      $result = Expression.product(ids).in(predicate);
   }
;

function [Variable current_node]
   returns [Expression result]:

   (WS)* L_BRAKT
      ID
      id_list[current_node]
   (WS)* R_BRAKT

   {
      final Expression predicate;
      final List<Expression> ids;
      final Relation predicate_as_relation;

      predicate_as_relation =
         Main.get_model().get_predicate_as_relation
         (
            ($ID.text)
         );

      if (predicate_as_relation == (Relation) null)
      {
         System.err.println
         (
            "[F] The property uses an unknown function: \""
            + ($ID.text)
            + "\" (l."
            + ($ID.getLine())
            + " c."
            + ($ID.getCharPositionInLine())
            + ")."
         );

         System.exit(-1);
      }

      if (($id_list.has_joker))
      {
         System.err.println
         (
            "[F] The property uses a joker inside a function call at \""
            + ($ID.text)
            + "\" (l."
            + ($ID.getLine())
            + " c."
            + ($ID.getCharPositionInLine())
            + ")."
         );

         System.exit(-1);
      }
      else
      {
         predicate = predicate_as_relation;
         ids = ($id_list.list);

         if (current_node != null)
         {
            ids.add(0, current_node);
         }

         $result = Expression.product(ids).join(predicate);
      }
   }
;

regex_special_predicate [Variable current_node]
   returns [Formula result]:

   (WS)* REGEX_SPECIAL_PREDICATE_KW
      id_or_string_or_fun[current_node]
      STRING
   (WS)* R_PAREN

   {
      try
      {
         $result =
            ($id_or_string_or_fun.value).product
            (
               Main.get_string_manager().get_regex_as_relation
               (
                  ($STRING.text)
               )
            ).in
            (
               Main.get_model().get_predicate_as_relation
               (
                  "is_start_node"
               ).transpose()
            );
      }
      catch (final Exception e)
      {
         System.err.println
         (
            "[F] A problem occured while handling a regex related predicate in"
            + " the property (l."
            + ($REGEX_SPECIAL_PREDICATE_KW.getLine())
            + " c."
            + ($REGEX_SPECIAL_PREDICATE_KW.getCharPositionInLine())
            + "):"
            + e.getMessage()
         );

         System.exit(-1);
      }
   }
;

non_empty_formula_list [Variable current_node]
   returns [List<Formula> list]

   @init
   {
      final List<Formula> result = new ArrayList<Formula>();
   }

   :
   (
      formula[current_node]

      {
         result.add(($formula.result));
      }
   )+

   {
      $list = result;
   }
;

/**** First Order Expressions *************************************************/
and_operator [Variable current_node]
   returns [Formula result]:

   (WS)* AND_OPERATOR_KW
      formula[current_node]
      non_empty_formula_list[current_node]
   (WS)* R_PAREN

   {
      $result =
         (
            ($formula.result)
         ).and
         (
            Formula.and(($non_empty_formula_list.list))
         );
   }
;

or_operator [Variable current_node]
   returns [Formula result]:

   (WS)* OR_OPERATOR_KW
      formula[current_node]
      non_empty_formula_list[current_node]
   (WS)* R_PAREN

   {
      $result =
         (
            ($formula.result)
         ).or
         (
            Formula.or(($non_empty_formula_list.list))
         );
   }
;

not_operator [Variable current_node]
   returns [Formula result]:

   (WS)* NOT_OPERATOR_KW
      formula[current_node]
   (WS)* R_PAREN

   {
      $result = ($formula.result).not();
   }
;

implies_operator [Variable current_node]
   returns [Formula result]:

   (WS)* IMPLIES_OPERATOR_KW
      a=formula[current_node]
      b=formula[current_node]
   (WS)* R_PAREN

   {
      $result = ($a.result).implies(($b.result));
   }
;

/** Quantified Expressions ****************************************************/
variable_declaration
   returns [Variable var_as_var, Relation type_as_rel]:

   var=ID (WS)+ type=ID

   {
      $type_as_rel =
         Main.get_model().get_type_as_relation
         (
            ($type.text)
         );

      if ($type_as_rel == (Relation) null)
      {
         System.err.println
         (
            "[F] The property uses an unknown type: \""
            + ($type.text)
            + "\" at (l."
            + ($type.getLine())
            + " c."
            + ($type.getCharPositionInLine())
            + ")."
         );

         System.exit(-1);
      }

      try
      {
         $var_as_var = Main.get_variable_manager().add_variable(($var.text));
      }
      catch (final Exception e)
      {
         System.err.println
         (
            "[F] The following exception was raised during the parsing of"
            + " the property (l."
            + ($var.getLine())
            + " c."
            + ($var.getCharPositionInLine())
            + "):\n"
            + e.getMessage()
         );

         System.exit(-1);
      }
   }
;

exists_operator [Variable current_node]
   returns [Formula result]:

   (WS)* EXISTS_OPERATOR_KW
      variable_declaration
      formula[current_node]
   (WS*) R_PAREN

   {
      if (current_node != null)
      {
         System.err.println
         (
            "[W] Use of the existential operator inside a \"CTL_verifies\""
            + " operator is not part of Tabellion's semantics and may not be"
            + " available on other solving platforms. As a result, its use is"
            + " discouraged (from l."
            + ($EXISTS_OPERATOR_KW.getLine())
            + " c."
            + ($EXISTS_OPERATOR_KW.getCharPositionInLine())
            + ")."
         );
      }

      $result =
         ($formula.result).forSome
         (
            ($variable_declaration.var_as_var).oneOf
            (
               ($variable_declaration.type_as_rel)
            )
         );
   }
;

forall_operator [Variable current_node]
   returns [Formula result]:

   (WS)* FORALL_OPERATOR_KW
      variable_declaration
      formula[current_node]
   (WS*) R_PAREN

   {
      if (current_node != null)
      {
         System.err.println
         (
            "[W] Use of the universal operator inside a \"CTL_verifies\""
            + " operator is not part of Tabellion's semantics and may not be"
            + " available on other solving platforms. As a result, its use is"
            + " discouraged (from l."
            + ($FORALL_OPERATOR_KW.getLine())
            + " c."
            + ($FORALL_OPERATOR_KW.getCharPositionInLine())
            + ")."
         );
      }

      $result =
         ($formula.result).forAll
         (
            ($variable_declaration.var_as_var).oneOf
            (
               ($variable_declaration.type_as_rel)
            )
         );
   }
;

/** Special Expressions *******************************************************/
ctl_verifies_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable root_node;

      root_node = Main.get_variable_manager().generate_new_anonymous_variable();
   }

   :
   (WS)* CTL_VERIFIES_OPERATOR_KW
         ps=ID
         f=formula[root_node]
   (WS)* R_PAREN

   {
      if (current_node != null)
      {
         System.err.println
         (
            "[F] The property uses a \"CTL_verifies\" inside a \"CTL_verifies\""
            + " and we have not heard anything about you liking"
            + " \"CTL_verifies\", so you can't CTL_verify while you CTL_verify"
            + " (l."
            + ($CTL_VERIFIES_OPERATOR_KW.getLine())
            + " c."
            + ($CTL_VERIFIES_OPERATOR_KW.getCharPositionInLine())
            + ")."
         );

         System.exit(-1);
      }

      try
      {
         $result =
            ($f.result).forSome
            (
               root_node.oneOf
               (
                  Main.get_variable_manager().get_variable(($ps.text)).join
                  (
                     Main.get_model().get_predicate_as_relation
                     (
                        "is_start_node"
                     ).transpose()
                  )
               )
            );
      }
      catch (final Exception e)
      {
         System.err.println
         (
            "[F] The following exception was raised during the parsing of"
            + " the property (l."
            + ($ps.getLine())
            + " c."
            + ($ps.getCharPositionInLine())
            + "):\n"
            + e.getMessage()
         );

         System.exit(-1);
      }
   }
;


/**** Computation Tree Logic Expressions **************************************/
ax_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable next_node;

      next_node = Main.get_variable_manager().generate_new_anonymous_variable();
   }

   :
   (WS)* AX_OPERATOR_KW
      formula[next_node]
   (WS)* R_PAREN

   {
      if (current_node == null)
      {
         System.err.println
         (
            "[F] The property uses a CTL operator outside of a \"CTL_verifies\""
            + " (l."
            + ($AX_OPERATOR_KW.getLine())
            + " c."
            + ($AX_OPERATOR_KW.getCharPositionInLine())
            + ")."
         );

         System.exit(-1);
      }

      $result =
         ($formula.result).forAll
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

ex_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable next_node;

      next_node = Main.get_variable_manager().generate_new_anonymous_variable();
   }

   :
   (WS)* EX_OPERATOR_KW
      formula[next_node]
   (WS)* R_PAREN

   {
      if (current_node == null)
      {
         System.err.println
         (
            "[F] The property uses a CTL operator outside of a \"CTL_verifies\""
            + " (l."
            + ($EX_OPERATOR_KW.getLine())
            + " c."
            + ($EX_OPERATOR_KW.getCharPositionInLine())
            + ")."
         );

         System.exit(-1);
      }

      $result =
         ($formula.result).forSome
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

ag_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable next_node;

      next_node = Main.get_variable_manager().generate_new_anonymous_variable();
   }

   :
   (WS)* AG_OPERATOR_KW
      formula[next_node]
   (WS)* R_PAREN

   {
      if (current_node == null)
      {
         System.err.println
         (
            "[F] The property uses a CTL operator outside of a \"CTL_verifies\""
            + " (l."
            + ($AG_OPERATOR_KW.getLine())
            + " c."
            + ($AG_OPERATOR_KW.getCharPositionInLine())
            + ")."
         );

         System.exit(-1);
      }

      $result =
         ($formula.result).forAll
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

eg_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable next_node, chosen_path;

      next_node = Main.get_variable_manager().generate_new_anonymous_variable();
      chosen_path = Main.get_variable_manager().generate_new_anonymous_variable();
   }

   :
   (WS)* EG_OPERATOR_KW
      formula[next_node]
   (WS)* R_PAREN

   {
      if (current_node == null)
      {
         System.err.println
         (
            "[F] The property uses a CTL operator outside of a \"CTL_verifies\""
            + " (l."
            + ($EG_OPERATOR_KW.getLine())
            + " c."
            + ($EG_OPERATOR_KW.getCharPositionInLine())
            + ")."
         );

         System.exit(-1);
      }

      $result =
         ($formula.result).forAll
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

af_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable next_node, chosen_path;

      next_node = Main.get_variable_manager().generate_new_anonymous_variable();
      chosen_path = Main.get_variable_manager().generate_new_anonymous_variable();
   }

   :
   (WS)* AF_OPERATOR_KW
      formula[next_node]
   (WS)* R_PAREN

   {
      if (current_node == null)
      {
         System.err.println
         (
            "[F] The property uses a CTL operator outside of a \"CTL_verifies\""
            + " (l."
            + ($AF_OPERATOR_KW.getLine())
            + " c."
            + ($AF_OPERATOR_KW.getCharPositionInLine())
            + ")."
         );

         System.exit(-1);
      }

      $result =
         ($formula.result).forSome
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

ef_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable next_node, chosen_path;

      next_node = Main.get_variable_manager().generate_new_anonymous_variable();
      chosen_path = Main.get_variable_manager().generate_new_anonymous_variable();
   }

   :
   (WS)* EF_OPERATOR_KW
      formula[next_node]
   (WS)* R_PAREN

   {
      if (current_node == null)
      {
         System.err.println
         (
            "[F] The property uses a CTL operator outside of a \"CTL_verifies\""
            + " (l."
            + ($EF_OPERATOR_KW.getLine())
            + " c."
            + ($EF_OPERATOR_KW.getCharPositionInLine())
            + ")."
         );

         System.exit(-1);
      }

      $result =
         ($formula.result).forSome
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

au_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable f1_node, f2_node, chosen_path;

      f1_node = Main.get_variable_manager().generate_new_anonymous_variable();
      f2_node = Main.get_variable_manager().generate_new_anonymous_variable();
      chosen_path = Main.get_variable_manager().generate_new_anonymous_variable();
   }

   :
   (WS)* AU_OPERATOR_KW
      f1=formula[f1_node]
      f2=formula[f2_node]
   (WS)* R_PAREN

   {
      if (current_node == null)
      {
         System.err.println
         (
            "[F] The property uses a CTL operator outside of a \"CTL_verifies\""
            + " (l."
            + ($AU_OPERATOR_KW.getLine())
            + " c."
            + ($AU_OPERATOR_KW.getCharPositionInLine())
            + ")."
         );

         System.exit(-1);
      }

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
                  Main.get_model().get_predicate_as_relation
                  (
                     "is_path_of"
                  ).transpose()
               )
            )
         );
   }
;

eu_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable f1_node, f2_node, chosen_path;

      f1_node = Main.get_variable_manager().generate_new_anonymous_variable();
      f2_node = Main.get_variable_manager().generate_new_anonymous_variable();
      chosen_path = Main.get_variable_manager().generate_new_anonymous_variable();
   }

   :
   (WS)* EU_OPERATOR_KW
      f1=formula[f1_node]
      f2=formula[f2_node]
   (WS)* R_PAREN

   {
      if (current_node == null)
      {
         System.err.println
         (
            "[F] The property uses a CTL operator outside of a \"CTL_verifies\""
            + " (l."
            + ($EU_OPERATOR_KW.getLine())
            + " c."
            + ($EU_OPERATOR_KW.getCharPositionInLine())
            + ")."
         );

         System.exit(-1);
      }

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
                  Main.get_model().get_predicate_as_relation
                  (
                     "is_path_of"
                  ).transpose()
               )
            )
         );
   }
;

/**** Depth Operators *********************************************************/
depth_no_parent_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable node_of_path, node_for_f, chosen_path;

      node_of_path = Main.get_variable_manager().generate_new_anonymous_variable();
      node_for_f = Main.get_variable_manager().generate_new_anonymous_variable();
      chosen_path = Main.get_variable_manager().generate_new_anonymous_variable();
   }

   :
   (WS)* DEPTH_NO_PARENT_OPERATOR_KW
      formula[node_for_f]
   (WS)* R_PAREN

   {
      final Relation depth_relation, lower_than_relation;

      depth_relation = Main.get_model().get_predicate_as_relation("depth");
      lower_than_relation = Main.get_model().get_predicate_as_relation("depth");

      if (current_node == null)
      {
         System.err.println
         (
            "[F] The property uses a CTL operator outside of a \"CTL_verifies\""
            + " (l."
            + ($DEPTH_NO_PARENT_OPERATOR_KW.getLine())
            + " c."
            + ($DEPTH_NO_PARENT_OPERATOR_KW.getCharPositionInLine())
            + ")."
         );

         System.exit(-1);
      }

      $result =
         node_of_path.join
         (
            depth_relation
         ).product
         (
            current_node.join(depth_relation)
         ).in
         (
            lower_than_relation
         ).not
         (
            /* (not (is_lower_than [depth node_of_path] [depth current_node])) */
         ).forAll
         (
            node_of_path.oneOf
            (
               node_for_f.join
               (
                  chosen_path.join
                  (
                     Main.get_model().get_predicate_as_relation("is_before")
                  ).transpose()
               )
            )
         ).and
         (
            ($formula.result).and
            (
               current_node.join
               (
                  depth_relation
               ).product
               (
                  node_for_f
               ).in
               (
                  lower_than_relation
               ).not()
            )
         ).forSome
         (
            node_for_f.oneOf
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
                  ).transpose()
               )
            )
         );
   }
;

depth_no_change_operator [Variable current_node]
   returns [Formula result]

   @init
   {
      final Variable node_of_path, node_for_f, chosen_path;

      node_of_path = Main.get_variable_manager().generate_new_anonymous_variable();
      node_for_f = Main.get_variable_manager().generate_new_anonymous_variable();
      chosen_path = Main.get_variable_manager().generate_new_anonymous_variable();
   }

   :
   (WS)* DEPTH_NO_CHANGE_OPERATOR_KW
      formula[node_for_f]
   (WS)* R_PAREN

   {
      final Relation depth_relation;

      depth_relation = Main.get_model().get_predicate_as_relation("depth");

      if (current_node == null)
      {
         System.err.println
         (
            "[F] The property uses a CTL operator outside of a \"CTL_verifies\""
            + " (l."
            + ($DEPTH_NO_CHANGE_OPERATOR_KW.getLine())
            + " c."
            + ($DEPTH_NO_CHANGE_OPERATOR_KW.getCharPositionInLine())
            + ")."
         );

         System.exit(-1);
      }

      $result =
         node_of_path.join
         (
            depth_relation
         ).eq
         (
            node_for_f.join(depth_relation)
         /* (eq? [depth node_of_path] [depth node_for_f]) */
         ).forAll
         (
            node_of_path.oneOf
            (
               node_for_f.join
               (
                  chosen_path.join
                  (
                     Main.get_model().get_predicate_as_relation("is_before")
                  ).transpose()
               )
            )
         ).and
         (
            ($formula.result)
         ).forSome
         (
            node_for_f.oneOf
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
                  ).transpose()
               )
            )
         );
   }
;

/**** Formula Definition ******************************************************/
formula [Variable current_node]
   returns [Formula result]:

   predicate[current_node]
   {
      $result = ($predicate.result);
   }

   | and_operator[current_node]
   {
      $result = ($and_operator.result);
   }

   | or_operator[current_node]
   {
      $result = ($or_operator.result);
   }

   | not_operator[current_node]
   {
      $result = ($not_operator.result);
   }

   | implies_operator[current_node]
   {
      $result = ($implies_operator.result);
   }

   | exists_operator[current_node]
   {
      $result = ($exists_operator.result);
   }

   | forall_operator[current_node]
   {
      $result = ($forall_operator.result);
   }

   | ctl_verifies_operator[current_node]
   {
      $result = ($ctl_verifies_operator.result);
   }

   | ax_operator[current_node]
   {
      $result = ($ax_operator.result);
   }

   | ex_operator[current_node]
   {
      $result = ($ex_operator.result);
   }

   | ag_operator[current_node]
   {
      $result = ($ag_operator.result);
   }

   | eg_operator[current_node]
   {
      $result = ($eg_operator.result);
   }

   | af_operator[current_node]
   {
      $result = ($af_operator.result);
   }

   | ef_operator[current_node]
   {
      $result = ($ef_operator.result);
   }

   | au_operator[current_node]
   {
      $result = ($au_operator.result);
   }

   | eu_operator[current_node]
   {
      $result = ($eu_operator.result);
   }

   | depth_no_parent_operator[current_node]
   {
      $result = ($depth_no_parent_operator.result);
   }

   | depth_no_change_operator[current_node]
   {
      $result = ($depth_no_change_operator.result);
   }
;
