import org.w3c.dom.Node;

public abstract class VHDLNode extends ParsableXML
{
   protected final IDs next_node;
   protected final int depth;
   protected final String[] attributes;

   public VHDLNode
   (
      final IDs parent_id,
      final Node xml_node,
      final IDs next_node,
      final int depth,
      final String[] attributes
   )
   {
      super(parent_id, xml_node);

      this.next_node = next_node;
      this.depth = depth;
      this.attributes = attributes;
   }

}