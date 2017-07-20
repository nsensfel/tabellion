import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;

public class XMLManager
{
   /* ... */
   /* private static final DocumentBuilderFactoryCreatorMakerInstanciator; */
   /* private static final DocumentBuilderFactoryCreatorMaker; */
   /* private static final DocumentBuilderFactoryCreator; */
   private static final DocumentBuilderFactory DOC_BUILDER_FACTORY;
   private static final DocumentBuilder DOC_BUILDER;

   private static final XPathFactory XPATH_FACTORY;
   private static final XPath XPATH;

   static
   {
      DocumentBuilder i_dont_even;

      DOC_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

      try
      {
         i_dont_even = DOC_BUILDER_FACTORY.newDocumentBuilder();
      }
      catch (final Exception e)
      {
         i_dont_even = null;

         System.err.println
         (
            "[E] Err... You somehow managed to trigger an exception from purely"
            + " static members:"
         );

         e.printStackTrace();

         System.exit(-1);
      }

      DOC_BUILDER = i_dont_even;

      XPATH_FACTORY = XPathFactory.newInstance();
      XPATH = XPATH_FACTORY.newXPath();
   }

   private XMLManager () {} /* Utility Class. */

   public static Document get_document (final String filename)
   throws
      SAXException,
      IOException
   {
      final File file;

      file = new File(filename, "r");

      return DOC_BUILDER.parse(file);
   }

   public static XPathExpression compile (final String expression)
   throws XPathExpressionException
   {
      return XPATH.compile(expression);
   }

   public static Collection<Node> node_list_to_node_collection
   (
      final NodeList nl
   )
   {
      final Collection<Node> result;
      final int nl_length;

      result = new ArrayList<Node>();

      nl_length = nl.getLength();

      for (int i = 0; i < nl_length; ++i)
      {
         result.add(nl.item(i));
      }

      return result;
   }
}
