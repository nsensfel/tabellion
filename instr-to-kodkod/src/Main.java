/* FIXME: Finer imports */
import kodkod.ast.*;

import kodkod.engine.*;
import kodkod.engine.config.*;
import kodkod.engine.satlab.*;

import kodkod.instance.*;
public class Main
{
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
      final VHDLModel model;

      final Universe univ;
      final TupleFactory tf;
      final Bounds bounds;

      final Solver solver;
      final Solution sol;

      if (args.length != 1)
      {
         System.out.println("Use: java Main <instructions_file>");

         return;
      }

      model = new VHDLModel();

      try
      {
         VHDLLevel.add_to_model(model, "./structural_level.data");
      }
      catch (final Exception e)
      {
         System.err.println("[E] Could not load structural level:");
         e.printStackTrace();

         return;
      }

      try
      {
         model.parse_file(args[0]);
      }
      catch (final Exception e)
      {
         System.err.println("[E] Could not load instructions:");
         e.printStackTrace();

         return;
      }

      univ = new Universe(model.get_atoms());
      tf = univ.factory();
      bounds = new Bounds(univ);

      model.add_to_bounds(bounds, tf);

      solver = new Solver();
      solver.options().setSolver(SATFactory.DefaultSAT4J);
      solver.options().setReporter(new ConsoleReporter());

      sol = solver.solve(get_formula(model), bounds);

      System.out.println(sol);
   }
}
