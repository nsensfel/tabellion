import java.util.*;

public class VHDLComponent
{
   private static final Map<String, VHDLComponent> FROM_ID;

   static
   {
      FROM_ID = new HashMap<String, VHDLComponent>();
   }

   public static void add_element (final String id)
   {
      if (!FROM_ID.containsKey(id))
      {
         FROM_ID.put(id, new VHDLComponent(id));
      }
   }

   public static boolean handle_is_component_of
   (
      final String cmp_id,
      final String e_id
   )
   {
      return false;
   }

   public static boolean handle_port_maps
   (
      final String cmp_id,
      final String pt_id,
      final String wfm_id
   )
   {
      return false;
   }

/******************************************************************************/
   private final Map<String, String> port_map;
   private final String id;

   private VHDLComponent (final String id)
   {
      this.id = id;

      port_map = new HashMap<String, String>();
   }
}
