import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.Stack;

/* Sequential Statement Chain Node */
/* Not actually a node in the resulting model, though. */
public class VHDLSSCNode extends VHDLNode
{
   private static final XPathExpression XPE_FIND_SUB_NODES;
   private final IDs prev_node;

   static
   {
      XPE_FIND_SUB_NODES = XMLManager.compile_or_die("./el");
   }

   public VHDLSSCNode
   (
      final OutputFile output,
      final IDs parent_id,
      final Node xml_node,
      final IDs prev_node, /* can't simply forward ref to SSC */
      final IDs next_node,
      final int depth,
      final String[] attributes
   )
   {
      super
      (
         output,
         parent_id,
         xml_node,
         next_node,
         depth,
         attributes
      );

      this.prev_node = prev_node;
   }

   @Override
   public void parse
   (
      final Stack<ParsableXML> waiting_list
   )
   throws XPathExpressionException
   {
      final NodeList sub_nodes;
      final int intermediary_nodes_count;
      int i;

      sub_nodes =
         (NodeList) XPE_FIND_SUB_NODES.evaluate
         (
            xml_node,
            XPathConstants.NODESET
         );

      intermediary_nodes_count = (sub_nodes.getLength() - 1);

      for (i = 0; i < intermediary_nodes_count; ++i)
      {
         final IDs next_node;

         next_node =
            IDs.get_id_from_xml_id
            (
               output,
               XMLManager.get_attribute
               (
                  sub_nodes.item(i + 1),
                  "id"
               ),
               "node"
            );

         waiting_list.push(get_vhdl_node(sub_nodes.item(i), next_node, i));
      }

      waiting_list.push(get_vhdl_node(sub_nodes.item(i), next_node, i));

      handle_backward_connection(sub_nodes.item(0));
   }

   private ParsableXML get_vhdl_node
   (
      final Node node,
      final IDs next_node,
      final int i
   )
   {
      final String node_kind;
      final String[] attributes;

      node_kind = XMLManager.get_attribute(node, "kind");

      if (i == 0)
      {
         /* Attributes are only inherited by the first node */
         attributes = this.attributes;
      }
      else
      {
         attributes = new String[0];
      }

      if (node_kind.equals("if_statement"))
      {
         return
            new VHDLISNode
            (
               output,
               parent_id,
               node,
               next_node,
               depth,
               attributes
            );
      }
      else if (node_kind.equals("simple_signal_assignment_statement"))
      {
         return
            new VHDLSSASNode
            (
               output,
               parent_id,
               node,
               next_node,
               depth,
               attributes
            );
      }
      else if (node_kind.equals("case_statement"))
      {
         return
            new VHDLCSNode
            (
               output,
               parent_id,
               node,
               next_node,
               depth,
               attributes
            );
      }

      System.err.println
      (
         "[E] Unimplemented instruction kind \""
         + node_kind
         + "\" found in Sequential Statement Chain."
      );

      System.exit(-1);

      return null;
   }

   private void handle_backward_connection
   (
      final Node first_node
   )
   {
      final IDs first_node_id;

      first_node_id =
         IDs.get_id_from_xml_id
         (
            output,
            XMLManager.get_attribute
            (
               first_node,
               "id"
            ),
            "node"
         );

      if (prev_node == null)
      {
         /* First node of the process */
         Predicates.add_entry
         (
            output,
            "is_start_node",
            first_node_id,
            parent_id
         );
      }
      else
      {
         /* First node of the process */
         Predicates.add_entry
         (
            output,
            "connect_to",
            prev_node,
            first_node_id
         );
      }
   }
}
