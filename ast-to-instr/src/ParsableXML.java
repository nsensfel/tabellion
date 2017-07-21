import org.w3c.dom.Node;

import java.util.Stack;

import javax.xml.xpath.XPathExpressionException;

public abstract class ParsableXML
{
   protected final IDs parent_id;
   protected final Node xml_node;

   public ParsableXML
   (
      final IDs parent_id,
      final Node xml_node
   )
   {
      this.parent_id = parent_id;
      this.xml_node = xml_node;
   }

   public void parse
   (
      final Stack<ParsableXML> waiting_list
   )
   throws XPathExpressionException
   {

   }
}
