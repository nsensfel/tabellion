import java.util.*;

public class VHDLArchitecture
{
   private static final Map<String, VHDLArchitecture> FROM_ID;

   static
   {
      FROM_ID = new HashMap<String, VHDLArchitecture>();
   }

   public static void add_element (final String id)
   {
      if (!FROM_ID.containsKey(id))
      {
         FROM_ID.put(id, new VHDLArchitecture(id));
      }
   }

   public static boolean handle_belongs_to_architecture
   (
      final String unknown_id,
      final String arch_id
   )
   {
      /* TODO */
      return false;
   }

   public static boolean handle_is_architecture_of
   (
      final String arch_id,
      final String e_id
   )
   {
      /* TODO */
      return false;
   }

/******************************************************************************/
   private final List<String> processes;
   private final List<String> components;
   private final String id;

   private VHDLEntity entity;

   private VHDLArchitecture (final String id)
   {
      this.id = id;

      processes = new ArrayList<String>();
      components = new ArrayList<String>();
   }

   public VHDLEntity get_entity ()
   {
      return entity;
   }
}
