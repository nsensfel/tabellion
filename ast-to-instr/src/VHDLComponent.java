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
   private static final XPathExpression XPE_FIND_ENTITY_NAME;
   private static final XPathExpression XPE_FIND_LIBRARY_NAME;

   private static final XPathExpression XPE_FIND_PORT_MAPS;
   private static final XPathExpression XPE_FIND_REAL_PORTS;
   private static final XPathExpression XPE_FIND_ACTUAL_NE;
   private static final XPathExpression XPE_FIND_FORMAL;

   private static final XPathExpression XPE_FIND_GENERIC_MAPS;

   static
   {
      XPE_FIND_INST_UNIT = XMLManager.compile_or_die("./instantiated_unit");
      XPE_FIND_BASE_NAME = XMLManager.compile_or_die("./base_name");

      XPE_FIND_ENTITY_NAME = XMLManager.compile_or_die
      (
        "./entity_name[@kind=\"selected_name\"]"
      );

      XPE_FIND_LIBRARY_NAME = XMLManager.compile_or_die
      (
         "./prefix[@kind=\"simple_name\"]"
      );

      XPE_FIND_PORT_MAPS =
         XMLManager.compile_or_die
         (
            "./port_map_aspect_chain/el"
            + "[@kind=\"association_element_by_expression\"]"
         );

      XPE_FIND_REAL_PORTS =
         XMLManager.compile_or_die
         (
            "./port_chain/el[@kind=\"interface_signal_declaration\"]"
         );

      XPE_FIND_ACTUAL_NE = XMLManager.compile_or_die("./actual/named_entity");
      XPE_FIND_FORMAL = XMLManager.compile_or_die("./formal");

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
      final Node linked_entity;

      xml_id = XMLManager.get_attribute(xml_node, "id");

      local_id = IDs.get_id_from_xml_id(xml_id, "component");

      /** Parent **************************************************************/
      handle_link_to_architecture(local_id);
      linked_entity = handle_link_to_entity(local_id);

      /** Functions ***********************************************************/
      handle_function_line(local_id);
      handle_function_column(local_id);
      handle_function_label(local_id);

      /** Predicates **********************************************************/

      if (linked_entity != (Node) null)
      {
         handle_predicate_port_maps(local_id, linked_entity);
         handle_predicate_generic_maps(local_id, linked_entity);
      }
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
            Main.get_xml_root(),
            XPathConstants.NODE
         );
   }

   private Node find_entity_from_direct_ref (final Node source_node)
   throws XPathExpressionException
   {
      final XPathExpression xpe_get_matching_entity;
      final Node entity_ref, library_ref;

      entity_ref =
         (Node) XPE_FIND_ENTITY_NAME.evaluate
         (
            source_node,
            XPathConstants.NODE
         );

      library_ref =
         (Node) XPE_FIND_ENTITY_NAME.evaluate
         (
            entity_ref,
            XPathConstants.NODE
         );

      xpe_get_matching_entity =
         XMLManager.compile_or_die
         (
            "./el[@kind=\"library_declaration\"][@identifier=\""
            + XMLManager.get_attribute(library_ref, "identifier")
            + "\"]//library_unit[@kind=\"entity_declaration\"][@identifier=\""
            + XMLManager.get_attribute(entity_ref, "identifier")
            + "\"]"
         );

      return
         (Node) xpe_get_matching_entity.evaluate
         (
            Main.get_xml_root(),
            XPathConstants.NODE
         );
   }

   private Node handle_link_to_entity
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
         current_node = find_entity_from_direct_ref(current_node);
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

         return null;
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

         return null;
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

      return current_node;
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
      final IDs local_id,
      final Node linked_entity
   )
   throws XPathExpressionException
   {
      final NodeList port_maps, real_ports;
      final int port_maps_length;

      port_maps =
         (NodeList) XPE_FIND_PORT_MAPS.evaluate
         (
            xml_node,
            XPathConstants.NODESET
         );

      real_ports =
         (NodeList) XPE_FIND_REAL_PORTS.evaluate
         (
            linked_entity,
            XPathConstants.NODESET
         );

      port_maps_length = port_maps.getLength();

      for (int i = 0; i < port_maps_length; ++i)
      {
         final Node mapping, formal_name, actual_waveform;

         mapping = port_maps.item(i);

         actual_waveform =
            (Node) XPE_FIND_ACTUAL_NE.evaluate
            (
               mapping,
               XPathConstants.NODE
            );

         formal_name =
            (Node) XPE_FIND_FORMAL.evaluate
            (
               mapping,
               XPathConstants.NODE
            );

         if (formal_name == null)
         {
            Predicates.add_entry
            (
               "port_maps",
               local_id,
               Waveforms.get_associated_waveform_id
               (
                  IDs.get_id_from_xml_id
                  (
                     XMLManager.get_attribute(actual_waveform, "ref"),
                     null
                  )
               ),
               IDs.get_id_from_xml_id
               (
                  XMLManager.get_attribute(real_ports.item(i), "id"),
                  "port"
               )
            );
         }
         else
         {
            final XPathExpression xpe_find_true_port;
            final Node true_port;

            xpe_find_true_port =
               XMLManager.compile_or_die
               (
                  "./port_chain/el[@identifier=\""
                  + XMLManager.get_attribute(formal_name, "identifier")
                  + "\"]"
               );

            true_port =
               (Node) xpe_find_true_port.evaluate
               (
                  linked_entity,
                  XPathConstants.NODE
               );

            Predicates.add_entry
            (
               "port_maps",
               local_id,
               Waveforms.get_associated_waveform_id
               (
                  IDs.get_id_from_xml_id
                  (
                     XMLManager.get_attribute(actual_waveform, "ref"),
                     null
                  )
               ),
               IDs.get_id_from_xml_id
               (
                  XMLManager.get_attribute(true_port, "id"),
                  "port"
               )
            );
         }
      }
   }

   private void handle_predicate_generic_maps
   (
      final IDs local_id,
      final Node linked_entity
   )
   throws XPathExpressionException
   {
      /* TODO */
   }
}
