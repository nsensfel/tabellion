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
      /* TODO */
      return false;
   }

/******************************************************************************/
   private final Collection<VHDLWaveform> accessed_wfm;
   private final Collection<VHDLProcess.Instance> instances;
   private final String id;
   private int instances_count;

   private VHDLProcess (final String id)
   {
      this.id = id;
      accessed_wfm = new ArrayList<VHDLWaveform>();
      instances = new ArrayList<VHDLProcess.Instance>();
      instances_count = 0;
   }

   public static class Instance
   {
      private final String id;
      private final VHDLProcess parent;
      private final Map<VHDLWaveform.Instance, VHDLWaveform> iwfm_map;
      private final VHDLEntity visibility;

      private Instance
      (
         final String id,
         final VHDLProcess parent,
         final VHDLEntity visibility,
         final Map<VHDLWaveform.Instance, VHDLWaveform> iwfm_map
      )
      {
         this.id = id;
         this.parent = parent;
         this.visibility = visibility;
         this.iwfm_map = iwfm_map;
      }

      public VHDLProcess get_parent ()
      {
         return parent;
      }

      public VHDLProcess.Instance add_instance
      (
         final VHDLEntity visibility,
         final Map<VHDLWaveform.Instance, VHDLWaveform.Instance> convertion
      )
      {
         final VHDLProcess.Instance result;
         final Set<Map.Entry<VHDLWaveform.Instance, VHDLWaveform>> iwfm_set;
         final Map<VHDLWaveform.Instance, VHDLWaveform> new_iwfm_map;

         iwfm_set = iwfm_map.entrySet();

         new_iwfm_map = new HashMap<VHDLWaveform.Instance, VHDLWaveform>();

         for
         (
            final Map.Entry<VHDLWaveform.Instance, VHDLWaveform> me: iwfm_set
         )
         {
            new_iwfm_map.put
            (
               convertion.get(me.getKey()),
               me.getValue()
            );
         }

         result =
            new VHDLProcess.Instance
            (
               Instances.get_id_for(parent.instances_count),
               parent,
               visibility,
               new_iwfm_map
            );

         parent.instances_count += 1;

         return result;
      }
   }
}
