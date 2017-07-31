import java.util.List;
import java.util.ArrayList;

public class Parameters
{
   private final List<String> level_files;
   private final List<String> model_files;
   private final List<String> map_files;
   private final String property_file;
   private final String output_file;
   private final boolean be_verbose;

   private final boolean are_valid;

   public static void print_usage ()
   {
      System.out.println
      (
         "Instr-to-kodkod\n"
         + "USAGE:\n"
         + "\tjava Main <OUTPUT_FILE> <FILES|OPTIONS>+\n"
         + "PARAMETERS:\n"
         + "\t- <OUTPUT_FILE>\tFile to write the solutions in.\n"
         + "\t- <FILES>\tList of files to be loaded.\n"
         + "OPTIONS:\n"
         + "\t- -v|--verbose\tPrint informative messages to STDOUT.\n"
         + "NOTES:\n"
         + "\t- Exactly one property file must be in <FILES>.\n"
         + "\t- Property files have a \".pro\" extension.\n"
         + "\t- Model files have a \".mod\" extension.\n"
         + "\t- Level files have a \".lvl\" extension.\n"
         + "\t- Map files have a \".map\" extension.\n"
         + "\t- The files may be given in any order."
      );
   }

   public Parameters (final String... args)
   {
      boolean has_pro_file, has_error, should_be_verbose;
      String prop_file;

      level_files = new ArrayList<String>();
      model_files = new ArrayList<String>();
      map_files = new ArrayList<String>();

      should_be_verbose = false;

      if (args.length < 2)
      {
         print_usage();

         property_file = new String();
         output_file = new String();

         are_valid = false;
         be_verbose = false;

         return;
      }

      has_pro_file = false;
      has_error = false;

      output_file = args[0];

      if
      (
         (output_file.equals("-v") || output_file.equals("--verbose"))
         /* || ... */
      )
      {
         print_usage();

         System.err.println
         (
            "[F] An option was found in lieu of the output file."
         );

         System.exit(-1);
      }

      if
      (
         output_file.endsWith(".lvl")
         || output_file.endsWith(".mod")
         || output_file.endsWith(".map")
         || output_file.endsWith(".pro")
      )
      {
         print_usage();

         System.err.println
         (
            "[F] The output file has an extension that could be used in an"
            + " input file. It is most likely that you did not indicate an"
            + " output file, meaning that one of the input files was about to"
            + " be written over. So likely, in fact, that we'll abort here. The"
            + " output file you indicated was \""
            + output_file
            + "\"."
         );

         System.exit(-1);
      }

      prop_file = new String();

      for (int i = 1; i < args.length; ++i)
      {
         if (args[i].endsWith(".lvl"))
         {
            level_files.add(args[i]);
         }
         else if (args[i].endsWith(".mod"))
          {
            model_files.add(args[i]);
         }
         else if (args[i].endsWith(".map"))
         {
            map_files.add(args[i]);
         }
         else if (args[i].endsWith(".pro"))
         {
            if (has_pro_file)
            {
               System.err.println
               (
                  "[E] Both files \""
                  + prop_file
                  + "\" and \"."
                  + args[i]
                  + "\" contain a property. Only one can be used at a time."
               );

               has_error = true;
            }
            else
            {
               has_pro_file = true;
               prop_file = args[i];
            }
         }
         else if (output_file.equals("-v") || output_file.equals("--verbose"))
         {
            should_be_verbose = true;
         }
         else
         {
            System.err.println
            (
               "[E] Unknown file type \""
               + args[i]
               + "\"."
            );

            has_error = true;
         }
      }

      property_file = prop_file;

      if (!has_pro_file)
      {
         System.err.println("[E] There was no property file.");

         has_error = true;
      }

      be_verbose = should_be_verbose;
      are_valid = !has_error;
   }

   public List<String> get_level_files ()
   {
      return level_files;
   }

   public List<String> get_model_files ()
   {
      return model_files;
   }

   public List<String> get_mapping_files ()
   {
      return map_files;
   }

   public String get_property_file ()
   {
      return property_file;
   }

   public String get_output_file ()
   {
      return output_file;
   }

   public boolean be_verbose ()
   {
      return be_verbose;
   }

   public boolean are_valid ()
   {
      return are_valid;
   }
}
