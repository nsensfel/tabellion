public class Functions
{
   public static void add_entry
   (
      final String function_name,
      final IDs... params
   )
   {
      System.out.print("[FUN] (");
      System.out.print(function_name);

      for (final IDs param: params)
      {
         System.out.print(" " + param.get_value());
      }

      System.out.println(")");
      /* TODO */
   }
}
