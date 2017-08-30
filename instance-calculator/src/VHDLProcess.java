import java.util.*;
import java.io.BufferedWriter;

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

   public static VHDLProcess get_from_id (final String id)
   {
      final VHDLProcess result;

      result = FROM_ID.get(id);

      if (result == null)
      {
         System.err.println
         (
            "[E] Element "
            + id
            + " is used like a process, but is not declared as such before that"
            + " use."
         );

         System.exit(-1);
      }

      return result;
   }

   public static VHDLProcess find (final String id)
   {
      return FROM_ID.get(id);
   }

/******************************************************************************/
   private final Collection<VHDLWaveform> accessed_wfm;
   private final Collection<VHDLProcess.Instance> instances;
   private final String id;
   private int instances_count;

   private VHDLArchitecture architecture;

   private VHDLProcess (final String id)
   {
      this.id = id;
      accessed_wfm = new ArrayList<VHDLWaveform>();
      instances = new ArrayList<VHDLProcess.Instance>();
      instances_count = 0;
      architecture = null;
   }

   public String get_id ()
   {
      return id;
   }

   public void add_accessed_wfm (final VHDLWaveform wfm)
   {
      if (!accessed_wfm.contains(wfm))
      {
         accessed_wfm.add(wfm);
      }
   }

   public void set_architecture (final VHDLArchitecture arch)
   {
      architecture = arch;
   }

   public VHDLProcess.Instance generate_base_instance
   (
      final VHDLEntity visibility,
      final Collection<VHDLWaveform.Instance> waveform_instances
   )
   {
      final VHDLProcess.Instance result;
      final Map<VHDLWaveform.Instance, VHDLWaveform> iwfm_map;

      iwfm_map = new HashMap<VHDLWaveform.Instance, VHDLWaveform>();

      for (final VHDLWaveform.Instance i_wfm: waveform_instances)
      {
         if (accessed_wfm.contains(i_wfm.get_parent()))
         {
            iwfm_map.put(i_wfm, i_wfm.get_parent());
         }
      }

      result =
         new VHDLProcess.Instance
         (
            this,
            visibility,
            iwfm_map
         );

      instances_count += 1;

      return result;
   }

   public static class Instance
   {
      private static int instances_count;

      static
      {
         instances_count = 0;
      }

      private final String id;
      private final VHDLProcess parent;
      private final Map<VHDLWaveform.Instance, VHDLWaveform> iwfm_map;
      private final VHDLEntity visibility;

      private Instance
      (
         final VHDLProcess parent,
         final VHDLEntity visibility,
         final Map<VHDLWaveform.Instance, VHDLWaveform> iwfm_map
      )
      {
         this.id =
            (
               Main.get_parameters().get_id_prefix()
               + "ps_"
               + instances_count
            );

         instances_count += 1;

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
               parent,
               visibility,
               new_iwfm_map
            );

         parent.instances_count += 1;

         return result;
      }

      public void write_predicates_to (final OutputFile of)
      {
         final Set<Map.Entry<VHDLWaveform.Instance, VHDLWaveform>> iwfm_set;

         iwfm_set = iwfm_map.entrySet();

         try
         {
            of.write("(add_element ps_instance ");
            of.write(id);
            of.write(")");
            of.insert_newline();

            of.write("(is_ps_instance_of ");
            of.write(id);
            of.write(" ");
            of.write(parent.get_id());
            of.write(")");
            of.insert_newline();

            of.write("(is_visible_in ");
            of.write(id);
            of.write(" ");
            of.write(visibility.get_id());
            of.write(")");
            of.insert_newline();

            for
            (
               final Map.Entry<VHDLWaveform.Instance, VHDLWaveform> iwfm:
               iwfm_set
            )
            {
               of.write("(process_instance_maps ");
               of.write(id);
               of.write(" ");
               of.write(iwfm.getKey().get_id());
               of.write(" ");
               of.write(iwfm.getValue().get_id());
               of.write(")");
               of.insert_newline();
            }
         }
         catch (final Exception e)
         {
            System.err.println
            (
               "[F] Could not write to output file:"
            );

            e.printStackTrace();

            System.exit(-1);
         }
      }
   }
}
