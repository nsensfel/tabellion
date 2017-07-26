/* FIXME: Finer imports */
import java.util.*;

import kodkod.ast.*;
import kodkod.ast.operator.*;

import kodkod.instance.*;


public class VHDLPredicate
{
   private final Collection<VHDLType[]> signatures;
   private final Collection<String[]> members;
   private final String name;
   private final int arity;
   private final Relation as_relation;
   private final boolean is_function;
   private boolean is_used;

   public VHDLPredicate
   (
      final String name,
      final VHDLType[] signature,
      final boolean is_function
   )
   {
      this.name = name;
      this.is_function = is_function;
      arity = signature.length;

      signatures = new ArrayList<VHDLType[]>();
      members = new ArrayList<String[]>();
      is_used = false;

      as_relation = Relation.nary(name, arity);

      signatures.add(signature);
   }

   public void add_member (final String[] tuple)
   {
      members.add(tuple);
   }

   public boolean is_used ()
   {
      return is_used;
   }

   public int get_arity ()
   {
      return arity;
   }

   public boolean is_function ()
   {
      return is_function;
   }

   public Relation get_as_relation ()
   {
      if (!is_used)
      {
         for (final VHDLType[] sig: signatures)
         {
            for (final VHDLType t: sig)
            {
               t.flag_as_used();
            }
         }

         is_used = true;

         System.out.println("Enabling predicate: " + name);
      }

      return as_relation;
   }

   /* pre: i < get_arity() */
   public boolean accepts_as_nth_param (final int i, final String id)
   {
      for (final VHDLType[] sig: signatures)
      {
         if (sig[i].get_member_as_relation(id) != null)
         {
            return true;
         }
      }

      return false;
   }

   public boolean add_signature (final VHDLType[] signature)
   {
      if (signature.length != get_arity())
      {
         return false;
      }

      signatures.add(signature);

      return true;
   }

   public String get_name ()
   {
      return name;
   }

   public void add_to_bounds (final Bounds b, final TupleFactory f)
   {
      final TupleSet as_tuples;

      /* Empty tuple set, will contain tuples of the right arity. */
      as_tuples = f.noneOf(get_arity());

      /* For every member of this predicate ... */
      for (final Object[] ref: members)
      {
         /* add a new tuple to the tuple set representing the predicate. */
         as_tuples.add(f.tuple(ref));
      }

      /* We now have the exact definition of the predicate as a relation */
      b.boundExactly(as_relation, as_tuples);
   }
}
