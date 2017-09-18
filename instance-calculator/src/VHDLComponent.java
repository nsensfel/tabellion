import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

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

   public static VHDLComponent get_from_id (final String id)
   {
      final VHDLComponent result;

      result = FROM_ID.get(id);

      if (result == null)
      {
         System.err.println
         (
            "[E] Element "
            + id
            + " is used like a component instantiation, but is not declared as"
            + " such before that use."
         );

         System.exit(-1);
      }

      return result;
   }

   public static VHDLComponent find (final String id)
   {
      return FROM_ID.get(id);
   }

/******************************************************************************/
   private final Map<String, String> reverse_port_map;
   private final String id;

   private VHDLEntity destination;
   private VHDLArchitecture parent;

   public void set_destination (final VHDLEntity dest)
   {
      destination = dest;
   }

   public VHDLEntity get_destination ()
   {
      return destination;
   }

   public void set_architecture (final VHDLArchitecture arch)
   {
      parent = arch;
   }

   public void add_port_map (final String src, final String dest)
   {
      reverse_port_map.put(dest, src);
   }

   public void add_instance_content_to
   (
      final Collection<VHDLProcess.Instance> process_instances,
      final Collection<VHDLWaveform.Instance> waveform_instances,
      final Map<VHDLWaveform, VHDLWaveform.Instance> local_conversion
   )
   {
      final Collection<VHDLProcess.Instance> dest_process_instances;
      final Collection<VHDLWaveform.Instance> dest_waveform_instances;
      final Map<VHDLWaveform.Instance, VHDLWaveform.Instance> wfm_map;

      dest_process_instances = destination.get_process_instances();
      dest_waveform_instances = destination.get_waveform_instances();

      wfm_map = new HashMap<VHDLWaveform.Instance, VHDLWaveform.Instance>();

      for (final VHDLWaveform.Instance i_wfm: dest_waveform_instances)
      {
         final String src_wfm, dest_port;
         final VHDLWaveform.Instance replacement;

         dest_port =
            Waveforms.get_id_from_waveform_id
            (
               i_wfm.get_parent().get_id()
            );

         src_wfm = reverse_port_map.get(dest_port);

         if (src_wfm == null)
         {
            /* i_wfm is not linked to a port, so it needs a new instance. */
            replacement = i_wfm.get_parent().add_instance(parent.get_entity());
         }
         else
         {
            /*
             * i_wfm is linked to a port. Replace it by what's connected to the
             * port.
             */
            replacement =
               local_conversion.get
               (
                  VHDLWaveform.get_from_id
                  (
                     src_wfm
                  )
               );
         }

         wfm_map.put(i_wfm, replacement);
         waveform_instances.add(replacement);
      }

      for (final VHDLProcess.Instance i_ps: dest_process_instances)
      {
         process_instances.add
         (
            i_ps.add_instance(parent.get_entity(), wfm_map)
         );
      }
   }

   private VHDLComponent (final String id)
   {
      this.id = id;

      reverse_port_map = new HashMap<String, String>();
   }
}
