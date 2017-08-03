import java.util.*;

public class Node
{
   /** Static *****************************************************************/
   private static final Map<String, Node> NODE_FROM_STRING;

   static
   {
      NODE_FROM_STRING = new HashMap<String, Node>();
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

   public static boolean handle_is_terminal (final String a)
   {
      Node n;

      n = NODE_FROM_STRING.get(a);

      if (n == (Node) null)
      {
         n = new Node(a);

         NODE_FROM_STRING.put(a, n);
      }

      n.set_as_terminal();

      return true;
   }

   public static boolean handle_connect_to (final String a, final String b)
   {
      final Node n_a, n_b;

      n_a = NODE_FROM_STRING.get(a);
      n_b = NODE_FROM_STRING.get(b);

      if ((n_a == null) || (n_b == null))
      {
         System.err.println
         (
            "[E] Causality issue: Nodes \""
            + a
            + "\" or \""
            + b
            + "\" used but not defined."
         );

         return false;
      }
      n_a.next_nodes.add(n_b);

      return true;
   }

   /** Non-Static *************************************************************/
   private final Collection<Node> next_nodes;
   private final String name;
   private boolean is_terminal;

   private Node (final String name)
   {
      this.name = name;

      next_nodes = new ArrayList<Node>();
      is_terminal = false;
   }

   private void set_as_terminal ()
   {
      is_terminal = true;
   }

   public Collection<Node> next_nodes ()
   {
      return next_nodes;
   }

   public boolean is_terminal ()
   {
      return is_terminal;
   }

   @Override
   public String toString ()
   {
      return name;
   }
}
