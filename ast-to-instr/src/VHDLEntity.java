import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.ArrayList;
import java.util.Collection;

public class VHDLEntity extends ParsableXML
{
   private static final XPathExpression GET_ENTITIES;

   static
   {
      GET_ENTITIES = null;
      /* TODO
         Main.get_xpath().compile
         (
         );
      */
       //     "./*/*/library_unit[@kind=\"entity_declaration\"]"
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
   public Collection<ParsableXML> parse ()
   throws XPathExpressionException
   {
      final Collection<ParsableXML> result;
      final String xml_id;
      final IDs local_id;

      result = new ArrayList<ParsableXML>();

      xml_id = null; /* TODO: elem.attrib.get("id") */

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
      result.addAll(handle_child_ports(local_id));
      result.addAll(handle_child_generics(local_id));

      return null;
   }

   /***************************************************************************/
   /** Parents ****************************************************************/
   /***************************************************************************/
   private void handle_link_to_file
   (
      final IDs local_id
   )
   {
      final IDs params[];

      params =
         new IDs[]
         {
            local_id,
            parent_id
         };

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
      final IDs params[];

      params =
         new IDs[]
         {
            local_id,
            Strings.get_id_from_string
            (
               null /* TODO: get attribute */
            )
         };

      /* Functions.add_entry("filename", params); */
   }

   private void handle_function_column
   (
      final IDs local_id
   )
   {
      final IDs params[];

      params =
         new IDs[]
         {
            local_id,
            Strings.get_id_from_string
            (
               null /* TODO: get attribute */
            )
         };

      /* Functions.add_entry("filename", params); */
   }

   private void handle_function_identifier
   (
      final IDs local_id
   )
   {
      final IDs params[];

      params =
         new IDs[]
         {
            local_id,
            Strings.get_id_from_string
            (
               null /* TODO: get attribute */
            )
         };

      /* Functions.add_entry("filename", params); */
   }

   /***************************************************************************/
   /** Predicates *************************************************************/
   /***************************************************************************/
   private void handle_predicate_has_begin
   (
      final IDs local_id
   )
   {
      final IDs params[];

      params =
         new IDs[]
         {
            local_id,
            Strings.get_id_from_string
            (
               null /* TODO: get attribute */
            )
         };

      /* Functions.add_entry("filename", params); */
   }

   private void handle_predicate_has_visible_flag
   (
      final IDs local_id
   )
   {
      final IDs params[];

      params =
         new IDs[]
         {
            local_id,
            Strings.get_id_from_string
            (
               null /* TODO: get attribute */
            )
         };

      /* Functions.add_entry("filename", params); */
   }

   private void handle_predicate_is_withing_flag
   (
      final IDs local_id
   )
   {
      final IDs params[];

      params =
         new IDs[]
         {
            local_id,
            Strings.get_id_from_string
            (
               null /* TODO: get attribute */
            )
         };

      /* Functions.add_entry("filename", params); */
   }

   private void handle_predicate_end_has_reserved_id
   (
      final IDs local_id
   )
   {
      final IDs params[];

      params =
         new IDs[]
         {
            local_id,
            Strings.get_id_from_string
            (
               null /* TODO: get attribute */
            )
         };

      /* Functions.add_entry("filename", params); */
   }

   private void handle_predicate_end_has_identifier
   (
      final IDs local_id
   )
   {
      final IDs params[];

      params =
         new IDs[]
         {
            local_id,
            Strings.get_id_from_string
            (
               null /* TODO: get attribute */
            )
         };

      /* Functions.add_entry("filename", params); */
   }

   /***************************************************************************/
   /** Children ***************************************************************/
   /***************************************************************************/
   private Collection<ParsableXML> handle_child_ports
   (
      final IDs local_id
   )
   throws XPathExpressionException
   {
      final Collection<ParsableXML> result;
      final NodeList entities;

      entities =
         (NodeList) GET_ENTITIES.evaluate
         (
            xml_node,
            XPathConstants.NODESET
         );

      /* TODO */
      return null;
   }

   private Collection<ParsableXML> handle_child_generics
   (
      final IDs local_id
   )
   throws XPathExpressionException
   {
      final Collection<ParsableXML> result;

      /* TODO */
      return null;
   }
}
