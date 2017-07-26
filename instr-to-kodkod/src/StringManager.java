import kodkod.ast.Relation;

import java.util.Map;
import java.util.HashMap;

public class StringManager
{
   private final Map<String, String> TO_ID;
   private final VHDLType string_type;
   private final String anon_string_prefix;
   private int anon_string_count;

   private static String cleanup_string (final String str)
   {
      return str.replaceAll("\\s","").toLowerCase();
   }

   public StringManager ()
   {
      TO_ID = new HashMap<String, String>();
      string_type = Main.get_model().get_string_type();
      anon_string_prefix = "_string_"; /* TODO: use a program param. */
      anon_string_count = 0;
   }


   public Relation get_string_as_relation
   (
      final String str
   )
   {
      String id;

      id = TO_ID.get(cleanup_string(str));

      if (id == null)
      {
         id = (anon_string_prefix + anon_string_count);

         string_type.add_member(id);

         TO_ID.put(str, id);
      }
      else
      {
         System.out.println
         (
            "[D] Using string \""
            + str
            + "\" (id: "
            + id
            + ")"
         );
      }

      return string_type.get_member_as_relation(id);
   }

   private void add_mapping (String str, final String id)
   {
      str = cleanup_string(str);

      if (!TO_ID.containsKey(str))
      {
         TO_ID.put(str, id);
         string_type.add_member(id);
      }
   }

   public boolean handle_mapping_instruction (final String... instr)
   {
      if (instr.length < 3)
      {
         return false;
      }

      if (!instr[0].equals("string->instr"))
      {
         return false;
      }

      add_mapping(instr[1], instr[2]);

      return true;
   }
}
