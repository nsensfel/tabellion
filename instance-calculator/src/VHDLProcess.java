import java.util.*;

public class VHDLProcess
{
   private static final Map<String, VHDLProcess> FROM_ID;

   static
   {
      FROM_ID = new HashMap<String, VHDLProcess>();
   }

   public static void add_element (final String id)
   {
      if (!FROM_ID.containsKey(id))
      {
         FROM_ID.put(id, new VHDLProcess(id));
      }
   }

   public static boolean handle_is_accessed_by
   (
      final String wfm_id,
      final String ps_id
   )
   {
      return false;
   }

/******************************************************************************/

   private final List<String> accessed_wfm;
   private final String id;

   private VHDLProcess (final String id)
   {
      this.id = id;

      ports = new ArrayList<String>();
   }
}
