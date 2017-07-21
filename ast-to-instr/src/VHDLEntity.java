import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.Stack;

public class VHDLEntity extends ParsableXML
{
   private static final XPathExpression XPE_FIND_PORTS;
   private static final XPathExpression XPE_FIND_GENERICS;

   static
   {
      XPE_FIND_PORTS =
         XMLManager.compile_or_die
         (
            "./port_chain/el[@kind=\"interface_signal_declaration\"]"
         );

      XPE_FIND_GENERICS =
         XMLManager.compile_or_die
         (
            "./generic_chain/el[@kind=\"interface_constant_declaration\"]"
         );
   }

   public VHDLEntity
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

      local_id = IDs.get_id_from_xml_id(xml_id, "entity");

      /** Parent **************************************************************/
      handle_link_to_file(local_id);

      /** Functions ***********************************************************/
      handle_function_line(local_id);
      handle_function_column(local_id);
      handle_function_identifier(local_id);

      /** Predicates **********************************************************/
      handle_predicate_has_begin(local_id);
      handle_predicate_has_visible_flag(local_id);
      handle_predicate_is_withing_flag(local_id);
      handle_predicate_end_has_reserved_id(local_id);
      handle_predicate_end_has_identifier(local_id);

      /** Children ************************************************************/
      handle_child_ports(local_id, waiting_list);
      handle_child_generics(local_id, waiting_list);
   }

   /***************************************************************************/
   /** Parents ****************************************************************/
   /***************************************************************************/
   private void handle_link_to_file
   (
      final IDs local_id
   )
   {
      Predicates.add_entry("is_in_file", local_id, parent_id);
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
            XMLManager.get_attribute(xml_node, "col")
         )
      );
   }

   /***************************************************************************/
   /** Predicates *************************************************************/
   /***************************************************************************/
   private void handle_predicate_has_begin
   (
      final IDs local_id
   )
   {
      if
      (
         XMLManager.get_attribute
         (
            xml_node,
            "has_begin"
         ).equals("true")
      )
      {
         Predicates.add_entry("has_begin", local_id);
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

   private void handle_predicate_is_withing_flag
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

   /***************************************************************************/
   /** Children ***************************************************************/
   /***************************************************************************/
   private void handle_child_ports
   (
      final IDs local_id,
      final Stack<ParsableXML> waiting_list
   )
   throws XPathExpressionException
   {
      final NodeList ports;
      final int children_count;

      ports =
         (NodeList) XPE_FIND_PORTS.evaluate
         (
            xml_node,
            XPathConstants.NODESET
         );

      children_count = ports.getLength();

      for (int i = 0; i < children_count; ++i)
      {
         waiting_list.push(new VHDLPort(local_id, ports.item(i)));
      }
   }

   private void handle_child_generics
   (
      final IDs local_id,
      final Stack<ParsableXML> waiting_list
   )
   throws XPathExpressionException
   {
      final NodeList generics;
      final int children_count;

      generics =
         (NodeList) XPE_FIND_GENERICS.evaluate
         (
            xml_node,
            XPathConstants.NODESET
         );

      children_count = generics.getLength();

      for (int i = 0; i < children_count; ++i)
      {
         waiting_list.push(new VHDLGeneric(local_id, generics.item(i)));
      }
   }
}
