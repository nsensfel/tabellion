/* FIXME: Finer imports */
import java.io.*;
import java.util.regex.*;
import java.util.*;

public class QuickSolParser
{
   private static final Pattern SOL_ITEM_PATTERN;
   private final BufferedReader buffered_reader;

   static
   {
      SOL_ITEM_PATTERN = Pattern.compile("\\((?<list>[a-zA-Z_0-9 \t]+)\\)");
   }
   public QuickSolParser (final String filename)
   throws FileNotFoundException
   {
      buffered_reader = new BufferedReader(new FileReader(filename));
   }

   public void finalize ()
   throws IOException
   {
      buffered_reader.close();
   }

   public List<String[]> next_solution ()
   throws IOException
   {
      final List<String[]> result;
      final Matcher matcher;
      boolean has_started_sol;
      String line;

      result = new ArrayList<String[]>();
      has_started_sol = false;

      matcher = SOL_ITEM_PATTERN.matcher("");

      for (;;)
      {
         line = buffered_reader.readLine();

         if (line == null)
         {
            return null;
         }

         line = line.replaceAll("\\s+"," ");

         if (line.equals(")"))
         {
            if (!has_started_sol)
            {
               throw
                  new IOException
                  (
                     "[E] Incorrect solution structure. (found a \")\" before a"
                     + " \"(solution\""
                  );
            }

            return result;
         }
         else if (line.equals("(solution"))
         {
            if (has_started_sol)
            {
               throw
                  new IOException
                  (
                     "[E] Incorrect solution structure. (found a second"
                     + "\"(solution\" before the \")\" ending the previous one."
                  );
            }

            has_started_sol = true;
         }
         else if (line.startsWith(";") || line.length() < 3)
         {
            continue;
         }
         else
         {
            final String[] item;

            matcher.reset(line);

            if (!matcher.find())
            {
               throw
                  new IOException
                  (
                     "[E] Incorrect solution structure. \""
                     + line
                     + "\" does not form a correct solution item."
                  );
            }

            item = matcher.group(1).split(" |\t");

            if (item.length != 3)
            {
               throw
                  new IOException
                  (
                     "[E] Incorrect solution item. \""
                     + line
                     + "\" should match the form \"(NAME ID TAG)\"."
                  );
            }

            result.add(item);
         }
      }
   }
}
