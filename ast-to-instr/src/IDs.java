import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class IDs
{
   /** Static *****************************************************************/
   private static final Map<String, IDs> FROM_XML;
   private static final Collection<IDs> ALL_IDS;
   private static int next_id;

   static
   {
      next_id = 0;

      FROM_XML = new HashMap<String, IDs>();
      ALL_IDS = new ArrayList<IDs>();
   }


   public static IDs get_id_from_xml_id
   (
      final String xml_id,
      final String type
   )
   {
      IDs result;

      result = FROM_XML.get(xml_id);

      if (result == null)
      {
         result = new IDs(type);

         FROM_XML.put(xml_id, result);
      }

      return result;
   }

   public static IDs generate_new_id
   (
      final String type
   )
   {
      final IDs result;

      result = new IDs(type);

      ALL_IDS.add(result);

      return result;
   }

   /** Non-Static *************************************************************/
   private final String type;
   private final int value;

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
}
