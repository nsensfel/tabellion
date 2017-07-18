/* FIXME: Finer imports */
import kodkod.ast.*;

import kodkod.engine.*;
import kodkod.engine.config.*;
import kodkod.engine.satlab.*;

import kodkod.instance.*;
public class Main
{
   private static Parameters PARAMETERS;
   private static VHDLModel MODEL;
   private static VariableManager VARIABLE_MANAGER;

   public static VHDLModel get_model ()
   {
      return MODEL;
   }

   public static VariableManager get_variable_manager ()
   {
      return VARIABLE_MANAGER;
   }

   private static Formula get_formula (final VHDLModel model)
   {
      final Variable w;

      w = Variable.unary("w");

      return
         w.join
         (
            model.get_predicate_as_relation("is_accessed_by")
         ).no().forSome(w.oneOf(model.get_type_as_relation("waveform")));
   }

   public static void main (final String... args)
   {
      /*
       * Order of operations:
       * 1/ Load Levels (Types + predicates)
       * 2/ Load Properties (will change 'is_used()' on predicates)
       * 3/ Generate complementary model according to used predicates.
       * 4/ Load Model, but only for used predicates and types.
       * 5/ Add all used types and predicates to the Universe.
       */
      final Universe univ;
      final TupleFactory tf;
      final Bounds bounds;

      final Solver solver;
      final Solution sol;
      final Formula property;

      PARAMETERS = new Parameters(args);

      if (!PARAMETERS.are_valid())
      {
         return;
      }

      VARIABLE_MANAGER = new VariableManager(PARAMETERS.get_variables_prefix());

      MODEL = new VHDLModel();

      /* 1/ Load Levels (Types + predicates) */
      try
      {
         VHDLLevel.add_to_model
         (
            MODEL,
            (
               PARAMETERS.get_levels_directory()
               + "/structural_level.data"
            )
         );
      }
      catch (final Exception e)
      {
         System.err.println("[E] Could not load structural level:");
         e.printStackTrace();

         return;
      }

       /* 2/ Load Properties (will change 'is_used()' on predicates) */
       property = get_formula(MODEL);
       /* TODO */

       /* 3/ Generate complementary model according to used predicates. */
       /* TODO */

      /* 4/ Load Model, but only for used predicates and types. */
      try
      {
         MODEL.parse_file(PARAMETERS.get_model_file());
      }
      catch (final Exception e)
      {
         System.err.println("[E] Could not load instructions:");
         e.printStackTrace();

         return;
      }

      /* 5/ Add all types and used predicates to the Universe. */
      univ = new Universe(MODEL.get_atoms());
      tf = univ.factory();
      bounds = new Bounds(univ);

      MODEL.add_to_bounds(bounds, tf);

      solver = new Solver();
      solver.options().setSolver(SATFactory.DefaultSAT4J);
      solver.options().setReporter(new ConsoleReporter());

      sol = solver.solve(property, bounds);

      System.out.println(sol);
   }
}
