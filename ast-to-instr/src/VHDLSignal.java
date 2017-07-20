import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.ArrayList;
import java.util.Collection;

public class VHDLSignal extends ParsableXML
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

   public VHDLSignal
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
   private void handle_predicate_has_disconnect_flag
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

   private void handle_predicate_has_active_flag
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

   private void handle_predicate_has_identifier_list
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

   private void handle_predicate_has_after_drivers_flag
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

   private void handle_predicate_has_use_flag
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

   private void handle_predicate_has_guarded_signal_flag
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

   private void handle_predicate_is_of_kind
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
   private void handle_child_waveform
   (
      final IDs local_id
   )
   {
      /* TODO */
   }
}
