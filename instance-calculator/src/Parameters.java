public class Parameters
{
   private final String model_file;
   private final String id_prefix;
   private final String output_dir;
   private final boolean are_valid;

   public static void print_usage ()
   {
      System.out.println
      (
         "Instance-Calculator\n"
         + "USAGE:\n"
         + "\tjava Main <INSTRUCTIONS> <ID_PREFIX> <OUTPUT_DIR>\n"
         + "PARAMETERS:\n"
         + "\t<INSTRUCTIONS>\tInstruction file describing the model.\n"
         + "\t<ID_PREFIX>\tPrefix for the IDs of generated paths.\n"
         + "\t<OUTPUT_DIR>\tDirectory in which to output the generated"
         + " instruction files."
      );
   }

   public Parameters (final String... args)
   {
      if (args.length != 3)
      {
         print_usage();

         model_file = new String();
         id_prefix = new String();
         output_dir = new String();

         are_valid = false;
      }
      else
      {
         model_file = args[0];
         id_prefix = args[1];
         output_dir = args[2];

         are_valid = true;
      }
   }

   public String get_model_file ()
   {
      return model_file;
   }

   public String get_id_prefix ()
   {
      return id_prefix;
   }

   public String get_output_directory ()
   {
      return output_dir;
   }

   public boolean are_valid ()
   {
      return are_valid;
   }
}
