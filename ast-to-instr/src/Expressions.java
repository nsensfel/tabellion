import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

public class Expressions
{
   private static final XPathExpression XPE_GET_LEFT_SIDE;
   private static final XPathExpression XPE_GET_RIGHT_SIDE;
   private static final XPathExpression XPE_GET_OPERAND;
   private static final XPathExpression XPE_GET_FUN_PARAMETERS;
   private static final XPathExpression XPE_GET_INDEX_LIST;
   private static final XPathExpression XPE_GET_NAMED_ENTITY;
   private static final XPathExpression XPE_GET_PREFIX_NAMED_ENTITY;
   private static final XPathExpression XPE_GET_PREFIX;

   static
   {
      XPE_GET_LEFT_SIDE = XMLManager.compile_or_die("./left");
      XPE_GET_RIGHT_SIDE = XMLManager.compile_or_die("./right");
      XPE_GET_OPERAND = XMLManager.compile_or_die("./operand");

      XPE_GET_FUN_PARAMETERS =
         XMLManager.compile_or_die
         (
            "./parameter_association_chain/el/actual"
         );

      XPE_GET_INDEX_LIST = XMLManager.compile_or_die("./index_list/el");

      XPE_GET_NAMED_ENTITY = XMLManager.compile_or_die("./named_entity");
      XPE_GET_PREFIX_NAMED_ENTITY =
         XMLManager.compile_or_die
         (
            "./prefix/named_entity"
         );

      XPE_GET_PREFIX = XMLManager.compile_or_die("./prefix");
   }

   private static enum Operator
   {
      /* From GHDL's ./src/vhdl/nodes_meta.adb */
      IDENTITY("identity_operator", "+", false), /* assuming it means "+ number" */
      NEGATION("negation_operator", "-", false), /* assuming it means "- number" */
      ABSOLUTE("absolute_operator", "abs", false),

      NOT("not_operator", "not", false),

      CONDITION("condition_operator", "???", true), /* FIXME: what's this? */

      /* Flattens vectors using an operator. */
      REDUCTION_AND("reduction_and_operator", "and", false),
      REDUCTION_OR("reduction_or_operator", "or", false),
      REDUCTION_NAND("reduction_nand_operator", "nand", false),
      REDUCTION_NOR("reduction_nor_operator", "nor", false),
      REDUCTION_XOR("reduction_xor_operator", "xor", false),
      REDUCTION_XNOR("reduction_xnor_operator", "xnor", false),

      AND("and_operator", "and", true),
      OR("or_operator", "or", true),
      NAND("nand_operator", "nand", true),
      NOR("nor_operator", "nor", true),
      XOR("xor_operator", "xor", true),
      XNOR("xnor_operator", "xnor", true),

      EQUALITY("equality_operator", "=", true),
      INEQUALITY("inequality_operator", "/=", true),
      LESS_THAN("less_than_operator", "<", true),
      LESS_THAN_OR_EQUAL("less_than_or_equal_operator", "<=", true),
      GREATER_THAN("greater_than_operator", ">", true),
      GREATER_THAN_OR_EQUAL("greater_than_or_equal_operator", ">=", true),

      /* FIXME: What are those? */
      MATCH_EQUALITY("match_equality_operator", "???", true),
      MATCH_INEQUALITY("match_inequality_operator", "???", true),
      MATCH_LESS_THAN("match_less_than_operator", "???", true),
      MATCH_LESS_THAN_OR_EQUAL
      (
         "match_less_than_or_equal_operator",
         "???",
         true
      ),
      MATCH_GREATER_THAN("match_greater_than_operator", "???", true),
      MATCH_GREATER_THAN_OR_EQUAL
      (
         "match_greater_than_or_equal_operator",
         "???",
         true
      ),

      /* Called using "logical array OP integer", apparently. */
      SLL("sll_operator", "sll", true),
      SLA("sla_operator", "sla", true),
      SRL("srl_operator", "srl", true),
      SRA("sra_operator", "sra", true),
      ROL("rol_operator", "rol", true),
      ROR("ror_operator", "ror", true),

      ADDITION("addition_operator", "+", true),
      SUBSTRACTION("substraction_operator", "-", true),
      CONCATENATION("concatenation_operator", "&", true),
      MULTIPLICATION("multiplication_operator", "*", true),
      DIVISION("division_operator", "/", true),
      MODULUS("modulus_operator", "mod", true),
      REMAINDER("remainder_operator", "rem", true),
      EXPONENTIATION("exponentiation_operator", "**", true);

      /** Static **************************************************************/
      private static final Map<String, Operator> FROM_TAG;

      static
      {
         final Operator operators[];

         FROM_TAG = new HashMap<String, Operator>();

         operators = Operator.class.getEnumConstants(); /* We Java now... */

         for (final Operator op: operators)
         {
            FROM_TAG.put(op.tag, op);
         }
      }

      /** Non-Static **********************************************************/
      private final String tag;
      private final String symbol;
      private final boolean is_binary;

      private Operator
      (
         final String tag,
         final String symbol,
         final boolean is_binary
      )
      {
         this.tag = tag;
         this.symbol = symbol;
         this.is_binary = is_binary;
      }
   }

   public static void process
   (
      final List<IDs> elements,
      final StringBuilder structure,
      final Node current_node
   )
   throws XPathExpressionException
   {
      final String kind;
      final Operator op;

      kind = XMLManager.get_attribute(current_node, "kind");

      op = Operator.FROM_TAG.get(kind);

      if (op == null)
      {
         process_non_operator(elements, structure, current_node, kind);
      }
      else if (op.is_binary)
      {
         structure.append("(?");
         elements.add
         (
            Strings.get_id_from_string
            (
               op.symbol
            )
         );

         process
         (
            elements,
            structure,
            (Node) XPE_GET_LEFT_SIDE.evaluate
            (
               current_node,
               XPathConstants.NODE
            )
         );

         process
         (
            elements,
            structure,
            (Node) XPE_GET_RIGHT_SIDE.evaluate
            (
               current_node,
               XPathConstants.NODE
            )
         );

         structure.append(")"); /* TODO */
      }
      else
      {
         structure.append("(?");
         elements.add
         (
            Strings.get_id_from_string
            (
               op.symbol
            )
         );

         process
         (
            elements,
            structure,
            (Node) XPE_GET_OPERAND.evaluate
            (
               current_node,
               XPathConstants.NODE
            )
         );

         structure.append(")");
      }
   }

   public static void process_non_operator
   (
      final List<IDs> elements,
      final StringBuilder structure,
      final Node current_node,
      final String kind
   )
   throws XPathExpressionException
   {
      if (kind.equals("simple_name"))
      {
         final Node named_entity;

         named_entity =
            (Node) XPE_GET_NAMED_ENTITY.evaluate
            (
               current_node,
               XPathConstants.NODE
            );

         structure.append("?");

         elements.add
         (
            Waveforms.get_associated_waveform_id
            (
               IDs.get_id_from_xml_id
               (
                  XMLManager.get_attribute(named_entity, "ref"),
                  null
               )
            )
         );
      }
      else if (kind.equals("function_call"))
      {
         final Node named_entity;
         final NodeList params;
         final int params_length;

         named_entity =
            (Node) XPE_GET_PREFIX/*_NAMED_ENTITY*/.evaluate
            (
               current_node,
               XPathConstants.NODE
            );

         structure.append("(?");

         /*
          * TODO: Handle functions better, like:
           elements.add
           (
              IDs.get_id_from_xml_id
              (
                 XMLManager.get_attribute(named_entity, "ref"),
                 null
              )
           );
          * But for now, we'll just use the function's name as string:
          */
         elements.add
         (
            Strings.get_id_from_string
            (
               XMLManager.get_attribute(named_entity, "identifier")
            )
         );


         params =
            (NodeList) XPE_GET_FUN_PARAMETERS.evaluate
            (
               current_node,
               XPathConstants.NODESET
            );

         params_length = params.getLength();

         for (int i = 0; i < params_length; ++i)
         {
            process
            (
               elements,
               structure,
               params.item(i)
            );
         }

         structure.append(")");
      }
      else if (kind.equals("indexed_name")) /* vector */
      {
         final Node named_entity;
         final NodeList params;
         final int params_length;

         named_entity =
            (Node) XPE_GET_PREFIX_NAMED_ENTITY.evaluate
            (
               current_node,
               XPathConstants.NODE
            );

         structure.append("(?");

         elements.add
         (
            Waveforms.get_associated_waveform_id
            (
               IDs.get_id_from_xml_id
               (
                  XMLManager.get_attribute(named_entity, "ref"),
                  null
               )
            )
         );

         params =
            (NodeList) XPE_GET_INDEX_LIST.evaluate
            (
               current_node,
               XPathConstants.NODESET
            );

         params_length = params.getLength();

         for (int i = 0; i < params_length; ++i)
         {
            process
            (
               elements,
               structure,
               params.item(i)
            );
         }

         structure.append(")");
      }
      else if (kind.contains("literal"))
      {
         /*
          grep "Kind.*Literal" ./src/vhdl/nodes_meta.adb | sort | uniq -u
          points to:
         "character_literal";
         "enumeration_literal";
         "floating_point_literal";
         "integer_literal";
         "null_literal";
         "overflow_literal";
         "physical_fp_literal";
         "physical_int_literal";
         "physical_literal"; (unsure if it can happen)
         "string_literal8";

         They don't all use the same structure, so we're going to handle them
         latter.
         */

         structure.append("l");
      }
   }
}
