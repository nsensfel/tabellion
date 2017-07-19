import java.util.List;
import java.util.ArrayList;

public class Parameters
{
   private final List<String> level_files;
   private final List<String> model_files;
   private final String property_file;
   private final String var_prefix;

   private final boolean are_valid;

   public static void print_usage ()
   {
      System.out.println
      (
         "Instr-to-kodkod\n"
         + "USAGE:\n"
         + "\tjava Main <VAR_PREFIX> <FILES>+\n"
         + "PARAMETERS:\n"
         + "\t- <VAR_PREFIX>\tPrefix for anonymous variables (e.g. \"_anon_\").\n"
         + "\t- <FILES>\tList of files to be loaded.\n"
         + "NOTES:\n"
         + "\t- One, single, property file MUST be in <FILES>.\n"
         + "\t- Property files have a \".pro\" extension.\n"
         + "\t- Model files have a \".mod\" extension.\n"
         + "\t- Level files have a \".lvl\" extension.\n"
         + "\t- The files may be given in any order."
      );
   }

   public Parameters (String... args)
   {
      boolean has_pro_file, has_error;
      String prop_file;

      level_files = new ArrayList<String>();
      model_files = new ArrayList<String>();

      if (args.length < 2)
      {
         print_usage();

         property_file = new String();
         var_prefix = new String();

         are_valid = false;

         return;
      }

      has_pro_file = false;
      has_error = false;

      var_prefix = args[1];
      prop_file = new String();

      for (int i = 2; i < args.length; ++i)
      {
         if (args[i].endsWith(".lvl"))
         {
            level_files.add(args[i]);
         }
         else if (args[i].endsWith(".mod"))
         {
            model_files.add(args[i]);
         }
         else if (args[i].endsWith(".lvl"))
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

      are_valid = has_error;
   }

   public List<String> get_level_files ()
   {
      return level_files;
   }

   public List<String> get_model_files ()
   {
      return model_files;
   }

   public String get_property_file ()
   {
      return property_file;
   }

   public String get_variables_prefix ()
   {
      return var_prefix;
   }

   public boolean are_valid ()
   {
      return are_valid;
   }
}
