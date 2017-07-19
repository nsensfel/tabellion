import java.util.Map;
import java.util.HashMap;

public class Strings
{
   private static final Map<String, IDs> TO_ID;

   static
   {
      TO_ID = new HashMap<String, IDs>();
   }

   private Strings () {} /* Utility class. */

   public static IDs get_id_from_string (final String string)
   {
      IDs result;

      result = TO_ID.get(string);

      if (result == null)
      {
         result = IDs.generate_new_id("string");

         TO_ID.put(string, result);
      }

      return result;
   }
}
