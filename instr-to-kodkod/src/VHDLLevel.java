/* FIXME: Finer imports */
import java.util.*;

import java.io.*;

public class VHDLLevel
{
   /* Utility Class */
   private VHDLLevel () {}

   public static boolean add_to_model
   (
      final VHDLModel m,
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

         if (input[0].equals("add_type"))
         {
            success = handle_add_type(input, m);
         }
         else if (input[0].equals("add_predicate"))
         {
            success = handle_add_predicate(input, m, false);
         }
         else if (input[0].equals("add_function"))
         {
            success = handle_add_predicate(input, m, true);
         }
         else
         {
            System.err.println
            (
               "[E] Unknown instruction type \""
               + input[0]
               + "\"  in file \""
               + filename
               + "\"."
            );

            return false;
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

   private static boolean handle_add_type
   (
      final String[] cmd,
      final VHDLModel m
   )
   {
      if (cmd.length != 2)
      {
         System.err.println
         (
            "[E] Badly formed \"add_type\" instruction: \""
            + String.join(" ", cmd)
            + "\"."
         );

         return false;
      }

      m.add_type(cmd[1]);

      return true;
   }

   private static boolean handle_add_predicate
   (
      final String[] cmd,
      final VHDLModel m,
      final boolean is_function
   )
   {
      final String[] signature;

      if (cmd.length < 2)
      {
         System.err.println
         (
            "[E] Badly formed \"add_predicate\" or \"add_function\""
            + " instruction: \""
            + String.join(" ", cmd)
            + "\"."
         );

         return false;
      }

      signature = new String[cmd.length - 2];

      for (int i = 2; i < cmd.length; ++i)
      {
         signature[i - 2] = cmd[i];
      }

      return m.add_predicate(cmd[1], signature, is_function);
   }
}
