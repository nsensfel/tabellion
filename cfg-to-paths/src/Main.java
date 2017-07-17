/* FIXME: Finer imports */
import java.util.*;

public class Main
{
   private static Parameters PARAMETERS;

   public static void main (final String... args)
   {
      final Collection<Path> all_paths;
      final Collection<List<Node>> all_subpaths;

      PARAMETERS = new Parameters(args);

      if (!PARAMETERS.are_valid())
      {
         return;
      }

      try
      {
         ControlFlow.load_file(PARAMETERS.get_model_file());
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

      all_paths = Path.get_all_paths_from(PARAMETERS.get_root_node());

      all_subpaths = new ArrayList<List<Node>>();

      for (final Path p: all_paths)
      {
         all_subpaths.addAll(p.get_all_subpaths());
      }
   }
}
