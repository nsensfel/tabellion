public class Parameters
{
   private final String levels_dir;
   private final String model_file;
   private final String var_prefix;
   private final boolean are_valid;

   public static void print_usage ()
   {
      System.out.println
      (
         "Instr-to-kodkod\n"
         + "USAGE:\n"
         + "\tjava Main <LEVELS_DIR> <INSTRUCTIONS> <VAR_PREFIX>\n"
         + "PARAMETERS:\n"
         + "\t<LEVELS_DIR>\tDirectory containing the level definitions.\n"
         + "\t<INSTRUCTIONS>\tInstruction file describing the model.\n"
         + "\t<VAR_PREFIX>\tPrefix for anonymous variables (e.g. \"_anon_\").\n"
         + "NOTES:\n"
         + "\tThe properties to be verified still have to be hand coded in the"
         + " source files (in Main.java)."
      );
   }

   public Parameters (String... args)
   {
      if (args.length != 3)
      {
         print_usage();

         levels_dir = new String();
         model_file = new String();
         var_prefix = new String();
         are_valid = false;
      }
      else
      {
         levels_dir = args[0];
         model_file = args[1];
         var_prefix = args[2];
         are_valid = true;
      }
   }

   public String get_levels_directory ()
   {
      return levels_dir;
   }

   public String get_model_file ()
   {
      return model_file;
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
