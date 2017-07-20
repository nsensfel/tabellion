import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.ArrayList;
import java.util.Collection;

/* If Statement Node */
public class VHDLISNode extends VHDLNode
{
   private static final XPathExpression XPE_FIND_SUB_NODES;

   static
   {
      XPE_FIND_SUB_NODES = XMLManager.compile_or_die("./el");
   }

   public VHDLISNode
   (
      final IDs parent_id,
      final Node xml_node,
      final IDs next_node,
      final int depth,
      final String[] attributes
   )
   {
      super
      (
         parent_id,
         xml_node,
         next_node,
         depth,
         attributes
      );
   }

   @Override
   public Collection<ParsableXML> parse ()
   throws XPathExpressionException
   {
      /* TODO */
      return null;
   }
}
