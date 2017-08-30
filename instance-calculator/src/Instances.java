import java.util.*;

public class Instances
{
   private static final Map<Integer, String> INSTANCES;
   private static final OutputFile OUTPUT_FILE;

   static
   {
      INSTANCES = new HashMap<Integer, String>();
      OUTPUT_FILE = OutputFile.new_output_file("instances.mod");
   }

   public static String get_id_for (final int i)
   {
      final Integer j;
      String result;

      j = new Integer(i);

      result = INSTANCES.get(j);

      if (result == null)
      {
         result = (Main.get_parameters().get_id_prefix() + i);

         INSTANCES.put(j, result);
      }

      return result;
   }

   public static void write_predicates ()
   {
      for (final String id: INSTANCES.values())
      {
         OUTPUT_FILE.write("(add_element instance ");
         OUTPUT_FILE.write(id);
         OUTPUT_FILE.write(")");

         OUTPUT_FILE.insert_newline();
      }
   }
}
