import java.util.*;

public class VHDLWaveform
{
   private static final Map<String, VHDLWaveform> FROM_ID;

   static
   {
      FROM_ID = new HashMap<String, VHDLWaveform>();
   }

   public static void add_element (final String id)
   {
      if (!FROM_ID.containsKey(id))
      {
         FROM_ID.put(id, new VHDLWaveform(id));
      }
   }

   public static VHDLWaveform get_from_id (final String id)
   {
      return FROM_ID.get(id);
   }

   public static boolean handle_is_accessed_by
   (
      final String wfm_id,
      final String ps_id
   )
   {
      /* TODO */
      return false;
   }

/******************************************************************************/
   private final Collection<String> accessed_wfm;
   private final Collection<VHDLWaveform.Instance> instances;
   private final String id;
   private int instances_count;

   private VHDLWaveform (final String id)
   {
      this.id = id;
      accessed_wfm = new ArrayList<String>();
      instances = new ArrayList<VHDLWaveform.Instance>();
      instances_count = 0;
   }

   public VHDLWaveform.Instance add_instance
   (
      final VHDLEntity visibility
   )
   {
      final VHDLWaveform.Instance result;

      result =
         new VHDLWaveform.Instance
         (
            Instances.get_id_for(instances_count),
            this,
            visibility
         );

      instances.add(result);

      instances_count += 1;

      return result;
   }

   public String get_id ()
   {
      return id;
   }

   public static class Instance
   {
      private final String id;
      private final VHDLWaveform parent;
      private final VHDLEntity visibility;

      private Instance
      (
         final String id,
         final VHDLWaveform parent,
         final VHDLEntity visibility
      )
      {
         this.id = id;
         this.parent = parent;
         this.visibility = visibility;
      }

      public VHDLWaveform get_parent ()
      {
         return parent;
      }
   }
}
