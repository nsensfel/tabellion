/* FIXME: Finer imports */
import java.util.*;

import kodkod.ast.*;
import kodkod.ast.operator.*;

import kodkod.instance.*;


public class VHDLType
{
   private final Map<String, Relation> members;
   private final String name;
   private final Relation as_relation;

   public VHDLType (final String name)
   {
      members = new HashMap<String, Relation>();

      this.name = name;
      as_relation = Relation.unary(name);
   }

   public void add_member (final String id)
   {
      members.put(id, Relation.unary(id));
   }

   public String get_name ()
   {
      return name;
   }

   public Relation get_as_relation ()
   {
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

      for (final Map.Entry<String, Relation> member: members_as_set)
      {
         b.boundExactly(member.getValue(), f.setOf(member.getKey()));
      }

      /*
       * the toArray() is required to avoid the collection being considered as
       * a single atom.
       */
      b.boundExactly(as_relation, f.setOf(get_all_members_as_atoms().toArray()));
   }
}
