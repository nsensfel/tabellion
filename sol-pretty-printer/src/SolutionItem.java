import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class SolutionItem
{
   private static final Map<String, SolutionItem> FROM_ID;

   static
   {
      FROM_ID = new HashMap<String, SolutionItem>();
   }

   public static void handle_is_waveform_of
   (
      final String wfm_id,
      final String origin_id
   )
   {
      SolutionItem si;

      si = FROM_ID.get(origin_id);

      if (si == null)
      {
         si = new SolutionItem(origin_id);

         FROM_ID.put(origin_id, si);
      }

      FROM_ID.put(wfm_id, si);
      si.function_values.put("wfm_id", wfm_id);
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

      if (si == null)
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

      function_values.put("ID", id);
   }

   public Set<Map.Entry<String, String>> get_functions_data ()
   {
      return function_values.entrySet();
   }

   public String get_function_value (final String fun)
   {
      return function_values.get(fun);
   }
}
