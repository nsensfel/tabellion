/* FIXME: Finer imports */
import java.util.*;

import java.io.*;

public class Main
{
   private static Parameters PARAMETERS;

   public static void main (final String... args)
   {
      final FileWriter output;

      PARAMETERS = new Parameters(args);

      if (!PARAMETERS.are_valid())
      {
         return;
      }

      try
      {
         ModelFile.load_file(PARAMETERS.get_model_file());
      }
      catch (final Exception e)
      {
         System.err.println
         (
            "[E] Could not load model file \""
            + PARAMETERS.get_model_file()
            + "\":"
         );

         e.printStackTrace();

         return;
      }
   }

   private static void create_instances ()
   {
      /*
       * FuturCandidates <- All Architecture.
       * Candidates <- emptyset
       * Set ProcessedCandidates <- emptyset.
       *
       * while (!isEmpty(FuturCandidates))
       * {
       *    is_stuck = True;
       *    Candidates.addAll(FuturCandidates);
       *    FuturCandidates.setLength(0);
       *
       *    while (!isEmpty(candidates))
       *    {
       *       a = Candidates.pop();
       *
       *       if (a.has_components_not_in(ProcessedCandidates))
       *       {
       *          FuturCandidates.push(a);
       *       }
       *       else
       *       {
       *          is_stuck = False;
       *
       *          a.create_instance();
       *
       *          ProcessedCandidates.add(a);
       *       }
       *    }
       *
       *    if (is_stuck)
       *    {
       *       Error.
       *    }
       * }
       */
   }

   public static Parameters get_parameters ()
   {
      return PARAMETERS;
   }
}
