import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.Stack;

public class VHDLProcess extends ParsableXML
{
   private static final XPathExpression XPE_FIND_SL_ELEMENTS;
   private static final XPathExpression XPE_FIND_START_NODE;

   static
   {
      XPE_FIND_SL_ELEMENTS =
         XMLManager.compile_or_die
         (
            "(./sensitivity_list/el/named_entity | ./sensitivity_list/el[@ref])"
         );

      XPE_FIND_START_NODE =
         XMLManager.compile_or_die
         (
            "./sequential_statement_chain"
         );
   }

   public VHDLProcess
   (
      final IDs parent_id,
      final Node xml_node
   )
   {
      super(parent_id, xml_node);
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

      local_id = IDs.get_id_from_xml_id(xml_id, "process");

      /** Parent **************************************************************/
      handle_link_to_architecture(local_id);

      /** Functions ***********************************************************/
      handle_function_line(local_id);
      handle_function_column(local_id);
      handle_function_label(local_id);

      /** Predicates **********************************************************/
      handle_predicate_has_seen_flag(local_id);
      handle_predicate_end_has_postponed(local_id);
      handle_predicate_is_ref(local_id);
      handle_predicate_has_passive_flag(local_id);
      handle_predicate_has_postponed_flag(local_id);
      handle_predicate_has_visible_flag(local_id);
      handle_predicate_is_within_flag(local_id);
      handle_predicate_has_label(local_id);
      handle_predicate_has_is(local_id);
      handle_predicate_end_has_reserved_id(local_id);
      handle_predicate_end_has_identifier(local_id);

      handle_predicate_is_explicit_process(local_id);
      handle_predicate_is_in_sensitivity_list(local_id);

      /** Children ************************************************************/
      handle_child_node(local_id, waiting_list);
   }

   /***************************************************************************/
   /** Parents ****************************************************************/
   /***************************************************************************/
   private void handle_link_to_architecture
   (
      final IDs local_id
   )
   {
      Predicates.add_entry("belongs_to_architecture", local_id, parent_id);
   }

   /***************************************************************************/
   /** Functions **************************************************************/
   /***************************************************************************/
   private void handle_function_line
   (
      final IDs local_id
   )
   {
      Functions.add_entry
      (
         "line",
         local_id,
         Strings.get_id_from_string
         (
            XMLManager.get_attribute(xml_node, "line")
         )
      );
   }

   private void handle_function_column
   (
      final IDs local_id
   )
   {
      Functions.add_entry
      (
         "column",
         local_id,
         Strings.get_id_from_string
         (
            XMLManager.get_attribute(xml_node, "col")
         )
      );
   }

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

   /***************************************************************************/
   /** Predicates *************************************************************/
   /***************************************************************************/
   private void handle_predicate_has_seen_flag
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "seen_flag"
         ).equals("true")
      )
      {
         Predicates.add_entry("has_seen_flag", local_id);
      }
   }

   private void handle_predicate_end_has_postponed
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "end_has_postponed"
         ).equals("true")
      )
      {
         Predicates.add_entry("end_has_postponed", local_id);
      }
   }

   private void handle_predicate_is_ref
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "is_ref"
         ).equals("true")
      )
      {
         Predicates.add_entry("is_ref", local_id);
      }
   }

   private void handle_predicate_has_passive_flag
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "passive_flag"
         ).equals("true")
      )
      {
         Predicates.add_entry("has_passive_flag", local_id);
      }
   }

   private void handle_predicate_has_postponed_flag
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "postponed_flag"
         ).equals("true")
      )
      {
         Predicates.add_entry("has_postponed_flag", local_id);
      }
   }

   private void handle_predicate_has_visible_flag
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "visible_flag"
         ).equals("true")
      )
      {
         Predicates.add_entry("has_visible_flag", local_id);
      }
   }

   private void handle_predicate_is_within_flag
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "is_within_flag"
         ).equals("true")
      )
      {
         Predicates.add_entry("is_within_flag", local_id);
      }
   }

   private void handle_predicate_has_label
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "has_label"
         ).equals("true")
      )
      {
         Predicates.add_entry("has_label", local_id);
      }
   }

   private void handle_predicate_has_is
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "has_is"
         ).equals("true")
      )
      {
         Predicates.add_entry("has_is", local_id);
      }
   }

   private void handle_predicate_end_has_reserved_id
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "end_has_reserved_id"
         ).equals("true")
      )
      {
         Predicates.add_entry("end_has_reserved_id", local_id);
      }
   }

   private void handle_predicate_end_has_identifier
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "end_has_identifier"
         ).equals("true")
      )
      {
         Predicates.add_entry("end_has_identifier", local_id);
      }
   }

   private void handle_predicate_is_explicit_process
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "is_ref"
         ).equals("false")
      )
      {
         Predicates.add_entry("is_explicit_process", local_id);
      }
   }

   private void handle_predicate_is_in_sensitivity_list
   (
      final IDs local_id
   )
   throws XPathExpressionException
   {
      final NodeList items;
      final int items_count;

      items =
         (NodeList) XPE_FIND_SL_ELEMENTS.evaluate
         (
            xml_node,
            XPathConstants.NODESET
         );

      items_count = items.getLength();

      for (int i = 0; i < items_count; ++i)
      {
         Predicates.add_entry
         (
            "is_in_sensitivity_list",
            Waveforms.get_associated_waveform_id
            (
               IDs.get_id_from_xml_id
               (
                  XMLManager.get_attribute
                  (
                     items.item(i),
                     "ref"
                  ),
                  null
               )
            ),
            local_id
         );
      }
   }

   /***************************************************************************/
   /** Children ***************************************************************/
   /***************************************************************************/
   private void handle_child_node
   (
      final IDs local_id,
      final Stack<ParsableXML> waiting_list
   )
   throws XPathExpressionException
   {
      final Node start_node;

      start_node =
         (Node) XPE_FIND_START_NODE.evaluate
         (
            xml_node,
            XPathConstants.NODE
         );

      waiting_list.push
      (
         new VHDLSSCNode
         (
            OutputFile.new_output_file
            (
               "cfg_" /* TODO: Prefix as parameter? */
               + Integer.toString(local_id.get_value())
               + ".mod" /* TODO: Suffix as parameter? */
            ),
            local_id,
            start_node,
            null, /* There is nothing before this sequence. */
            null, /* There is nothing after this sequence. */
            0, /* Depth starts at zero. */
            new String[0] /* No attributes. */
         )
      );
   }
}
