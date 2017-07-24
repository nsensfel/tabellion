import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;

public abstract class VHDLNode extends ParsableXML
{
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

   protected void handle_expression
   (
      final IDs local_id,
      final Node expression_ref
   )
   throws XPathExpressionException
   {
      final String ref;

      ref = XMLManager.get_attribute(expression_ref, "ref");

      if (!Main.node_is_function_or_literal(ref))
      {
         Predicates.add_entry
         (
            output,
            predicate,
            local_id,
            Waveforms.get_associated_waveform_id
            (
               IDs.get_id_from_xml_id(ref, (String) null)
            )
         );
      }
   }
}
