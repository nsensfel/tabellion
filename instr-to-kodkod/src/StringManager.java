import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import java.util.regex.Pattern;

import kodkod.ast.Relation;

public class StringManager
{
   private final Map<String, String> TO_ID;
   private final Collection<Pattern> regexes;
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
      regexes = new ArrayList<Pattern>();

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

      return string_type.get_member_as_relation(id);
   }

   public Relation get_regex_as_relation
   (
      final String str
   )
   {
      regexes.add(Pattern.compile(str.substring(1, (str.length() - 1))));

      return get_string_as_relation(str);
   }

   public void populate_regex_predicate (final VHDLPredicate rp)
   {
      final Set<Map.Entry<String, String>> candidates;

      candidates = TO_ID.entrySet();

      for (final Pattern p: regexes)
      {
         for (final Map.Entry<String, String> c: candidates)
         {
            String word;

            word = c.getKey();
            /* Remove the surounding "" */
            word = word.substring(1, (word.length() - 1));

            if (p.matcher(word).matches())
            {
               System.out.println
               (
                  "[D] \""
                  + word
                  + "\" MATCHES pattern \""
                  + p.pattern()
                  + "\"."
               );

               rp.add_member
               (
                  new String[]
                  {
                     c.getValue(),
                     TO_ID.get("\"" + p.pattern() + "\"")
                  }
               );
            }
            /*
            else
            {
               System.out.println
               (
                  "[D] \""
                  + word
                  + "\" DOES NOT match pattern \""
                  + p.pattern()
                  + "\"."
               );
            }
            */
         }
      }
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
