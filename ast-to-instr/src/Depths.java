import java.util.Map;
import java.util.HashMap;

public class Depths
{
   private static final Map<Integer, IDs> TO_ID;
   private static final OutputFile DEPTHS_OUTPUT;
   private static int highest_depth;

   static
   {
      highest_depth = -1;

      TO_ID = new HashMap<Integer, IDs>();

      /* TODO: filename as a param? */
      DEPTHS_OUTPUT = OutputFile.new_output_file("depths.mod");
   }

   private Depths () {} /* Utility class. */

   public static IDs get_id_from_depth
   (
      final String depth
   )
   {
      return get_id_from_depth(Integer.valueOf(depth));
   }

   public static IDs get_id_from_depth
   (
      final Integer depth
   )
   {
      IDs result;

      result = TO_ID.get(depth);

      if (result == null)
      {
         result = IDs.generate_new_id(DEPTHS_OUTPUT, "depth");

         TO_ID.put(depth, result);
      }

      if (depth.intValue() > highest_depth)
      {
         highest_depth = depth.intValue();
      }

      return result;
   }

   public static void generate_predicates ()
   {
      for
      (
         int current_depth = highest_depth;
         current_depth > 0;
         --current_depth
      )
      {
         for (int i = 0; i < current_depth; ++i)
         {
            Predicates.add_entry
            (
               DEPTHS_OUTPUT,
               "is_lower_than",
               get_id_from_depth(new Integer(i)),
               get_id_from_depth(new Integer(current_depth))
            );
         }
      }
   }
}
