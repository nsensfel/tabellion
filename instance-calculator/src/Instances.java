import java.util.*;

public class Instances
{
   private static final Map<Integer, String> instances;

   static
   {
      instances = new HashMap<Integer, String>();
   }

   public static String get_id_for (final int i)
   {
      final Integer j;
      String result;

      j = new Integer(i);

      result = instances.get(j);

      if (result == null)
      {
         result = (Main.get_parameters().get_id_prefix() + i);

         instances.put(j, result);
      }

      return result;
   }
}
