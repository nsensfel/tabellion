import java.util.*;

public class VHDLEntity
{
   private static final Map<String, VHDLEntity> FROM_ID;

   static
   {
      FROM_ID = new HashMap<String, VHDLEntity>();
   }

   public static Collection<VHDLEntity> get_all ()
   {
      return FROM_ID.values();
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

   public String get_id ()
   {
      return id;
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

   public VHDLArchitecture get_architecture ()
   {
      return architecture;
   }

   public Collection<VHDLProcess.Instance> get_process_instances ()
   {
      return process_instances;
   }

   public Collection<VHDLWaveform.Instance> get_waveform_instances ()
   {
      return waveform_instances;
   }

   public void generate_instance ()
   {
      final Map<VHDLWaveform, VHDLWaveform.Instance> local_conversion;

      local_conversion = new HashMap<VHDLWaveform, VHDLWaveform.Instance>();

      for (final String pt: ports)
      {
         final VHDLWaveform wfm;
         final VHDLWaveform.Instance i_wfm;

         wfm = VHDLWaveform.get_from_id(Waveforms.get_waveform_id_from_id(pt));
         i_wfm = wfm.add_instance(this);

         waveform_instances.add(i_wfm);

         local_conversion.put(wfm, i_wfm);
      }

      architecture.add_instance_to
      (
         process_instances,
         waveform_instances,
         local_conversion
      );
   }
}
