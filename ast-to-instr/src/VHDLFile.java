import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.ArrayList;
import java.util.Collection;

public class VHDLFile extends ParsableXML
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

      xml_id = null; /* TODO: elem.attrib.get("id") */

      local_id = IDs.get_id_from_xml_id(xml_id, "file");

      /** Functions ***********************************************************/
      handle_function_filename(local_id);

      /** Predicates **********************************************************/

      /** Children ************************************************************/
      result.addAll(handle_child_entities(local_id));
      result.addAll(handle_child_architectures(local_id));

      return null;
   }

   private void handle_function_filename
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

   private Collection<ParsableXML> handle_child_entities
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

   private Collection<ParsableXML> handle_child_architectures
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
