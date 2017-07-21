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
   private static Document XML_ROOT;
   private static OutputFile MAIN_OUTPUT;

   static
   {
      XPE_FIND_ALL_VHDL_FILES =
         XMLManager.compile_or_die
            (
               //"./*/*/el[@kind=\"design_file\"][@file]"
               "//el[@kind=\"design_file\"][@file]"
            );
   }

   public static void main (final String... args)
   {
      final Collection<Node> vhdl_files;

      PARAMETERS = new Parameters(args);

      if (!PARAMETERS.are_valid())
      {
         return;
      }

      try
      {
         XML_ROOT = XMLManager.get_document(PARAMETERS.get_xml_file());
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

      MAIN_OUTPUT =
         OutputFile.new_output_file
         (
            PARAMETERS.get_main_output_filename()
         );

      try
      {
         vhdl_files =
            XMLManager.node_list_to_node_collection
            (
               (NodeList) XPE_FIND_ALL_VHDL_FILES.evaluate
               (
                  XML_ROOT,
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

      parse_content(vhdl_files);

      OutputFile.close_all();
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
            waiting_list.pop().parse(waiting_list);
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
      }
   }

   public static boolean node_is_function_or_literal (final String xml_id)
   throws XPathExpressionException
   {
      final XPathExpression xpe_find_el_from_id;
      final Node n;
      final String kind;

      xpe_find_el_from_id =
         XMLManager.compile_or_die
         (
            ".//el[@id=\""
            + xml_id
            + "\"]"
         );

      n =
         (Node) xpe_find_el_from_id.evaluate
         (
            XML_ROOT,
            XPathConstants.NODE
         );

      if (n == (Node) null)
      {
         return true;
      }

      kind = XMLManager.get_attribute(n, "kind");

      if (kind.equals("function_declaration"))
      {
         return true;
      }
      else if (kind.equals("enumeration_literal"))
      {
         return true;
      }

      return false;
   }

   public static Document get_xml_root ()
   {
      return XML_ROOT;
   }

   public static OutputFile get_main_output ()
   {
      return MAIN_OUTPUT;
   }
}
