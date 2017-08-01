import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.Stack;

/* Simple Signal Assignment Statement Node */
public class VHDLSSASNode extends VHDLNode
{
   private static final XPathExpression XPE_FIND_TARGET;
   private static final XPathExpression XPE_FIND_SOURCES;
   private static final XPathExpression XPE_FIND_PREFIXED_NE;
   private static final XPathExpression XPE_FIND_NE;

   static
   {
      XPE_FIND_TARGET = XMLManager.compile_or_die("./target");

      XPE_FIND_SOURCES =
         XMLManager.compile_or_die
         (
            "./waveform_chain/el/we_value"/* //named_entity" */
         );

      XPE_FIND_PREFIXED_NE = XMLManager.compile_or_die("./prefix/named_entity");
      XPE_FIND_NE = XMLManager.compile_or_die("./named_entity");
   }

   public VHDLSSASNode
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
      handle_predicate_expr_writes(local_id);

      /** Children ************************************************************/
      handle_next_node(local_id);
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
         Strings.get_id_from_string("signal_assignement")
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

      handle_read_expr_predicates(local_id, sources);
   }

   private void handle_predicate_expr_writes
   (
      final IDs local_id
   )
   throws XPathExpressionException
   {
      final IDs target_id;
      Node target;

      target =
         (Node) XPE_FIND_TARGET.evaluate
         (
            xml_node,
            XPathConstants.NODE
         );

      /* Oddly enough, we can get a target as a ref... */
      /* Let's get the real source! */
      while (target.getNodeName().equals("target"))
      {
         final XPathExpression xpe_find_source;

         if (XMLManager.get_attribute(target, "kind").equals("indexed_name"))
         {
            target =
               (Node) XPE_FIND_PREFIXED_NE.evaluate
               (
                  target,
                  XPathConstants.NODE
               );
         }
         else
         {
            target =
               (Node) XPE_FIND_NE.evaluate
               (
                  target,
                  XPathConstants.NODE
               );
         }

         /* XXX "or_die" might be a bit abusive here. */
         xpe_find_source =
            XMLManager.compile_or_die
            (
               ".//*[@id=\""
               + XMLManager.get_attribute(target, "ref")
               + "\"]"
            );

         target =
            (Node) xpe_find_source.evaluate
            (
               Main.get_xml_root(),
               XPathConstants.NODE
            );
      }

      target_id =
         Waveforms.get_associated_waveform_id
         (
            IDs.get_id_from_xml_id
            (
               XMLManager.get_attribute(target, "id"),
               (String) null
            )
         );

      Predicates.add_entry
      (
         output,
         "expr_writes",
         local_id,
         target_id
      );

      Predicates.add_entry
      (
         "is_accessed_by",
         target_id,
         parent_id
      );
   }

   /***************************************************************************/
   /** Children ***************************************************************/
   /***************************************************************************/
   private void handle_next_node
   (
      final IDs local_id
   )
   {
      if (next_node == (IDs) null)
      {
         Predicates.add_entry
         (
            output,
            "is_terminal",
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
}
