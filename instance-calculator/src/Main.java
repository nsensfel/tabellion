import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;

import java.io.FileWriter;

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

      create_instances();
      OutputFile.close_all();
   }

   private static void create_instances ()
   {
      final Collection<VHDLEntity> futur_candidates;
      final Deque<VHDLEntity> candidates;
      final Collection<VHDLEntity> processed_candidates;
      boolean is_stuck;

      futur_candidates = new ArrayList<VHDLEntity>();
      candidates = new ArrayDeque<VHDLEntity>();
      processed_candidates = new ArrayList<VHDLEntity>();

      futur_candidates.addAll(VHDLEntity.get_all());

      while (!futur_candidates.isEmpty())
      {
         is_stuck = true;

         candidates.addAll(futur_candidates);
         futur_candidates.clear();

         while (!candidates.isEmpty())
         {
            final VHDLEntity e;
            boolean is_ready;

            e = candidates.pop();

            is_ready = true;

            for (final VHDLComponent cmp: e.get_architecture().get_components())
            {
               if (!processed_candidates.contains(cmp.get_destination()))
               {
                  is_ready = false;

                  break;
               }
            }

            if (is_ready)
            {
               is_stuck = false;

               e.generate_instance();

               e.write_predicates();
               processed_candidates.add(e);
            }
            else
            {
               futur_candidates.add(e);
            }
         }

         if (is_stuck)
         {
            System.err.println("[F] Unable to create all the instances...");
            System.err.println
            (
               "[E] The following entites might form a circular dependency:"
            );

            for (final VHDLEntity e: futur_candidates)
            {
               System.err.println("[E] Entity " + e.get_id());
            }

            System.exit(-1);
         }
      }
   }

   public static Parameters get_parameters ()
   {
      return PARAMETERS;
   }
}
