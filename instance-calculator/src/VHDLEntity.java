import java.util.*;

public class VHDLEntity
{
   private static final Map<String, VHDLEntity> FROM_ID;

   static
   {
      FROM_ID = new HashMap<String, VHDLEntity>();
   }

   public static void add_element (final String id)
   {
      if (!FROM_ID.containsKey(id))
      {
         FROM_ID.put(id, new VHDLEntity(id));
      }
   }

   public static boolean handle_is_port_of
   (
      final String pt_id,
      final String e_id
   )
   {
      return false;
   }

/******************************************************************************/
   private final List<String> ports;
   private final String id;

   private VHDLEntity (final String id)
   {
      this.id = id;

      ports = new ArrayList<String>();
   }
}
