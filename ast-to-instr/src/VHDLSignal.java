import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.Stack;

public class VHDLSignal extends ParsableXML
{
   public VHDLSignal
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

      local_id = IDs.get_id_from_xml_id(xml_id, "signal");

      /** Parent **************************************************************/
      handle_link_to_architecture(local_id);

      /** Functions ***********************************************************/
      handle_function_line(local_id);
      handle_function_column(local_id);
      handle_function_identifier(local_id);

      /** Predicates **********************************************************/
      handle_predicate_has_disconnect_flag(local_id);
      handle_predicate_is_ref(local_id);
      handle_predicate_has_active_flag(local_id);
      handle_predicate_has_identifier_list(local_id);
      handle_predicate_has_visible_flag(local_id);
      handle_predicate_has_after_drivers_flag(local_id);
      handle_predicate_has_use_flag(local_id);
      handle_predicate_has_guarded_signal_flag(local_id);

      handle_predicate_is_of_kind(local_id);

      /** Children ************************************************************/
      handle_child_waveform(local_id);
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

   private void handle_function_identifier
   (
      final IDs local_id
   )
   {
      Functions.add_entry
      (
         "identifier",
         local_id,
         Strings.get_id_from_string
         (
            XMLManager.get_attribute(xml_node, "identifier")
         )
      );
   }

   /***************************************************************************/
   /** Predicates *************************************************************/
   /***************************************************************************/
   private void handle_predicate_has_disconnect_flag
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "has_disconnect_flag"
         ).equals("true")
      )
      {
         Predicates.add_entry("has_disconnect_flag", local_id);
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

   private void handle_predicate_has_active_flag
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "has_active_flag"
         ).equals("true")
      )
      {
         Predicates.add_entry("has_active_flag", local_id);
      }
   }

   private void handle_predicate_has_identifier_list
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "has_identifier_list"
         ).equals("true")
      )
      {
         Predicates.add_entry("has_identifier_list", local_id);
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

   private void handle_predicate_has_after_drivers_flag
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "after_drivers_flag"
         ).equals("true")
      )
      {
         Predicates.add_entry("has_after_drivers_flag", local_id);
      }
   }

   private void handle_predicate_has_use_flag
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "use_flag"
         ).equals("true")
      )
      {
         Predicates.add_entry("has_use_flag", local_id);
      }
   }

   private void handle_predicate_has_guarded_signal_flag
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "guarded_signal_flag"
         ).equals("true")
      )
      {
         Predicates.add_entry("has_guarded_signal_flag", local_id);
      }
   }

   private void handle_predicate_is_of_kind
   (
      final IDs local_id
   )
   {
      Predicates.add_entry
      (
         "is_of_kind",
         local_id,
         Strings.get_id_from_string
         (
            XMLManager.get_attribute
            (
               xml_node,
               "signal_kind"
            )
         )
      );
   }

   /***************************************************************************/
   /** Children ***************************************************************/
   /***************************************************************************/
   private void handle_child_waveform
   (
      final IDs local_id
   )
   {
      Predicates.add_entry
      (
         "is_waveform_of",
         Waveforms.get_associated_waveform_id
         (
            local_id
         ),
         local_id
      );
   }
}
