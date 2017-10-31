import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class ParserEntry
{
   private static final List<ParserEntry> ALL_ENTRIES;
   private static String predicate_name;
   private static String output_template_filename;
   private static BufferedWriter output_template_file;
   private static String inferred_level_filename;
   private static BufferedWriter inferred_level_file;

   static
   {
      ALL_ENTRIES = new ArrayList<ParserEntry>();
   }

   private static int parse_arguments (final String args[])
   {
      if (args.length != 3)
      {
         System.err.println
         (
            "usage: java ParserEntry <predicate_name> <output_template_filename>"
            + " inferred_level_filename"
         );

         return -1;
      }

      predicate_name = args[0];
      output_template_filename = args[1];
      inferred_level_filename = args[2];

      return 0;
   }

   private static void parse_file ()
   throws IOException
   {
      final Scanner s;

      s = new Scanner(System.in);

      while (s.hasNextLine())
      {
         final String line;
         final String[] line_data;
         final ParserEntry p;

         line = s.nextLine().trim();

         line_data = line.replaceAll("\\s+", " ").split(" ");

         p = new ParserEntry(line_data[0].trim(), line_data[1].trim());

         ALL_ENTRIES.add(p);

         inferred_level_file.write("(add_type ");
         inferred_level_file.write(p.get_var_type());
         inferred_level_file.write(")\n");
      }
      /*/while */

      inferred_level_file.write("(add_predicate _");
      inferred_level_file.write(predicate_name);

      for (final ParserEntry pe: ALL_ENTRIES)
      {
         inferred_level_file.write(" ");
         inferred_level_file.write(pe.get_var_type());
      }

      inferred_level_file.write(")\n");
   }

   private static void create_template ()
   throws IOException
   {
      final StringBuilder sb;

      sb = new StringBuilder();

      for (final ParserEntry pe: ALL_ENTRIES)
      {
         final String new_id;

         if (pe.get_var_type().equals("waveform"))
         {
            new_id = ("$" + pe.get_var_name() + ".WFM_ID$");
         }
         else
         {
            new_id = ("$" + pe.get_var_name() + ".ID$");
         }

         sb.append(" ");
         sb.append(new_id);

         output_template_file.write("(add_element ");
         output_template_file.write(pe.get_var_type());
         output_template_file.write(" ");
         output_template_file.write(new_id);
         output_template_file.write(")\n");
      }

      output_template_file.write("(_");
      output_template_file.write(predicate_name);
      output_template_file.write(sb.toString());
      output_template_file.write(")\n");
   }

   public static void main (final String args[])
   throws IOException
   {
      File f;

      if (parse_arguments(args) < 0)
      {
         System.exit(-1);
      }

      f = new File(output_template_filename);

      if (!f.exists())
      {
         f.createNewFile();
      }

      output_template_file = new BufferedWriter(new FileWriter(f));

      f = new File(inferred_level_filename);

      if (!f.exists())
      {
         f.createNewFile();
      }

      inferred_level_file = new BufferedWriter(new FileWriter(f, true));

      parse_file();
      create_template();

      output_template_file.close();
      inferred_level_file.close();
   }

   /***************************************************************************/
   private final String var_name;
   private final String var_type;

   private ParserEntry (final String var_name, final String var_type)
   {
      this.var_name = var_name;
      this.var_type = var_type;
   }

   private String get_var_type ()
   {
      return var_type;
   }

   private String get_var_name ()
   {
      return var_name;
   }
}
