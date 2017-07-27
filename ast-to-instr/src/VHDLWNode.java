import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.Stack;

/* When Node */
public class VHDLWNode extends VHDLNode
{
   private static final XPathExpression XPE_FIND_SOURCES;
   private static final XPathExpression XPE_FIND_BODY;

   static
   {
      XPE_FIND_SOURCES =
         XMLManager.compile_or_die
         (
            "./choice_expression" /* //named_entity" */
         );

      XPE_FIND_BODY =
         XMLManager.compile_or_die
         (
            "./associated_chain"
         );
   }

   public VHDLWNode
   (
      final OutputFile output,
      final IDs parent_id,
      final Node xml_node,
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
   }

   @Override
   public void parse
   (
      final Stack<ParsableXML> waiting_list
   )
   throws XPathExpressionException
   {
      final String xml_id;
      final IDs local_id;

      xml_id = XMLManager.get_attribute(xml_node, "id");

      local_id = IDs.get_id_from_xml_id(output, xml_id, "node");

      /** Functions ***********************************************************/
      handle_function_label(local_id);
      handle_function_kind(local_id);
      handle_function_depth(local_id);

      /** Predicates **********************************************************/
      handle_predicate_has_option(local_id);
      handle_predicate_expr_reads(local_id);

      /** Children ************************************************************/
      handle_body(local_id, waiting_list);
   }

   /***************************************************************************/
   /** Functions **************************************************************/
   /***************************************************************************/
   private void handle_function_label
   (
      final IDs local_id
   )
   {
      Functions.add_entry
      (
         output,
         "label",
         local_id,
         Strings.get_id_from_string("")
      );
   }

   private void handle_function_kind
   (
      final IDs local_id
   )
   {
      Functions.add_entry
      (
         output,
         "kind",
         local_id,
         Strings.get_id_from_string("when")
      );
   }

   private void handle_function_depth
   (
      final IDs local_id
   )
   {
      Functions.add_entry
      (
         output,
         "depth",
         local_id,
         Depths.get_id_from_depth(new Integer(depth))
      );
   }

   /***************************************************************************/
   /** Predicates *************************************************************/
   /***************************************************************************/
   private void handle_predicate_has_option
   (
      final IDs local_id
   )
   {
      for (final String s: attributes)
      {
         Predicates.add_entry
         (
            output,
            "has_option",
            local_id,
            Strings.get_id_from_string(s)
         );
      }
   }

   private void handle_predicate_expr_reads
   (
      final IDs local_id
   )
   throws XPathExpressionException
   {
      final Node sources;

      sources =
         (Node) XPE_FIND_SOURCES.evaluate
         (
            xml_node,
            XPathConstants.NODE
         );

      if (sources != (Node) null)
      {
         /* Not "others" */
         handle_read_expr_predicates(local_id, sources);
      }
   }

   /***************************************************************************/
   /** Children ***************************************************************/
   /***************************************************************************/

   private void handle_body
   (
      final IDs local_id,
      final Stack<ParsableXML> waiting_list
   )
   throws XPathExpressionException
   {
      final Node body;

      body =
         (Node) XPE_FIND_BODY.evaluate
         (
            xml_node,
            XPathConstants.NODE
         );

      waiting_list.push
      (
         new VHDLSSCNode
         (
            output,
            parent_id,
            body,
            local_id,
            next_node,
            (depth + 1),
            new String[] {"COND_WAS_TRUE"}
         )
      );
   }
}
