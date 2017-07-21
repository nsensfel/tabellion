import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.Stack;

/* If Statement Node */
public class VHDLISNode extends VHDLNode
{
   private static final XPathExpression XPE_FIND_NAMED_ENTITIES;
   private static final XPathExpression XPE_FIND_TRUE_BRANCH;
   private static final XPathExpression XPE_FIND_ELSE_BRANCH;

   static
   {
      XPE_FIND_NAMED_ENTITIES =
         XMLManager.compile_or_die
         (
            "./condition//named_entity"
         );

      XPE_FIND_TRUE_BRANCH =
         XMLManager.compile_or_die
         (
            "./sequential_statement_chain"
         );

      XPE_FIND_ELSE_BRANCH =
         XMLManager.compile_or_die
         (
            "./else_clause/sequential_statement_chain"
         );
   }

   public VHDLISNode
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
      handle_function_expression(local_id);

      /** Predicates **********************************************************/
      handle_predicate_has_option(local_id);
      handle_predicate_expr_reads(local_id);

      /** Children ************************************************************/
      handle_true_branch(local_id, waiting_list);
      handle_else_branch(local_id, waiting_list);
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
         Strings.get_id_from_string
         (
            XMLManager.get_attribute(xml_node, "label")
         )
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
         Strings.get_id_from_string("if")
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
               output,
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
   private void handle_true_branch
   (
      final IDs local_id,
      final Stack<ParsableXML> waiting_list
   )
   throws XPathExpressionException
   {
      final Node true_branch;

      true_branch =
         (Node) XPE_FIND_TRUE_BRANCH.evaluate
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
            true_branch,
            local_id,
            next_node,
            (depth + 1),
            new String[] {"COND_WAS_TRUE"}
         )
      );
   }

   private void handle_else_branch
   (
      final IDs local_id,
      final Stack<ParsableXML> waiting_list
   )
   throws XPathExpressionException
   {
      final Node else_branch;

      else_branch =
         (Node) XPE_FIND_ELSE_BRANCH.evaluate
         (
            xml_node,
            XPathConstants.NODE
         );

      if (else_branch == (Node) null)
      {
         if (next_node == (IDs) null)
         {
            Predicates.add_entry
            (
               output,
               "is_final",
               local_id
            );
         }
         else
         {
            Predicates.add_entry
            (
               output,
               "node_connect",
               local_id,
               next_node
            );
         }
      }
      else
      {
         waiting_list.push
         (
            new VHDLSSCNode
            (
               output,
               parent_id,
               else_branch,
               local_id,
               next_node,
               (depth + 1),
               new String[] {"COND_WAS_FALSE"}
            )
         );
      }
   }
}
