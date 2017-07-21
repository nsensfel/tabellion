import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class IDs
{
   /** Static *****************************************************************/
   private static final Map<String, IDs> FROM_XML;
   private static final OutputFile XML_MAP_OUTPUT;
   private static int next_id;

   static
   {
      next_id = 0;

      FROM_XML = new HashMap<String, IDs>();

      /* TODO: filename as a param? */
      XML_MAP_OUTPUT = OutputFile.new_output_file("xml_to_instr.map");
   }

   public static IDs get_id_from_xml_id
   (
      final String xml_id,
      final String type
   )
   {
      return
         get_id_from_xml_id
         (
            Main.get_main_output(),
            xml_id,
            type
         );
   }

   public static IDs get_id_from_xml_id
   (
      final OutputFile output,
      final String xml_id,
      final String type
   )
   {
      IDs result;

      result = FROM_XML.get(xml_id);

      if (result == null)
      {
         result = generate_new_id(output, type);

         FROM_XML.put(xml_id, result);

         XML_MAP_OUTPUT.write("(xml->instr ");
         XML_MAP_OUTPUT.write(xml_id);
         XML_MAP_OUTPUT.write(" ");
         XML_MAP_OUTPUT.write(Integer.toString(result.get_value()));
         XML_MAP_OUTPUT.write(")");
         XML_MAP_OUTPUT.insert_newline();

      }
      else if ((result.type == null) && (type != null))
      {
         /* This allows us to get an ID from a simple reference. */
         result.type = type;

         result.add_to_output(output);
      }

      return result;
   }

   public static IDs generate_new_id
   (
      final String type
   )
   {
      return generate_new_id(Main.get_main_output(), type);
   }

   public static IDs generate_new_id
   (
      final OutputFile output,
      final String type
   )
   {
      final IDs result;

      result = new IDs(type);

      if (type != null)
      {
         result.add_to_output(output);
      }

      return result;
   }

   /** Non-Static *************************************************************/
   private final int value;
   private String type;

   private IDs (final String type)
   {
      this.type = type;

      value = IDs.next_id;

      IDs.next_id += 1;
   }

   public String get_type ()
   {
      return type;
   }

   public int get_value ()
   {
      return value;
   }

   private void add_to_output (final OutputFile output)
   {
      output.write("(add_element ");
      output.write(type);
      output.write(" ");
      output.write(Integer.toString(value));
      output.write(")");
      output.insert_newline();
   }
}
