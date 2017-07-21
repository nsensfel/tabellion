public class Predicates
{
   public static void add_entry
   (
      final String predicate_name,
      final IDs... params
   )
   {
      add_entry(Main.get_main_output(), predicate_name, params);
   }

   public static void add_entry
   (
      final OutputFile output,
      final String predicate_name,
      final IDs... params
   )
   {
      output.write("(");

      output.write(predicate_name);

      for (final IDs param: params)
      {
         output.write(" " + param.get_value());
      }

      output.write(")");
      output.insert_newline();
   }
}
