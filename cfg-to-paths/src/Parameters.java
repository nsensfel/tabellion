public class Parameters
{
   private final String model_file;
   private final String root_node;
   private final String id_prefix;
   private final String output_file;
   private final boolean are_valid;

   public static void print_usage ()
   {
      System.out.println
      (
         "Instr-to-kodkod\n"
         + "USAGE:\n"
         + "\tjava Main <INSTRUCTIONS> <ROOT_NODE> <ID_PREFIX> <OUTPUT_FILE>\n"
         + "PARAMETERS:\n"
         + "\t<INSTRUCTIONS>\tInstruction file describing the model.\n"
         + "\t<ROOT_NODE>\tID of the root node for this DAG.\n"
         + "\t<ID_PREFIX>\tPrefix for the IDs of generated paths.\n"
         + "\t<OUTPUT_FILE>\tFile in which to output the generated"
         + " instructions."
      );
   }

   public Parameters (final String... args)
   {
      if (args.length != 4)
      {
         print_usage();

         model_file = new String();
         root_node = new String();
         id_prefix = new String();
         output_file = new String();

         are_valid = false;
      }
      else
      {
         model_file = args[0];
         root_node = args[1];
         id_prefix = args[2];
         output_file = args[3];
         are_valid = true;
      }
   }

   public String get_model_file ()
   {
      return model_file;
   }

   public String get_root_node ()
   {
      return root_node;
   }

   public String get_id_prefix ()
   {
      return id_prefix;
   }

   public String get_output_file ()
   {
      return output_file;
   }

   public boolean are_valid ()
   {
      return are_valid;
   }
}
