import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.Stack;

public class VHDLFile extends ParsableXML
{
   private static final XPathExpression XPE_FIND_ENTITIES;
   private static final XPathExpression XPE_FIND_ARCHITECTURES;

   static
   {
      XPE_FIND_ENTITIES =
         XMLManager.compile_or_die
         (
            "./*/*/library_unit[@kind=\"entity_declaration\"]"
         );

      XPE_FIND_ARCHITECTURES =
         XMLManager.compile_or_die
         (
            "./*/*/library_unit[@kind=\"architecture_body\"]"
         );
   }

   public VHDLFile
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

      local_id = IDs.get_id_from_xml_id(xml_id, "file");

      /** Functions ***********************************************************/
      handle_function_filename(local_id);

      /** Predicates **********************************************************/

      /** Children ************************************************************/
      handle_child_entities(local_id, waiting_list);
      handle_child_architectures(local_id, waiting_list);
   }

   /***************************************************************************/
   /** Functions **************************************************************/
   /***************************************************************************/
   private void handle_function_filename
   (
      final IDs local_id
   )
   {
      Functions.add_entry
      (
         "filename",
         local_id,
         Strings.get_id_from_string
         (
            XMLManager.get_attribute(xml_node, "file")
         )
      );
   }

   /***************************************************************************/
   /** Children ***************************************************************/
   /***************************************************************************/
   private void handle_child_entities
   (
      final IDs local_id,
      final Stack<ParsableXML> waiting_list
   )
   throws XPathExpressionException
   {
      final NodeList entities;
      final int children_count;

      entities =
         (NodeList) XPE_FIND_ENTITIES.evaluate
         (
            xml_node,
            XPathConstants.NODESET
         );

      children_count = entities.getLength();

      for (int i = 0; i < children_count; ++i)
      {
         waiting_list.push(new VHDLEntity(local_id, entities.item(i)));
      }
   }

   private void handle_child_architectures
   (
      final IDs local_id,
      final Stack<ParsableXML> waiting_list
   )
   throws XPathExpressionException
   {
      final NodeList architectures;
      final int children_count;

      architectures =
         (NodeList) XPE_FIND_ARCHITECTURES.evaluate
         (
            xml_node,
            XPathConstants.NODESET
         );

      children_count = architectures.getLength();

      for (int i = 0; i < children_count; ++i)
      {
         waiting_list.push
         (
            new VHDLArchitecture(local_id, architectures.item(i))
         );
      }
   }
}
