import java.util.Map;
import java.util.HashMap;

public class SolutionItem
{
   private static final Map<String, SolutionItem> FROM_ID;

   static
   {
      FROM_ID = new HashMap<String, SolutionItem>();
   }


   public static void handle_unary_set_function
   (
      final String function,
      final String id,
      final String value
   )
   {
      SolutionItem si;

      si = FROM_ID.get(id);

      if (id == null)
      {
         si = new SolutionItem(id);

         FROM_ID.put(id, si);
      }

      si.function_values.put(function.toLowerCase(), value);
   }

   public static SolutionItem get_item_from_id (final String id)
   {
      return FROM_ID.get(id);
   }

   /** Non-Static *************************************************************/
   private final Map<String, String> function_values;

   private SolutionItem
   (
      final String id
   )
   {
      function_values = new HashMap<String, String>();
      function_values.put("id", id);
   }

   public String get_function_value (final String fun)
   {
      return function_values.get(fun);
   }
}
