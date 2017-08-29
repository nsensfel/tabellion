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
      /* TODO */
      return false;
   }

   public static boolean handle_is_architecture_of
   (
      final String pt_id,
      final String e_id
   )
   {
      /* TODO */
      return false;
   }

/******************************************************************************/
   private final Collection<VHDLProcess.Instance> process_instances;
   private final Collection<VHDLWaveform.Instance> waveform_instances;

   private final List<String> ports;
   private final String id;

   private String architecture;

   private VHDLEntity (final String id)
   {
      this.id = id;
      ports = new ArrayList<String>();

      this.process_instances = new ArrayList<VHDLProcess.Instance>();
      this.waveform_instances = new ArrayList<VHDLWaveform.Instance>();
   }

   public Collection<VHDLProcess.Instance> get_process_instances ()
   {
      return process_instances;
   }

   public Collection<VHDLWaveform.Instance> get_waveform_instances ()
   {
      return waveform_instances;
   }
}
