import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.Stack;

public class VHDLComponent extends ParsableXML
{
   private static final XPathExpression XPE_FIND_INST_UNIT;
   private static final XPathExpression XPE_FIND_BASE_NAME;

   private static final XPathExpression XPE_FIND_PORT_MAPS;
   private static final XPathExpression XPE_FIND_REAL_PORTS;

   private static final XPathExpression XPE_FIND_GENERIC_MAPS;

   static
   {
      XPE_FIND_INST_UNIT = XMLManager.compile_or_die("./instantiated_unit");
      XPE_FIND_BASE_NAME = XMLManager.compile_or_die("./base_name");

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

   private Node find_entity_from_internal_ref (Node current_node)
   throws XPathExpressionException
   {
      XPathExpression current_query;

      /*
       * Get the item containing the reference to the internal description
       * of this component.
       */
      current_node =
         (Node) XPE_FIND_BASE_NAME.evaluate
         (
            current_node,
            XPathConstants.NODE
         );

      /* Get the referenced component declaration. */
      current_query =
         XMLManager.compile_or_die
         (
            "./../../declaration_chain/el[@kind=\"component_declaration\"]"
            + "[@id=\""
            + XMLManager.get_attribute(current_node, "ref")
            + "\"]"
         );
      current_node =
         (Node) current_query.evaluate
         (
            xml_node,
            XPathConstants.NODE
         );

      /* Actually get the entity. */
      current_query =
         XMLManager.compile_or_die
         (
            ".//library_unit[@kind=\"entity_declaration\"][@identifier=\""
            + XMLManager.get_attribute(current_node, "identifier")
            + "\"]"
         );

      return
         (Node) current_query.evaluate
         (
            current_node,
            XPathConstants.NODE
         );
   }

   private void handle_link_to_entity
   (
      final IDs local_id
   )
   throws XPathExpressionException
   {
      final String kind;
      Node current_node;

      current_node =
         (Node) XPE_FIND_INST_UNIT.evaluate
         (
            xml_node,
            XPathConstants.NODE
         );

      kind = XMLManager.get_attribute(current_node, "kind");

      if (kind.equals("simple_name"))
      {
         current_node = find_entity_from_internal_ref(current_node);
      }
      else if (kind.equals("entity_aspect_entity"))
      {
         /* TODO */
         //current_node = find_entity_from_external_ref(current_node);
      }
      else
      {
         System.err.println
         (
            "[E] Unsupported component instantiation type for element "
            + local_id.get_value()
            + " (XML_ID: "
            + XMLManager.get_attribute(xml_node, "id")
            + ")."
         );

         return;
      }

      if (current_node == (Node) null)
      {
         System.err.println
         (
            "[E] Could not find any entity for the component instantiation "
            + local_id.get_value()
            + " (XML_ID: "
            + XMLManager.get_attribute(xml_node, "id")
            + ")."
         );

         return;
      }

      Predicates.add_entry
      (
         "is_component_of",
         local_id,
         IDs.get_id_from_xml_id
         (
            XMLManager.get_attribute(current_node, "id"),
            "entity"
         )
      );
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
