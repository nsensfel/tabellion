import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import kodkod.ast.Relation;
import kodkod.ast.Formula;

import kodkod.instance.Bounds;
import kodkod.instance.TupleFactory;


public class VHDLType
{
   private final Map<String, Relation> members;
   private final String name;
   private final Relation as_relation;
   private boolean is_used;

   public VHDLType (final String name)
   {
      members = new HashMap<String, Relation>();
      is_used = false;

      this.name = name;
      as_relation = Relation.unary(name);
   }

   public void add_member (final String id)
   {
      if (!members.containsKey(id))
      {
         members.put(id, Relation.unary(id));
      }
   }

   public String get_name ()
   {
      return name;
   }

   public void flag_as_used ()
   {
      if (!is_used)
      {
         if (Main.get_parameters().be_verbose())
         {
            System.out.println("Enabling type: " + name);
         }

         is_used = true;
      }
   }

   public boolean is_used ()
   {
      return is_used;
   }

   public Relation get_as_relation ()
   {
      is_used = true;

      return as_relation;
   }

   public Relation get_member_as_relation (final String id)
   {
      return members.get(id);
   }

   public Collection<String> get_all_members_as_atoms ()
   {
      return members.keySet();
   }

   public Formula generate_declarations ()
   {
      Formula result;

      result = Formula.TRUE;

      return result;
   }

   public void add_to_bounds (final Bounds b, final TupleFactory f)
   {
      final Set<Map.Entry<String, Relation>> members_as_set;

      members_as_set = members.entrySet();

      if (name.toLowerCase().equals("string"))
      {
         /* Other types do not require direct access to a member. */
         /* TODO: only bound the strings that are actually mentionned in the
          * formula.
          */
         for (final Map.Entry<String, Relation> member: members_as_set)
         {
            b.boundExactly(member.getValue(), f.setOf(member.getKey()));
         }
      }
      /*
       * the toArray() is required to avoid the collection being considered as
       * a single atom.
       */
      b.boundExactly(as_relation, f.setOf(get_all_members_as_atoms().toArray()));
   }
}
