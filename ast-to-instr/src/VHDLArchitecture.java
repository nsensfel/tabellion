import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.ArrayList;
import java.util.Collection;

public class VHDLArchitecture extends ParsableXML
{
   private static final XPathExpression XPE_FIND_SIGNALS;
   private static final XPathExpression XPE_FIND_PROCESSES;
   private static final XPathExpression XPE_FIND_COMPONENTS;

   static
   {
      XPE_FIND_SIGNALS =
         XMLManager.compile_or_die
         (
            "./*/el[@kind=\"signal_declaration\"]"
         );

      XPE_FIND_PROCESSES =
         XMLManager.compile_or_die
         (
            "./*/el[@kind=\"sensitized_process_statement\"]"
         );

      XPE_FIND_COMPONENTS =
         XMLManager.compile_or_die
         (
            "./*/el[@kind=\"component_instantiation_statement\"]"
         );
   }

   public VHDLArchitecture
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

      xml_id = XMLManager.get_attribute(xml_node, "id");

      local_id = IDs.get_id_from_xml_id(xml_id, "architecture");

      /** Parent **************************************************************/
      handle_link_to_file(local_id);
      handle_link_to_entity(local_id);

      /** Functions ***********************************************************/
      handle_function_line(local_id);
      handle_function_column(local_id);
      handle_function_identifier(local_id);

      /** Predicates **********************************************************/
      handle_predicate_has_foreign_flag(local_id);
      handle_predicate_has_visible_flag(local_id);
      handle_predicate_is_withing_flag(local_id);
      handle_predicate_end_has_reserved_id(local_id);
      handle_predicate_end_has_identifier(local_id);

      /** Children ************************************************************/
      result.addAll(handle_child_signals(local_id));
      result.addAll(handle_child_processes(local_id));
      result.addAll(handle_child_components(local_id));

      return result;
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
   private void handle_predicate_has_foreign_flag
   (
      final IDs local_id
   )
   {
      if (XMLManager.get_attribute(xml_node, "foreign_flag").equals("true"))
      {
         Predicates.add_entry("has_foreign_flag", local_id);
      }
   }

   private void handle_predicate_has_visible_flag
   (
      final IDs local_id
   )
   {
      if (XMLManager.get_attribute(xml_node, "visible_flag").equals("true"))
      {
         Predicates.add_entry("has_visible_flag", local_id);
      }
   }

   private void handle_predicate_is_withing_flag
   (
      final IDs local_id
   )
   {
      if (XMLManager.get_attribute(xml_node, "is_within_flag").equals("true"))
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
   private Collection<ParsableXML> handle_child_signals
   (
      final IDs local_id
   )
   throws XPathExpressionException
   {
      final Collection<ParsableXML> result;
      final NodeList signals;
      final int childrens_count;

      result = new ArrayList<ParsableXML>();

      signals =
         (NodeList) XPE_FIND_SIGNALS.evaluate
         (
            xml_node,
            XPathConstants.NODESET
         );

      childrens_count = signals.getLength();

      for (int i = 0; i < childrens_count; ++i)
      {
         result.add(new VHDLSignal(local_id, signals.item(i)));
      }

      return result;
   }

   private Collection<ParsableXML> handle_child_processes
   (
      final IDs local_id
   )
   throws XPathExpressionException
   {
      final Collection<ParsableXML> result;
      final NodeList processes;
      final int childrens_count;

      result = new ArrayList<ParsableXML>();

      processes =
         (NodeList) XPE_FIND_PROCESSES.evaluate
         (
            xml_node,
            XPathConstants.NODESET
         );

      childrens_count = processes.getLength();

      for (int i = 0; i < childrens_count; ++i)
      {
         result.add(new VHDLProcess(local_id, processes.item(i)));
      }

      return result;
   }

   private Collection<ParsableXML> handle_child_components
   (
      final IDs local_id
   )
   throws XPathExpressionException
   {
      final Collection<ParsableXML> result;
      final NodeList components;
      final int childrens_count;

      result = new ArrayList<ParsableXML>();

      components =
         (NodeList) XPE_FIND_COMPONENTS.evaluate
         (
            xml_node,
            XPathConstants.NODESET
         );

      childrens_count = components.getLength();

      for (int i = 0; i < childrens_count; ++i)
      {
         result.add(new VHDLComponent(local_id, components.item(i)));
      }

      return result;
   }
}
