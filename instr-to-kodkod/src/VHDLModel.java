/* FIXME: Finer imports */
import java.util.*;

import java.io.*;

import kodkod.ast.*;
import kodkod.instance.*;

public class VHDLModel
{
   private final Map<String, VHDLType> types;
   private final Map<String, VHDLPredicate> predicates;

   public VHDLModel ()
   {
      types = new HashMap<String, VHDLType>();
      predicates = new HashMap<String, VHDLPredicate>();
   }

   public void add_type (final String name)
   {
      if (!types.containsKey(name))
      {
         types.put(name, new VHDLType(name));
      }
   }

   public boolean add_predicate (final String name, final String[] signature)
   {
      final VHDLPredicate p;
      final VHDLType[] true_signature;

      true_signature = new VHDLType[signature.length];

      for (int i = 0; i < signature.length; ++i)
      {
         true_signature[i] = types.get(signature[i]);

         if (true_signature[i] == null)
         {
            System.err.println
            (
               "[E] The predicate \""
               + name
               + "\" uses an unknown type \""
               + signature[i]
               + "\""
            );

            return false;
         }
      }

      p = predicates.get(name);

      if (p == null)
      {
         predicates.put(name, new VHDLPredicate(name, true_signature));
      }
      else
      {
         return p.add_signature(true_signature);
      }

      return true;
   }

   public boolean parse_file (final String filename)
   throws FileNotFoundException
   {
      final QuickParser qp;
      String[] input;
      boolean success;

      qp = new QuickParser(filename);

      for (;;)
      {
         try
         {
            input = qp.parse_line();

            if (input == null)
            {
               qp.finalize();

               return false;
            }
            else if (input.length == 0)
            {
               qp.finalize();

               break;
            }
         }
         catch (final IOException e)
         {
            System.err.println
            (
               "[E] IO error while parsing file \""
               + filename
               + "\":"
               /* FIXME: can be null */
               + e.getMessage()
            );

            return false;
         }

         if (input[0].equals("add_element"))
         {
            success = handle_add_element(input);
         }
         else if (input[0].equals("set_function"))
         {
            success = handle_set_function(input);
         }
         else
         {
            success = handle_predicate(input);
         }

         if (!success)
         {
            System.err.println
            (
               "[E] An erroneous instruction was found in file \""
               + filename
               + "\"."
            );

            try
            {
               qp.finalize();
            }
            catch (final Exception e)
            {
               System.err.println("[E] Additionally:");
               e.printStackTrace();
            }

            return false;
         }
      }

      return true;
   }

   private boolean handle_add_element (final String... cmd)
   {
      final VHDLType t;

      if (cmd.length != 3)
      {
         System.err.println
         (
            "[E] Badly formed \"add_element\" instruction: \""
            + String.join(" ", cmd)
            + "\"."
         );

         return false;
      }

      t = types.get(cmd[1]);

      if (t == null)
      {
         System.err.println
         (
            "[E] Instruction to add element to unknown type \""
            + cmd[1]
            + "\": \""
            + String.join(" ", cmd)
            + "\"."
         );

         return false;
      }

      t.add_member(cmd[2]);

      return true;
   }

   private boolean handle_set_function (final String... cmd)
   {
      if (cmd.length != 4)
      {
         System.err.println
         (
            "[E] Badly formed \"set_function\" instruction: \""
            + String.join(" ", cmd)
            + "\"."
         );

         return false;
      }

      /*
      System.err.println
      (
         "[W] \"set_function\" instructions are ignored."
      );
      */

      return true;
   }

   private boolean handle_predicate (final String... cmd)
   {
      final VHDLPredicate p;
      final String[] params;

      params = new String[cmd.length - 1];

      p = predicates.get(cmd[0]);

      if (p == null)
      {
         System.err.println
         (
            "[E] Instruction with an unknown predicate (\""
            + cmd[0]
            + "\"): \""
            + String.join(" ", cmd)
            + "\"."
         );

         return false;
      }

      if (params.length != p.get_arity())
      {
         System.err.println
         (
            "[E] Predicate \""
            + cmd[0]
            + "\" is of arity "
            + p.get_arity()
            + ", making the instruction: \""
            + String.join(" ", cmd)
            + "\" invalid."
         );

         return false;
      }

      for (int i = 0; i < params.length; ++i)
      {
         /* TODO: check if the IDs are registered in the corresponding type. */
         params[i] = cmd[i + 1];

         if (!p.accepts_as_nth_param(i, params[i]))
         {
            System.err.println
            (
               "[E] The predicate \""
               + p.get_name()
               + "\" has no signature allowing the element \""
               + params[i]
               + "\" as "
               + i
               + "th parameter, making the instruction: \""
               + String.join(" ", cmd)
               + "\" invalid."
            );

            return false;
         }
      }

      p.add_member(params);

      return true;
   }

   public Collection<String> get_atoms ()
   {
      final Collection<String> result;

      result = new ArrayList<String>();

      for (final VHDLType t: types.values())
      {
         result.addAll(t.get_all_members_as_atoms());
      }

      return result;
   }

   public void add_to_bounds (final Bounds b, final TupleFactory f)
   {
      for (final VHDLType t: types.values())
      {
         t.add_to_bounds(b, f);
      }

      for (final VHDLPredicate p: predicates.values())
      {
         p.add_to_bounds(b, f);
      }
   }

   public Relation get_predicate_as_relation (final String name)
   {
      final VHDLPredicate p;

      p = predicates.get(name);

      if (p == null)
      {
         return null;
      }
      else
      {
         return p.get_as_relation();
      }
   }

   public Relation get_type_as_relation (final String name)
   {
      final VHDLType t;

      t = types.get(name);

      if (t == null)
      {
         return null;
      }
      else
      {
         return t.get_as_relation();
      }
   }
}
