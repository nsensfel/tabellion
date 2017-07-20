import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.ArrayList;
import java.util.Collection;

public class VHDLProcess extends ParsableXML
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

   public VHDLProcess
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
      handle_predicate_has_label(local_id);
      handle_predicate_has_is(local_id);
      handle_predicate_end_has_reserved_id(local_id);
      handle_predicate_end_has_identifier(local_id);

      handle_predicate_is_explicit_process(local_id);
      handle_predicate_is_in_sensitivity_list(local_id);
      handle_predicate_is_accessed_by(local_id);

      /** Children ************************************************************/
      result.addAll(handle_child_nodes(local_id));

      return null;
   }

   /***************************************************************************/
   /** Parents ****************************************************************/
   /***************************************************************************/
   private void handle_link_to_architecture
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

   private void handle_function_label
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
   private void handle_predicate_has_seen_flag
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

   private void handle_predicate_end_has_postponed
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

   private void handle_predicate_is_ref
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

   private void handle_predicate_has_passive_flag
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

   private void handle_predicate_has_postponed_flag
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

   private void handle_predicate_has_label
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

   private void handle_predicate_has_is
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

   private void handle_predicate_is_explicit_process
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

   private void handle_predicate_is_in_sensitivity_list
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

   private void handle_predicate_is_accessed_by
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
   private Collection<ParsableXML> handle_child_nodes
   (
      final IDs local_id
   )
   {
      /* TODO */
      return null;
   }
}
