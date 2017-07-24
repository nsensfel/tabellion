import java.util.Map;
import java.util.HashMap;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

public class Expressions
{
   private static final XPathExpression XPE_GET_LEFT, XPE_GET_RIGHT;

   static
   {
      XPE_GET_LEFT = XMLManager.compile_or_die("./left");
      XPE_GET_RIGHT = XMLManager.compile_or_die("./right");
   }

   private static enum Operator
   {
      /* From GHDL's ./src/vhdl/nodes_meta.adb */
      IDENTITY("identity_operator", false), /* assuming it means "+ number" */
      NEGATION("negation_operator", false), /* assuming it means "- number" */
      ABSOLUTE("absolute_operator", false),

      NOT("not_operator", false),

      CONDITION("condition_operator", true), /* FIXME: what's this? */

      /* Flattens vectors using an operator. */
      REDUCTION_AND("reduction_and_operator", false),
      REDUCTION_OR("reduction_or_operator", false),
      REDUCTION_NAND("reduction_nand_operator", false),
      REDUCTION_NOR("reduction_nor_operator", false),
      REDUCTION_XOR("reduction_xor_operator", false),
      REDUCTION_XNOR("reduction_xnor_operator", false),

      AND("and_operator", true),
      OR("or_operator", true),
      NAND("nand_operator", true),
      NOR("nor_operator", true),
      XOR("xor_operator", true),
      XNOR("xnor_operator", true),

      EQUALITY("equality_operator", true),
      INEQUALITY("inequality_operator", true),
      LESS_THAN("less_than_operator", true),
      LESS_THAN_OR_EQUAL("less_than_or_equal_operator", true),
      GREATER_THAN("greater_than_operator", true),
      GREATER_THAN_OR_EQUAL("greater_than_or_equal_operator", true),

      /* FIXME: What are those? */
      MATCH_EQUALITY("match_equality_operator", true),
      MATCH_INEQUALITY("match_inequality_operator", true),
      MATCH_LESS_THAN("match_less_than_operator", true),
      MATCH_LESS_THAN_OR_EQUAL("match_less_than_or_equal_operator", true),
      MATCH_GREATER_THAN("match_greater_than_operator", true),
      MATCH_GREATER_THAN_OR_EQUAL("match_greater_than_or_equal_operator", true),

      /* Called using "logical array OP integer", apparently. */
      SLL("sll_operator", true),
      SLA("sla_operator", true),
      SRL("srl_operator", true),
      SRA("sra_operator", true),
      ROL("rol_operator", true),
      ROR("ror_operator", true),

      ADDITION("addition_operator", true),
      SUBSTRACTION("substraction_operator", true),
      CONCATENATION("concatenation_operator", true),
      MULTIPLICATION("multiplication_operator", true),
      DIVISION("division_operator", true),
      MODULUS("modulus_operator", true),
      REMAINDER("remainder_operator", true),
      EXPONENTIATION("exponentiation_operator", true);

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
      private final boolean is_binary;

      private Operator
      (
         final String tag,
         final boolean is_binary
      )
      {
         this.tag = tag;
         this.is_binary = is_binary;
      }
   }
   /***************************************************************************/
}
