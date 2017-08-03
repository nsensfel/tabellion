import java.util.List;
import java.util.ArrayList;

public class Parameters
{
   private final String xml_file;
   private final String output_dir;

   private final boolean are_valid;

   public static void print_usage ()
   {
      System.out.println
      (
         "AST-to-Instr\n"
         + "USAGE:\n"
         + "\tjava Main <XML_FILE> <OUTPUT_DIR>\n"
         + "PARAMETERS:\n"
         + "\t- <XML_FILE>\tThe AST (XML format).\n"
         + "\t- <OUTPUT_DIR>\tThe output directory (must already exist)."
      );
   }

   public Parameters (String... args)
   {
      if (args.length != 2)
      {
         print_usage();

         xml_file = new String();
         output_dir = new String();
         are_valid = false;

         return;
      }

      xml_file = args[0];
      output_dir = args[1];

      are_valid = true;
   }

   public String get_xml_file ()
   {
      return xml_file;
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
