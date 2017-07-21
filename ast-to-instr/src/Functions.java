public class Functions
{
   public static void add_entry
   (
      final String function_name,
      final IDs... params
   )
   {
      add_entry(Main.get_main_output(), function_name, params);
   }

   public static void add_entry
   (
      final OutputFile output,
      final String function_name,
      final IDs... params
   )
   {
      output.write("(set_function ");

      output.write(function_name);

      for (final IDs param: params)
      {
         output.write(" " + param.get_value());
      }

      output.write(")");
      output.insert_newline();
   }
}
