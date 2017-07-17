import java.util.*;

public class Node
{
   private static final Map<String, Node> NODE_FROM_STRING;
   private final Collection<Node> next_nodes;

   private final String name;

   static
   {
      NODE_FROM_STRING = new HashMap<String, Node>();
   }

   private Node (final String name)
   {
      this.name = name;
      next_nodes = new ArrayList<Node>();
   }

   public Collection<Node> next_nodes ()
   {
      return next_nodes;
   }

   @Override
   public String toString ()
   {
      return name;
   }

   public static Node get_node (final String s)
   {
      return NODE_FROM_STRING.get(s);
   }

   public static boolean handle_add_node (final String a)
   {
      if (!NODE_FROM_STRING.containsKey(a))
      {
         NODE_FROM_STRING.put(a, new Node(a));
      }

      return true;
   }

   public static boolean handle_connect_to (final String a, final String b)
   {
      final Node n_a, n_b;

      n_a = NODE_FROM_STRING.get(a);
      n_b = NODE_FROM_STRING.get(b);

      n_a.next_nodes.add(n_b);

      return true;
   }
}
