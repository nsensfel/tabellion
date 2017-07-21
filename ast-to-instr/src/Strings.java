import java.util.Map;
import java.util.HashMap;

public class Strings
{
   private static final Map<String, IDs> TO_ID;
   private static final OutputFile STRING_MAP_OUTPUT;

   static
   {
      TO_ID = new HashMap<String, IDs>();

      /* TODO: filename as a param? */
      STRING_MAP_OUTPUT = OutputFile.new_output_file("string_to_instr.map");
   }

   private Strings () {} /* Utility class. */

   public static IDs get_id_from_string
   (
      String string
   )
   {
      return get_id_from_string(Main.get_main_output(), string);
   }

   public static IDs get_id_from_string
   (
      final OutputFile output,
      String string
   )
   {
      IDs result;

      string = string.toLowerCase();
      result = TO_ID.get(string);

      if (result == null)
      {
         result = IDs.generate_new_id(output, "string");

         TO_ID.put(string, result);

         STRING_MAP_OUTPUT.write("(string->instr \"");
         STRING_MAP_OUTPUT.write(string);
         STRING_MAP_OUTPUT.write("\" ");
         STRING_MAP_OUTPUT.write(Integer.toString(result.get_value()));
         STRING_MAP_OUTPUT.write(")");
         STRING_MAP_OUTPUT.insert_newline();
      }

      return result;
   }
}
