import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathExpression;

public abstract class VHDLNode extends ParsableXML
{
   /** Static *****************************************************************/
   private static XPathExpression XPE_FIND_EXPR_NAMED_ENTITIES;

   static
   {
      XPE_FIND_EXPR_NAMED_ENTITIES =
         XMLManager.compile_or_die
         (
            ".//named_entity"
         );
   }

   protected void handle_expression
   (
      final IDs local_id,
      final Node expression_start
   )
   throws XPathExpressionException
   {
      final String ref;

      ref = XMLManager.get_attribute(expression_start, "ref");

      if (!Main.node_is_function_or_literal(ref))
      {
         Predicates.add_entry
         (
            output,
            "expr_reads",
            local_id,
            Waveforms.get_associated_waveform_id
            (
               IDs.get_id_from_xml_id(ref, (String) null)
            )
         );
      }
   }

   /** Non-Static *************************************************************/
   protected final IDs next_node;
   protected final int depth;
   protected final String[] attributes;
   protected final OutputFile output;

   public VHDLNode
   (
      final OutputFile output,
      final IDs parent_id,
      final Node xml_node,
      final IDs next_node,
      final int depth,
      final String[] attributes
   )
   {
      super(parent_id, xml_node);

      this.output = output;
      this.next_node = next_node;
      this.depth = depth;
      this.attributes = attributes;
   }

}
