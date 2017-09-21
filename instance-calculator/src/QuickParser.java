import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuickParser
{
   private static final Pattern instr_pattern;
   private final BufferedReader buffered_reader;

   static
   {
      instr_pattern = Pattern.compile("\\((?<list>[a-z_A-Z0-9 \"]+)\\)");
   }
   public QuickParser (final String filename)
   throws FileNotFoundException
   {
      buffered_reader = new BufferedReader(new FileReader(filename));
   }

   public void finalize ()
   throws IOException
   {
      buffered_reader.close();
   }

   public String[] parse_line ()
   throws IOException
   {
      final List<String> result;
      final Matcher matcher;
      String line;

      do
      {
         line = buffered_reader.readLine();

         if (line == null)
         {
            return new String[0];
         }

         line = line.replaceAll("\\s+"," ");
      }
      while (line.length() < 3 || line.startsWith(";"));

      matcher = instr_pattern.matcher(line);

      if (!matcher.find())
      {
         System.err.println("[E] Invalid instruction \"" + line + "\"");

         return null;
      }

      return matcher.group(1).split(" |\t");
   }
}
