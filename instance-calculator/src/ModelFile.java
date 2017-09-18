import java.io.IOException;
import java.io.FileNotFoundException;

public class ModelFile
{
   public static boolean load_file
   (
      final String filename
   )
   throws FileNotFoundException
   {
      final QuickParser qp;
      String[] input;
      boolean success;

      qp = new QuickParser(filename);

      for (;;)
      {
         try
         {
            input = qp.parse_line();

            if (input == null)
            {
               qp.finalize();

               return false;
            }
            else if (input.length == 0)
            {
               qp.finalize();

               break;
            }
         }
         catch (final IOException e)
         {
            System.err.println
            (
               "[E] IO error while parsing file \""
               + filename
               + "\":"
               /* FIXME: can be null */
               + e.getMessage()
            );

            return false;
         }

         if (input[0].equals("add_element"))
         {
            success = handle_add_element(input);
         }
         else if (input[0].equals("is_accessed_by"))
         {
            success = handle_is_accessed_by(input);
         }
         else if (input[0].equals("is_waveform_of"))
         {
            success = handle_is_waveform_of(input);
         }
         else if (input[0].equals("is_port_of"))
         {
            success = handle_is_port_of(input);
         }
         else if (input[0].equals("is_architecture_of"))
         {
            success = handle_is_architecture_of(input);
         }
         else if (input[0].equals("belongs_to_architecture"))
         {
            success = handle_belongs_to_architecture(input);
         }
         else if (input[0].equals("is_component_of"))
         {
            success = handle_is_component_of(input);
         }
         else if (input[0].equals("port_maps"))
         {
            success = handle_port_maps(input);
         }
         else
         {
            continue;
         }

         if (!success)
         {
            System.err.println
            (
               "[E] An erroneous instruction was found in file \""
               + filename
               + "\"."
            );

            try
            {
               qp.finalize();
            }
            catch (final Exception e)
            {
               System.err.println("[E] Additionally:");
               e.printStackTrace();
            }

            return false;
         }
      }

      VHDLArchitecture.resolve_all_waveforms();

      return true;
   }

   private static boolean handle_add_element
   (
      final String[] input
   )
   {
      if (input.length != 3)
      {
         return false;
      }

      if (input[1].equals("entity"))
      {
         VHDLEntity.add_element(input[2]);
      }
      else if (input[1].equals("architecture"))
      {
         VHDLArchitecture.add_element(input[2]);
      }
      else if (input[1].equals("waveform"))
      {
         VHDLWaveform.add_element(input[2]);
      }
      else if (input[1].equals("process"))
      {
         VHDLProcess.add_element(input[2]);
      }
      else if (input[1].equals("component"))
      {
         VHDLComponent.add_element(input[2]);
      }

      return true;
   }

   private static boolean handle_is_accessed_by
   (
      final String[] input
   )
   {
      final VHDLProcess ps;
      final VHDLWaveform wfm;

      if (input.length != 3)
      {
         return false;
      }

      ps = VHDLProcess.get_from_id(input[2]);
      wfm = VHDLWaveform.find(input[1]);

      if (wfm != null)
      {
         /*
          * Assumes that otherwise it's a string, but that could also be due to
          * an inconsistent model.
          */

         ps.add_accessed_wfm(wfm);
      }

      return true;
   }

   private static boolean handle_is_waveform_of
   (
      final String[] input
   )
   {
      if (input.length != 3)
      {
         return false;
      }

      Waveforms.register_map(input[1], input[2]);

      return true;
   }

   private static boolean handle_is_port_of
   (
      final String[] input
   )
   {
      if (input.length != 3)
      {
         return false;
      }

      VHDLEntity.get_from_id(input[2]).add_port(input[1]);

      return true;
   }

   private static boolean handle_is_architecture_of
   (
      final String[] input
   )
   {
      final VHDLEntity e;
      final VHDLArchitecture arch;

      if (input.length != 3)
      {
         return false;
      }

      e = VHDLEntity.get_from_id(input[2]);
      arch = VHDLArchitecture.get_from_id(input[1]);

      e.set_architecture(arch);
      arch.set_entity(e);

      return true;
   }

   private static boolean handle_belongs_to_architecture
   (
      final String[] input
   )
   {
      final VHDLArchitecture arch;
      final VHDLProcess ps;
      final VHDLComponent cmp;

      if (input.length != 3)
      {
         return false;
      }

      arch = VHDLArchitecture.get_from_id(input[2]);

      ps = VHDLProcess.find(input[1]);

      if (ps != null)
      {
         arch.add_process(ps);
         ps.set_architecture(arch);

         return true;
      }

      cmp = VHDLComponent.find(input[1]);

      if (cmp != null)
      {
         arch.add_component(cmp);
         cmp.set_architecture(arch);

         return true;
      }

      arch.add_futur_waveform(input[1]);

      return true;
   }

   private static boolean handle_is_component_of
   (
      final String[] input
   )
   {
      if (input.length != 3)
      {
         return false;
      }

      VHDLComponent.get_from_id(input[1]).set_destination
      (
         VHDLEntity.get_from_id(input[2])
      );

      return true;
   }

   private static boolean handle_port_maps
   (
      final String[] input
   )
   {
      if (input.length != 4)
      {
         return false;
      }

      VHDLComponent.get_from_id(input[1]).add_port_map
      (
         input[2],
         input[3]
      );

      return true;
   }

   private ModelFile () {} /* Utility Class */
}
