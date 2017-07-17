import java.util.*;

public class Path
{
   private final ArrayList<Node> nodes;
   private final Node last_node;

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

   public static Collection<Path> get_all_paths_from (final String root)
   {
      final Collection<Path> result;
      final Stack<Path> waiting_list;

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
            for (final Node next: next_nodes)
            {
               waiting_list.push(current_path.add_step(next));
            }
         }
      }

      return result;
   }

   public static Collection<List<Node>> get_subpaths (final Path p)
   {
      final Collection<List<Node>> result;
      final int path_length;

      result = new ArrayList<List<Node>>();
      path_length = p.nodes.size();

      for (int i = 0; i < path_length; ++i)
      {
         result.add(p.nodes.subList(i, path_length));
      }

      return result;
   }
}
