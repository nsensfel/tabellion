import java.util.*;

public class VHDLArchitecture
{
   private static final Map<String, VHDLArchitecture> FROM_ID;

   static
   {
      FROM_ID = new HashMap<String, VHDLArchitecture>();
   }

   public static void resolve_all_waveforms ()
   {
      for (final VHDLArchitecture arch: FROM_ID.values())
      {
         arch.resolve_waveforms();
      }
   }

   public static void add_element (final String id)
   {
      if (!FROM_ID.containsKey(id))
      {
         FROM_ID.put(id, new VHDLArchitecture(id));
      }
   }

   public static VHDLArchitecture get_from_id (final String id)
   {
      final VHDLArchitecture result;

      result = FROM_ID.get(id);

      if (result == null)
      {
         System.err.println
         (
            "[E] Element "
            + id
            + " is used like an architecture, but is not declared as such"
            + " before that use."
         );

         System.exit(-1);
      }

      return result;
   }

   public static VHDLArchitecture find (final String id)
   {
      return FROM_ID.get(id);
   }

/******************************************************************************/
   private final Collection<VHDLProcess> processes;
   private final Collection<VHDLComponent> components;
   private final Collection<VHDLWaveform> waveforms;
   private final Deque<String> futur_waveforms;
   private final String id;

   private VHDLEntity entity;

   private VHDLArchitecture (final String id)
   {
      this.id = id;

      processes = new ArrayList<VHDLProcess>();
      components = new ArrayList<VHDLComponent>();
      waveforms = new ArrayList<VHDLWaveform>();
      futur_waveforms = new ArrayDeque<String>();
   }

   public VHDLEntity get_entity ()
   {
      return entity;
   }

   public void set_entity (final VHDLEntity e)
   {
      entity = e;
   }

   public void add_process (final VHDLProcess ps)
   {
      if (!processes.contains(ps))
      {
         processes.add(ps);
      }
   }

   public void add_component (final VHDLComponent cmp)
   {
      if (!components.contains(cmp))
      {
         components.add(cmp);
      }
   }

   public Collection<VHDLComponent> get_components ()
   {
      return components;
   }

   public void add_waveform (final VHDLWaveform wfm)
   {
      if (!waveforms.contains(wfm))
      {
         waveforms.add(wfm);
      }
   }

   public void add_futur_waveform (final String fwfm)
   {
      if (!futur_waveforms.contains(fwfm))
      {
         futur_waveforms.add(fwfm);
      }
   }

   public void resolve_waveforms ()
   {
      while (!futur_waveforms.isEmpty())
      {
         final String src_id, wfm_id;

         src_id = futur_waveforms.pop();

         wfm_id = Waveforms.get_waveform_id_from_id(src_id);
         add_waveform(VHDLWaveform.get_from_id(wfm_id));
      }
   }

   public void add_instance_to
   (
      final Collection<VHDLProcess.Instance> process_instances,
      final Collection<VHDLWaveform.Instance> waveform_instances,
      final Map<VHDLWaveform, VHDLWaveform.Instance> local_conversion
   )
   {
      for (final VHDLWaveform wfm: waveforms)
      {
         final VHDLWaveform.Instance i_wfm;

         i_wfm = wfm.add_instance(entity);

         waveform_instances.add(i_wfm);

         local_conversion.put(wfm, i_wfm);
      }

      for (final VHDLProcess ps: processes)
      {
         process_instances.add
         (
            ps.generate_base_instance
            (
               entity,
               waveform_instances
            )
         );
      }

      for (final VHDLComponent cmp: components)
      {
         cmp.add_instance_content_to
         (
            process_instances,
            waveform_instances,
            local_conversion
         );
      }
   }
}
