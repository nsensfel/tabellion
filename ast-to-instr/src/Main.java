import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.util.Collection;
import java.util.Stack;

public class Main
{
   private static final XPathExpression XPE_FIND_ALL_VHDL_FILES;
   private static Parameters PARAMETERS;

   static
   {
      XPathExpression xpe;

      try
      {
         xpe = XMLManager.compile("./*/*/el[@kind=\"design_file\"][@file]");
      }
      catch (final XPathExpressionException xpee)
      {
         xpe = null;

         System.err.println
         (
            "[P] Invalid XPath expression (report as bug):"
         );

         xpee.printStackTrace();
      }

      XPE_FIND_ALL_VHDL_FILES = xpe;
   }

   public static void main (final String... args)
   {
      final Document root;
      final Collection<Node> vhdl_files;

      PARAMETERS = new Parameters(args);

      if (!PARAMETERS.are_valid())
      {
         return;
      }

      try
      {
         root = XMLManager.get_document(PARAMETERS.get_xml_file());
      }
      catch (final Exception e)
      {
         System.err.println
         (
            "[E] Could not load XML file \""
            + PARAMETERS.get_xml_file()
            + "\":"
         );

         e.printStackTrace();

         return;
      }

      try
      {
         vhdl_files =
            XMLManager.node_list_to_node_collection
            (
               (NodeList) XPE_FIND_ALL_VHDL_FILES.evaluate
               (
                  root,
                  XPathConstants.NODESET
               )
            );
      }
      catch (final XPathExpressionException xpee)
      {
         System.err.println
         (
            "[E] Something went wrong when looking for the VHDL files:"
         );

         xpee.printStackTrace();

         return;
      }
   }

   private static void parse_content (final Collection<Node> vhdl_files)
   {
      /* Stack highly recommended over FIFO (you don't want to grow large). */
      final Stack<ParsableXML> waiting_list;

      waiting_list = new Stack<ParsableXML>();

      for (final Node f: vhdl_files)
      {
         waiting_list.push(new VHDLFile(null, f));
      }

      while (!waiting_list.isEmpty())
      {
         final Collection<ParsableXML> children;

         try
         {
            children = waiting_list.pop().parse();
         }
         catch (final XPathExpressionException xpee)
         {
            System.err.println
            (
               "[E] Something went wrong while parsing the XML file:"
            );

            xpee.printStackTrace();

            return;
         }

         for (final ParsableXML c: children)
         {
            waiting_list.push(c);
         }
      }
   }
}
