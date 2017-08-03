import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;

public class Strings
{
   private static final Map<String, String> FROM_ID;

   static
   {
      FROM_ID = new HashMap<String, String>();
   }

   private static void add_mapping (final String id, final String str)
   {
      FROM_ID.put(id, str);
   }

   public static String get_string_from_id (final String id)
   {
      return FROM_ID.get(id);
   }

   private static boolean handle_mapping_instruction (final String... instr)
   {
      if (instr.length < 3)
      {
         return false;
      }

      if (!instr[0].equals("string->instr"))
      {
         return false;
      }

      add_mapping(instr[2], instr[1]);

      return true;
   }

   public static boolean load_file (final String filename)
   throws FileNotFoundException
   {
      final QuickParser qp;
      String[] input;

      qp = new QuickParser(filename);

      for (;;)
      {
         try
         {
            input = qp.parse_line();

            if (input == null)
            {
               qp.finalize();

               return false;
            }
            else if (input.length == 0)
            {
               qp.finalize();

               break;
            }
         }
         catch (final IOException e)
         {
            System.err.println
            (
               "[E] IO error while parsing file \""
               + filename
               + "\":"
               /* FIXME: can be null */
               + e.getMessage()
            );

            return false;
         }

         handle_mapping_instruction(input);
      }

      return true;
   }
}
