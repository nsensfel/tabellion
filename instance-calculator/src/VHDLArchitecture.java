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
   private final String id;

   private VHDLEntity entity;

   private VHDLArchitecture (final String id)
   {
      this.id = id;

      processes = new ArrayList<VHDLProcess>();
      components = new ArrayList<VHDLComponent>();
      waveforms = new ArrayList<VHDLWaveform>();
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
}
