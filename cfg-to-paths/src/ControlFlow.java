import java.io.*;

public class ControlFlow
{
   public static boolean load_file
   (
      final String filename
   )
   throws FileNotFoundException
   {
      final QuickParser qp;
      String[] input;
      boolean success;

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

         if (input[0].equals("add_element"))
         {
            success = handle_add_element(input);
         }
         else if (input[0].equals("connect_to"))
         {
            success = handle_add_connect_to(input);
         }
         else
         {
            continue;
         }

         if (!success)
         {
            System.err.println
            (
               "[E] An erroneous instruction was found in file \""
               + filename
               + "\"."
            );

            try
            {
               qp.finalize();
            }
            catch (final Exception e)
            {
               System.err.println("[E] Additionally:");
               e.printStackTrace();
            }

            return false;
         }
      }

      return true;
   }

   private static boolean handle_add_element
   (
      final String[] input
   )
   {
      if (input.length != 3)
      {
         return false;
      }

      if (input[1] != "node")
      {
         return true;
      }

      Node.handle_add_node(input[2]);

      return true;
   }

   private static boolean handle_add_connect_to
   (
      final String[] input
   )
   {
      if ((input.length != 3))
      {
         return false;
      }

      return Node.handle_connect_to(input[1], input[2]);
   }
}
