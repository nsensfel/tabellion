import java.util.Map;
import java.util.List;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;


public class Solutions
{
   private static String load_file (final String filename)
   throws IOException
   {
      return
         new String
         (
            Files.readAllBytes(Paths.get(filename)),
            StandardCharsets.UTF_8
         );
   }

   public static boolean print (final String sol_file, final String pp_file)
   throws IOException
   {
      final String pp_content;
      final QuickSolParser qsp;
      List<String[]> solution;

      pp_content = load_file(pp_file);

      qsp = new QuickSolParser(sol_file);

      for (;;)
      {
         solution = qsp.next_solution();

         if (solution == null)
         {
            return true;
         }

         if (!handle_solution(solution, pp_content))
         {
            return false;
         }
      }
   }

   private static boolean handle_solution
   (
      final List<String[]> solution,
      String pp_content
   )
   {
      for (final String[] sol_data: solution)
      {
         final SolutionItem si;

         si = SolutionItem.get_item_from_id(sol_data[1]);

         if (si == null)
         {
            System.err.println
            (
               "[E] There is no element in the model with an ID of \""
               + sol_data[1]
               + "\", yet the solution file refers to it."
            );

            return false;
         }

         for (final Map.Entry<String, String> me: si.get_functions_data())
         {
            pp_content =
               pp_content.replace
               (
                  (
                     "$"
                     + sol_data[0]
                     + "."
                     + me.getKey().toUpperCase()
                     + "$"
                  ),
                  /* FIXME */
                  (Strings.get_string_from_id(me.getValue()) == null) ? "null" : Strings.get_string_from_id(me.getValue())
               );
         }
      }

      System.out.println(pp_content);

      return true;
   }
}
