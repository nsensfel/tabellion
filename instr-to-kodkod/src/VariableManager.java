/* FIXME: finer imports. */
import java.util.*;

import kodkod.ast.*;

public class VariableManager
{
   private final Map<String, Variable> from_string;
   private final Map<String, String> tags;
   private final String var_prefix;
   private int next_id;

   public VariableManager (final String var_prefix)
   {
      from_string = new HashMap<String, Variable>();
      tags = new HashMap<String, String>();

      this.var_prefix = var_prefix;
   }

   private String generate_new_id ()
   {
      final String result;

      result = var_prefix + next_id;

      next_id += 1;

      return result;
   }

   public Variable get_variable (final String name)
   {
      Variable result;

      result = from_string.get(name);

      if (result == null)
      {
         result = Variable.unary(name);

         from_string.put(name, result);
      }

      return result;
   }

   public Variable generate_new_variable ()
   {
      return get_variable(generate_new_id());
   }

   public void tag_variable (final String name, final String tag)
   {
      tags.put(name, tag);
   }
}
