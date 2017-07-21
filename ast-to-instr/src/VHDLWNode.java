import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.Stack;

/* When Node */
public class VHDLWNode extends VHDLNode
{
   private static final XPathExpression XPE_FIND_NAMED_ENTITIES;
   private static final XPathExpression XPE_FIND_BODY;

   static
   {
      XPE_FIND_NAMED_ENTITIES =
         XMLManager.compile_or_die
         (
            "./choice_expression//named_entity"
         );

      XPE_FIND_BODY =
         XMLManager.compile_or_die
         (
            "./associated_chain"
         );
   }

   public VHDLWNode
   (
      final IDs parent_id,
      final Node xml_node,
      final IDs next_node,
      final int depth,
      final String[] attributes
   )
   {
      super
      (
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

      local_id = IDs.get_id_from_xml_id(xml_id, "node");

      /** Functions ***********************************************************/
      handle_function_label(local_id);
      handle_function_kind(local_id);
      handle_function_depth(local_id);
      handle_function_expression(local_id);

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
         "depth",
         local_id,
         Strings.get_id_from_string
         (
            Integer.toString(depth)
         )
      );
   }

   private void handle_function_expression
   (
      final IDs local_id
   )
   {
      /* TODO */
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
      final NodeList named_entities;
      final int named_entities_count;

      named_entities =
         (NodeList) XPE_FIND_NAMED_ENTITIES.evaluate
         (
            xml_node,
            XPathConstants.NODESET
         );

      named_entities_count = named_entities.getLength();

      for (int i = 0; i < named_entities_count; ++i)
      {
         final String ref;

         ref = XMLManager.get_attribute(named_entities.item(0), "ref");

         if (!Main.node_is_function_or_literal(ref))
         {
            Predicates.add_entry
            (
               "expr_reads",
               local_id,
               Waveforms.get_associated_waveform_id
               (
                  IDs.get_id_from_xml_id(ref, (String) null)
               )
            );
         }
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
