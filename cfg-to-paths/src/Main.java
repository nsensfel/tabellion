/* FIXME: Finer imports */
import java.util.*;

import java.io.*;

public class Main
{
   private static Parameters PARAMETERS;
   private static int path_counter = 0;

   public static void main (final String... args)
   {
      final FileWriter output;
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

      try
      {
         output = new FileWriter(PARAMETERS.get_output_file());

         for (final List<Node> tuple: all_subpaths)
         {
            node_tuple_to_predicates(tuple, output);
         }

         output.close();
      }
      catch (final Exception e)
      {
         System.err.println
         (
            "[E] Could not write to output file \""
            + PARAMETERS.get_model_file()
            + "\":"
         );

         e.printStackTrace();

         return;
      }
   }

   private static void node_tuple_to_predicates
   (
      final List<Node> tuple,
      final FileWriter out
   )
   throws IOException
   {
      final String id;
      final int tuple_size;

      tuple_size = tuple.size();

      if (tuple_size == 1)
      {
         return;
      }

      id = (PARAMETERS.get_id_prefix() + path_counter);
      path_counter += 1;

      out.write("(add_element path " + id + ")\n");
      out.write("(is_path_of " + id + " " + tuple.get(0) + ")\n");

      for (int i = 1; i < tuple_size; ++i)
      {
         out.write("(contains_node " + id + " " + tuple.get(i) + ")\n");

         for (int j = (i + 1); j < tuple_size; ++j)
         {
            out.write
            (
               "(is_before "
               + id
               + " "
               + tuple.get(i)
               + " "
               + tuple.get(j)
               + ")\n"
            );
         }
      }
   }
}
