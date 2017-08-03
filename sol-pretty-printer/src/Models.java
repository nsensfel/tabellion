import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Models
{
   public static Map<String, String> IS_IN_FILE;
   public static Map<String, String> IS_IN_ARCHITECTURE;
   public static Map<String, String> IS_IN_ENTITY;

   static
   {
      IS_IN_FILE = new HashMap<String, String>();
      IS_IN_ARCHITECTURE = new HashMap<String, String>();
      IS_IN_ENTITY = new HashMap<String, String>();
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

         if (input[0].equals("set_function"))
         {
            handle_set_function(input);
         }
         else if (input[0].equals("is_waveform_of"))
         {
            handle_is_waveform_of(input);
         }
         else
         {
            handle_parent_predicate(input);
         }
      }

      return true;
   }

   private static void handle_is_waveform_of
   (
      final String[] input
   )
   {
      if (input.length != 3)
      {
         return;
      }

      SolutionItem.handle_is_waveform_of
      (
         input[1],
         input[2]
      );
   }

   private static void handle_set_function
   (
      final String[] input
   )
   {
      if (input.length != 4)
      {
         return;
      }

      SolutionItem.handle_unary_set_function
      (
         input[1],
         input[2],
         input[3]
      );
   }

   private static void handle_parent_predicate
   (
      final String[] input
   )
   {
      if (input.length != 3)
      {
         return;
      }

      if (input[0].equals("is_in_file"))
      {
         IS_IN_FILE.put(input[1], input[2]);
      }
      else if (input[0].equals("belongs_to_architecture"))
      {
         IS_IN_ARCHITECTURE.put(input[1], input[2]);
      }
      else if
      (
         input[0].equals("is_port_of")
         || input[0].equals("is_generic_of")
      )
      {
         IS_IN_ENTITY.put(input[1], input[2]);
      }
   }

   public static boolean propagate_filenames ()
   {
      for (final Map.Entry<String, String> file_ref: IS_IN_FILE.entrySet())
      {
         final SolutionItem file_si;

         file_si =
            SolutionItem.get_item_from_id
            (
               file_ref.getValue()
            );

         if (file_si == null)
         {
            System.err.println
            (
               "[E] Can't find any file with id \""
               + file_ref.getValue()
               + "\", yet the item with id \""
               + file_ref.getKey()
               + "\" is supposed to be in it."
            );

            return false;
         }

         SolutionItem.handle_unary_set_function
         (
            "file",
            file_ref.getKey(),
            file_si.get_function_value("filename")
         );
      }

      for
      (
         final Map.Entry<String, String> arch_ref: IS_IN_ARCHITECTURE.entrySet()
      )
      {
         final SolutionItem arch_si;

         arch_si =
            SolutionItem.get_item_from_id
            (
               arch_ref.getValue()
            );

         if (arch_si == null)
         {
            System.err.println
            (
               "[E] Can't find any architecture with id \""
               + arch_ref.getValue()
               + "\", yet the item with id \""
               + arch_ref.getKey()
               + "\" is supposed to be in it."
            );

            return false;
         }

         SolutionItem.handle_unary_set_function
         (
            "file",
            arch_ref.getKey(),
            arch_si.get_function_value("file")
         );
      }

      for
      (
         final Map.Entry<String, String> entity_ref: IS_IN_ENTITY.entrySet()
      )
      {
         final SolutionItem entity_si;

         entity_si =
            SolutionItem.get_item_from_id
            (
               entity_ref.getValue()
            );

         if (entity_si == null)
         {
            System.err.println
            (
               "[E] Can't find any entity with id \""
               + entity_ref.getValue()
               + "\", yet the item with id \""
               + entity_ref.getKey()
               + "\" is supposed to be in it."
            );

            return false;
         }

         SolutionItem.handle_unary_set_function
         (
            "file",
            entity_ref.getKey(),
            entity_si.get_function_value("file")
         );
      }

      return true;
   }
}
