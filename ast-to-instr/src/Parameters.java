import java.util.List;
import java.util.ArrayList;

public class Parameters
{
   private final String xml_file;

   private final boolean are_valid;

   public static void print_usage ()
   {
      System.out.println
      (
         "AST-to-Instr\n"
         + "USAGE:\n"
         + "\tjava Main <XML_FILE>\n"
         + "PARAMETERS:\n"
         + "\t- <XML_FILE>\tThe AST (XML format)."
      );
   }

   public Parameters (String... args)
   {
      if (args.length != 1)
      {
         print_usage();

         xml_file = new String();
         are_valid = false;

         return;
      }

      are_valid = true;

      xml_file = args[0];
   }

   public String get_xml_file ()
   {
      return xml_file;
   }

   public String get_main_output_filename()
   {
      return "structural.mod";
   }

   public boolean are_valid ()
   {
      return are_valid;
   }
}
