import java.io.BufferedWriter;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import kodkod.ast.Expression;
import kodkod.ast.Formula;
import kodkod.ast.Relation;
import kodkod.ast.Variable;

import kodkod.instance.Bounds;
import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;

public class VariableManager
{
   private final Map<String, Expression> from_string;
   private final Map<String, TaggedVariable> tagged_variables;
   private int next_id;

   public VariableManager ()
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

   public void print_solution
   (
      final Map<Relation, TupleSet> solution,
      final BufferedWriter output
   )
   throws IOException
   {
      output.write("(solution");

      for (final TaggedVariable tg: tagged_variables.values())
      {
         output.newLine();
         output.write("   (");
         output.write(tg.name);
         output.write(" ");
         output.write
         (
            solution.get(tg.as_relation).iterator().next().atom(0).toString()
         );
         output.write(" ");
         output.write(tg.tag);
         output.write(")");
      }

      output.newLine();
      output.write(")");
      output.newLine();
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
