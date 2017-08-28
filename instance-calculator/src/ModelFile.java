import java.io.*;

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

      if (input.equals("entity"))
      {
         VHDLEntity.add_element(input[2]);
      }
      else if (input.equals("architecture"))
      {
         VHDLArchitecture.add_element(input[2]);
      }
      else if (input.equals("process"))
      {
         VHDLProcess.add_element(input[2]);
      }
      else if (input.equals("component"))
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
      if (input.length != 3)
      {
         return false;
      }

      return VHDLProcess.handle_is_accessed_by(input[1], input[2]);
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

      VHDLEntity.handle_is_port_of(input[1], input[2]);

      return true;
   }

   private static boolean handle_is_architecture_of
   (
      final String[] input
   )
   {
      if (input.length != 3)
      {
         return false;
      }

      VHDLArchitecture.handle_is_architecture_of(input[1], input[2]);

      return true;
   }

   private static boolean handle_belongs_to_architecture
   (
      final String[] input
   )
   {
      if (input.length != 3)
      {
         return false;
      }

      VHDLArchitecture.handle_belongs_to_architecture(input[1], input[2]);

      return true;
   }

   private static boolean handle_belongs_to_architecture
   (
      final String[] input
   )
   {
      if (input.length != 3)
      {
         return false;
      }

      VHDLArchitecture.handle_belongs_to_architecture(input[1], input[2]);

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

      VHDLComponent.handle_is_component_of(input[1], input[2]);

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

      VHDLComponent.handle_port_maps(input[1], input[2], input[3]);

      return true;
   }

   private ModelFile () {} /* Utility Class */
}
