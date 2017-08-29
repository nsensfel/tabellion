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

   public static VHDLEntity get_from_id (final String id)
   {
      final VHDLEntity result;

      result = FROM_ID.get(id);

      if (result == null)
      {
         System.err.println
         (
            "[E] Element "
            + id
            + " is used like an entity, but is not declared as such before that"
            + " use."
         );

         System.exit(-1);
      }

      return result;
   }

   public static VHDLEntity find (final String id)
   {
      return FROM_ID.get(id);
   }

/******************************************************************************/
   private final Collection<VHDLProcess.Instance> process_instances;
   private final Collection<VHDLWaveform.Instance> waveform_instances;

   private final Collection<String> ports;
   private final String id;

   private VHDLArchitecture architecture;

   private VHDLEntity (final String id)
   {
      this.id = id;
      ports = new ArrayList<String>();

      architecture = null;

      this.process_instances = new ArrayList<VHDLProcess.Instance>();
      this.waveform_instances = new ArrayList<VHDLWaveform.Instance>();
   }

   public void add_port (final String pt)
   {
      if (!ports.contains(pt))
      {
         ports.add(pt);
      }
   }

   public void set_architecture (final VHDLArchitecture arch)
   {
      architecture = arch;
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
