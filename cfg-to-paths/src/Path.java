import java.util.*;

public class Path
{
   private final ArrayList<Node> nodes;
   private final Node last_node;

   public static Collection<Path> get_all_paths_from (final String root)
   {
      final Collection<Path> result;
      final Stack<Path> waiting_list;
      final Node root_node;

      root_node = Node.get_node(root);

      if (root_node == null)
      {
         System.err.println
         (
            "[E] Could not find root node \""
            + root
            + "\"."
         );

         return null;
      }

      result = new ArrayList<Path>();
      waiting_list = new Stack<Path>();

      waiting_list.push((new Path(Node.get_node(root))));

      while (!waiting_list.empty())
      {
         final Path current_path;
         final Node current_node;
         final Collection<Node> next_nodes;

         current_path = waiting_list.pop();
         current_node = current_path.last_node;
         next_nodes = current_node.next_nodes();

         if (next_nodes.isEmpty())
         {
            result.add(current_path);
         }
         else
         {
            if (current_node.is_terminal())
            {
               result.add(current_path);
            }
            for (final Node next: next_nodes)
            {
               waiting_list.push(current_path.add_step(next));
            }
         }
      }

      return result;
   }

   private Path (final Node start)
   {
      nodes = new ArrayList<Node>();

      nodes.add(start);

      last_node = start;
   }

   private Path (final ArrayList<Node> nodes, final Node last_node)
   {
      this.nodes = nodes;
      this.last_node = last_node;

      this.nodes.add(last_node);
   }

   private Path add_step (final Node n)
   {
      return new Path((ArrayList<Node>) nodes.clone(), n);
   }

   public Collection<List<Node>> get_all_subpaths ()
   {
      final Collection<List<Node>> result;
      final int path_length;

      result = new ArrayList<List<Node>>();
      path_length = nodes.size();

      for (int i = 0; i < path_length; ++i)
      {
         result.add(nodes.subList(i, path_length));
      }

      return result;
   }
}
