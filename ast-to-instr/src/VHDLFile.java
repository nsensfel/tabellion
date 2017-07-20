import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.ArrayList;
import java.util.Collection;

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
            "./*/*/library_unit[@kind=\"entity_declaration\"]"
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
   public Collection<ParsableXML> parse ()
   throws XPathExpressionException
   {
      final Collection<ParsableXML> result;
      final String xml_id;
      final IDs local_id;

      result = new ArrayList<ParsableXML>();

      xml_id = XMLManager.get_attribute(xml_node, "id");

      local_id = IDs.get_id_from_xml_id(xml_id, "file");

      /** Functions ***********************************************************/
      handle_function_filename(local_id);

      /** Predicates **********************************************************/

      /** Children ************************************************************/
      result.addAll(handle_child_entities(local_id));
      result.addAll(handle_child_architectures(local_id));

      return result;
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
   private Collection<ParsableXML> handle_child_entities
   (
      final IDs local_id
   )
   throws XPathExpressionException
   {
      final Collection<ParsableXML> result;
      final NodeList entities;
      final int children_count;

      result = new ArrayList<ParsableXML>();

      entities =
         (NodeList) XPE_FIND_ENTITIES.evaluate
         (
            xml_node,
            XPathConstants.NODESET
         );

      children_count = entities.getLength();

      for (int i = 0; i < children_count; ++i)
      {
         result.add(new VHDLEntity(local_id, entities.item(i)));
      }

      return result;
   }

   private Collection<ParsableXML> handle_child_architectures
   (
      final IDs local_id
   )
   throws XPathExpressionException
   {
      final Collection<ParsableXML> result;
      final NodeList architectures;
      final int children_count;

      result = new ArrayList<ParsableXML>();

      architectures =
         (NodeList) XPE_FIND_ENTITIES.evaluate
         (
            xml_node,
            XPathConstants.NODESET
         );

      children_count = architectures.getLength();

      for (int i = 0; i < children_count; ++i)
      {
         result.add(new VHDLArchitecture(local_id, architectures.item(i)));
      }

      return result;
   }
}
