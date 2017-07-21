import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.Stack;

public class VHDLComponent extends ParsableXML
{
   private static final XPathExpression XPE_FIND_PORT_MAPS;
   private static final XPathExpression XPE_FIND_REAL_PORTS;
   private static final XPathExpression XPE_FIND_GENERIC_MAPS;

   static
   {
      XPE_FIND_PORT_MAPS =
         XMLManager.compile_or_die
         (
            "./el[@kind=\"association_element_by_expression\"]"
         );

      XPE_FIND_REAL_PORTS =
         XMLManager.compile_or_die
         (
            "./port_chain/el[@kind=\"interface_signal_declaration\"]"
         );

      /* TODO */
      XPE_FIND_GENERIC_MAPS = null; /* TODO */
   }

   public VHDLComponent
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

      local_id = IDs.get_id_from_xml_id(xml_id, "component");

      /** Parent **************************************************************/
      handle_link_to_architecture(local_id);
      handle_link_to_entity(local_id);

      /** Functions ***********************************************************/
      handle_function_line(local_id);
      handle_function_column(local_id);
      handle_function_label(local_id);

      /** Predicates **********************************************************/
      handle_predicate_port_maps(local_id);
      handle_predicate_generic_maps(local_id);
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

   private void handle_link_to_entity
   (
      final IDs local_id
   )
   {
      /* TODO */
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
   private void handle_predicate_port_maps
   (
      final IDs local_id
   )
   {
      /* TODO */
   }

   private void handle_predicate_generic_maps
   (
      final IDs local_id
   )
   {
      /* TODO */
   }
}
