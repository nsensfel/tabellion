/* FIXME: finer imports. */
import java.util.*;

import kodkod.ast.*;

public class VariableManager
{
   private final Map<String, Expression> from_string;
   private final Map<String, String> tags;
   private int next_id;

   public VariableManager (final String var_prefix)
   {
      from_string = new HashMap<String, Expression>();
      tags = new HashMap<String, String>();
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

      from_string.put(var_name, Variable.unary(var_name));
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
}
