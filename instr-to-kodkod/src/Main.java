/* FIXME: Finer imports */
import kodkod.ast.*;

import kodkod.engine.*;
import kodkod.engine.config.*;
import kodkod.engine.satlab.*;

import kodkod.instance.*;

import java.io.IOException;

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

   private static boolean load_levels ()
   {
      for (final String lvl: PARAMETERS.get_level_files())
      {
         try
         {
            VHDLLevel.add_to_model(MODEL, lvl);
         }
         catch (final Exception e)
         {
            System.err.println
            (
               "[E] Could not load level file \""
               + lvl
               + "\":"
            );

            e.printStackTrace();

            return false;
         }
      }

      return true;
   }

   private static Formula load_property ()
   {
      final VHDLProperty pro;

      pro = new VHDLProperty(PARAMETERS.get_property_file());

      try
      {
         return pro.generate_formula();
      }
      catch (final IOException e)
      {
         System.err.println
         (
            "[E] Could not load property file \""
            + PARAMETERS.get_property_file()
            + "\":"
         );
         e.printStackTrace();

         return null;
      }
   }

   private static boolean load_models ()
   {
      for (final String mod: PARAMETERS.get_model_files())
      {
         try
         {
            MODEL.parse_file(mod);
         }
         catch (final Exception e)
         {
            System.err.println
            (
               "[E] Could not load instructions from file \""
               + mod
               + "\":"
            );

            e.printStackTrace();

            return false;
         }
      }

      return true;
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
      if (!load_levels())
      {
         return;
      }

      /* 2/ Load Properties (will change 'is_used()' on predicates) */
      /* FIXME? Currently only one property, due to the 'is_used' */
      property = load_property();

      if (property == null)
      {
         return;
      }

      /* 3/ Generate complementary model according to used predicates. */
      /* TODO */

      /* 4/ Load Model, but only for used predicates and types. */
      if (!load_models())
      {
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
