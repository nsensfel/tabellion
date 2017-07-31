/* FIXME: finer imports. */
import java.util.*;

import kodkod.ast.*;

import kodkod.instance.*;

public class VariableManager
{
   private final Map<String, Expression> from_string;
   private final Map<String, TaggedVariable> tagged_variables;
   private int next_id;

   public VariableManager (final String var_prefix)
   {
      from_string = new HashMap<String, Expression>();
      tagged_variables = new HashMap<String, TaggedVariable>();
   }

   private String generate_new_anonymous_variable_name ()
   {
      final String result;

      result = "_var" + next_id;

      next_id += 1;

      return result;
   }

   public void add_tag
   (
      final String var_name,
      final String var_type,
      final String tag_name
   )
   throws Exception
   {
      final TaggedVariable tg;

      System.out.println("[D] Skolemizing: " + var_name);

      if (from_string.containsKey(var_name))
      {
         throw
            new Exception
            (
               "[F] Invalid property: the variable name \""
               + var_name
               + "\" is bound multiple times in the \"tag_existing\""
               + " operator."
            );
      }

      tg = new TaggedVariable(var_name, var_type, tag_name);

      from_string.put(var_name, tg.as_relation);
      tagged_variables.put(var_name, tg);
   }

   public Variable add_variable (final String var_name)
   throws Exception
   {
      final Variable result;

      if (from_string.containsKey(var_name))
      {
         throw
            new Exception
            (
               "[F] Invalid property: the variable name \""
               + var_name
               + "\" is declared multiple times."
            );
      }

      result = Variable.unary(var_name);

      from_string.put(var_name, result);

      return result;
   }

   public Expression get_variable (final String var_name)
   throws Exception
   {
      final Expression result;

      result = from_string.get(var_name);

      if (result == null)
      {
         throw
            new Exception
            (
               "[F] Variable \""
               + var_name
               + "\" is used, but not declared."
            );
      }

      return result;
   }

   public Variable generate_new_anonymous_variable ()
   {
      return Variable.unary(generate_new_anonymous_variable_name());
   }

   public void add_tagged_variables_to_bounds
   (
      final Bounds b,
      final TupleFactory f
   )
   {
      for (final TaggedVariable tg: tagged_variables.values())
      {
         b.bound
         (
            tg.as_relation,
            f.setOf(new Object[0]),
            f.setOf
            (
               Main.get_model().get_type
               (
                  tg.type
               ).get_all_members_as_atoms().toArray()
            )
         );
      }
   }

   public Formula generate_tagged_variable_constraints ()
   {
      Formula result;

      result = Formula.TRUE;

      for (final TaggedVariable tg: tagged_variables.values())
      {
         result = result.and(tg.as_relation.one());
      }

      return result;
   }

   public void print_solution (final Map<Relation, TupleSet> solution)
   {
      System.out.print("(");

      for (final TaggedVariable tg: tagged_variables.values())
      {
         System.out.print("\n   (");
         System.out.print(tg.name);
         System.out.print(" ");
         System.out.print
         (
            solution.get(tg.as_relation).iterator().next().atom(0)
         );
         System.out.print(" ");
         System.out.print(tg.tag);
         System.out.print(")");
      }

      System.out.println("\n)");
   }

   private static class TaggedVariable
   {
      private final String name;
      private final String type;
      private final String tag;
      private final Relation as_relation;

      private TaggedVariable
      (
         final String name,
         final String type,
         final String tag
      )
      {
         this.name = name;
         this.type = type;
         this.tag = tag;

         as_relation = Relation.unary(name);
      }
   }
}
