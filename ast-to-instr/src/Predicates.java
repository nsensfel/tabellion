public class Predicates
{
   public static void add_entry
   (
      final String predicate_name,
      final IDs... params
   )
   {
      System.out.print("[PRE] (");
      System.out.print(predicate_name);

      for (final IDs param: params)
      {
         System.out.print(" " + param.get_value());
      }

      System.out.println(")");
      /* TODO */
   }
}
