import java.util.List;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main
{
   private static Parameters PARAMETERS;

   private static boolean load_model_files ()
   {
      for (final String model_file: PARAMETERS.get_model_files())
      {
         try
         {
            if (!Models.load_file(model_file))
            {
               System.err.println
               (
                  "[F] Something went wrong while loading the model file \""
                  + model_file
                  + "\""
               );

               return false;
            }
         }
         catch (final FileNotFoundException fnfe)
         {
            System.err.println
            (
               "[F] Could not find model file \""
               + model_file
               + "\""
            );

            return false;
         }
      }

      return Models.propagate_filenames();
   }

   private static boolean load_map_files ()
   {
      for (final String map_file: PARAMETERS.get_map_files())
      {
         try
         {
            if (!Strings.load_file(map_file))
            {
               System.err.println
               (
                  "[F] Something went wrong while loading the map file \""
                  + map_file
                  + "\""
               );

               return false;
            }
         }
         catch (final FileNotFoundException fnfe)
         {
            System.err.println
            (
               "[F] Could not find map file \""
               + map_file
               + "\""
            );

            return false;
         }
      }

      return true;
   }

   private static void print_solutions ()
   {
      final List<String> sol_files, pp_files;
      final int solutions_count;

      sol_files = PARAMETERS.get_solution_files();
      pp_files =  PARAMETERS.get_pretty_print_files();

      solutions_count = sol_files.size();

      if (solutions_count != pp_files.size())
      {
         System.err.println
         (
            "[F] Not as many solution files as pretty-print files."
         );

         return;
      }

      for (int i = 0; i < solutions_count; ++i)
      {
         try
         {
            Solutions.print(sol_files.get(i), pp_files.get(i));
         }
         catch (final IOException ioe)
         {
            System.err.println
            (
               "[F] Something went wrong while printing the solution linked to"
               + " \""
               + sol_files.get(i)
               + "\" and \""
               + sol_files.get(i)
               + "\":"
            );

            ioe.printStackTrace();

            return;
         }
      }
   }

   public static void main (final String... args)
   {
      PARAMETERS = new Parameters(args);

      if (!PARAMETERS.are_valid())
      {
         return;
      }

      if (!load_map_files())
      {
         return;
      }

      if (!load_model_files())
      {
         return;
      }

      print_solutions();
   }
}
