import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.ArrayList;
import java.util.Collection;

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
            "./waveform_chain//named_entity"
         );

      XPE_FIND_PREFIXED_NE = XMLManager.compile_or_die("./prefix/named_entity");
      XPE_FIND_NE = XMLManager.compile_or_die("./named_entity");
   }

   public VHDLSSASNode
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
   public Collection<ParsableXML> parse ()
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
      handle_predicate_expr_writes(local_id);

      /** Children ************************************************************/
      handle_next_node(local_id);

      return (new ArrayList<ParsableXML>());
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
      final NodeList sources;
      final int sources_count;

      sources =
         (NodeList) XPE_FIND_SOURCES.evaluate
         (
            xml_node,
            XPathConstants.NODESET
         );

      sources_count = sources.getLength();

      for (int i = 0; i < sources_count; ++i)
      {
         final String ref;

         ref = XMLManager.get_attribute(sources.item(0), "ref");

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

   private void handle_predicate_expr_writes
   (
      final IDs local_id
   )
   throws XPathExpressionException
   {
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

      Predicates.add_entry
      (
         "expr_writes",
         local_id,
         Waveforms.get_associated_waveform_id
         (
            IDs.get_id_from_xml_id
            (
               XMLManager.get_attribute(target, "id"),
               (String) null
            )
         )
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
            "is_final",
            local_id
         );
      }
      else
      {
         Predicates.add_entry
         (
            "node_connect",
            local_id,
            next_node
         );
      }
   }
}
