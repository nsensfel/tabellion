import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Iterator;

import kodkod.ast.Formula;

import kodkod.engine.Solution;
import kodkod.engine.Solver;
import kodkod.engine.config.ConsoleReporter;
import kodkod.engine.satlab.SATFactory;

import kodkod.instance.Bounds;
import kodkod.instance.TupleFactory;
import kodkod.instance.Universe;

public class Main
{
   private static Parameters PARAMETERS;
   private static VHDLModel MODEL;
   private static VariableManager VARIABLE_MANAGER;
   private static StringManager STRING_MANAGER;

   public static Parameters get_parameters ()
   {
      return PARAMETERS;
   }

   public static VHDLModel get_model ()
   {
      return MODEL;
   }

   public static VariableManager get_variable_manager ()
   {
      return VARIABLE_MANAGER;
   }

   public static StringManager get_string_manager ()
   {
      return STRING_MANAGER;
   }

   private static boolean load_levels ()
   {
      for (final String lvl: PARAMETERS.get_level_files())
      {
         try
         {
            if (PARAMETERS.be_verbose())
            {
               System.out.println("Loading level file \"" + lvl + "\"...");
            }

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
         if (PARAMETERS.be_verbose())
         {
            System.out.println
            (
               "Loading property file \""
               + PARAMETERS.get_property_file()
               + "\"..."
            );
         }

         return pro.generate_base_formula();
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
            if (PARAMETERS.be_verbose())
            {
               System.out.println("Loading model file \"" + mod + "\"...");
            }

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

   private static boolean load_mapping_file (final String filename)
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

         if
         (
            (!STRING_MANAGER.handle_mapping_instruction(input))
            /* && (!_____.handle_mapping_instruction(input)) */
            /* Yeah, we don't handle those */
            && (!input[0].equals("xml->instr"))
         )
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

   private static boolean load_mappings ()
   {
      try
      {
         for (final String file: PARAMETERS.get_mapping_files())
         {
            if (!load_mapping_file(file))
            {
               return false;
            }
         }
      }
      catch (final Exception e)
      {
         System.err.println("[F] Could not load mappings:");
         e.printStackTrace();

         System.exit(-1);
      }

      return true;
   }

   private static BufferedWriter get_output_file (final String filename)
   {
      try
      {
         return new BufferedWriter(new FileWriter(new File(filename)));
      }
      catch (final Exception e)
      {
         System.err.println
         (
            "[F] Could not create/open output file \""
            + filename
            + "\":"
         );

         e.printStackTrace();

         System.exit(-1);
      }

      return null;
   }

   public static void main (final String... args)
   {
      /*
       * Order of operations:
       * 1/ Load Levels (Types + predicates)
       * 2/ Load Mappings (string handling, some already have IDs).
       * 3/ Load Property (will change 'is_used()' on predicates).
       * 4/ Generate model according to the used predicates.
       * 5/ Load Model, but only for used predicates and types.
       * 6/ Add all used types and predicates to the Universe.
       * 7/ Solve regular expressions.
       * 8/ Add constraints linked to tagged variables.
       */
      final Universe univ;
      final TupleFactory tf;
      final Bounds bounds;
      final Iterator<Solution> solutions;
      final Solver solver;
      final Formula property;
      final BufferedWriter solution_output;

      PARAMETERS = new Parameters(args);

      if (!PARAMETERS.are_valid())
      {
         return;
      }

      solution_output = get_output_file(PARAMETERS.get_output_file());

      if (solution_output == null)
      {
         return;
      }

      VARIABLE_MANAGER = new VariableManager();

      MODEL = new VHDLModel();

      /* 1/ Load Levels (Types + predicates) */
      if (!load_levels())
      {
         return;
      }

      /* 2/ Load Mappings (to allow references in the property). */
      STRING_MANAGER = new StringManager();

      if (!load_mappings())
      {
         return;
      }

      /* 3/ Load Properties (will change 'is_used()' on predicates) */
      property = load_property();

      if (property == null)
      {
         return;
      }
      else if (PARAMETERS.be_verbose())
      {
         System.out.println
         (
            "Solving property:\n"
            + property.toString()
         );
      }

      /* 4/ Generate model according to used predicates. */
      /* Done implicitly by step 5.

      /* 5/ Load Model, but only for used predicates and types. */
      if (!load_models())
      {
         return;
      }

      /* 6/ Handle regexps */
      STRING_MANAGER.populate_regex_predicate
      (
         MODEL.get_predicate("string_matches")
      );


      /* 7/ Add all types and used predicates to the Universe. */
      univ = new Universe(MODEL.get_atoms());
      tf = univ.factory();
      bounds = new Bounds(univ);

      MODEL.add_to_bounds(bounds, tf);
      VARIABLE_MANAGER.add_tagged_variables_to_bounds(bounds, tf);

      solver = new Solver();
      solver.options().setSkolemDepth(-1);
      solver.options().setSymmetryBreaking(0);
      solver.options().setSolver(SATFactory.DefaultSAT4J);

      if (PARAMETERS.be_verbose())
      {
         System.out.println(bounds);
         System.out.println
         (
            "Final formula:"
            +
            property.and
            (
               VARIABLE_MANAGER.generate_tagged_variable_constraints()
            ).toString()
         );
         solver.options().setReporter(new ConsoleReporter());
      }

      solutions =
         solver.solveAll
         (
            property.and
            (
               VARIABLE_MANAGER.generate_tagged_variable_constraints()
            ),
            bounds
         );

      while (solutions.hasNext())
      {
         final Solution sol;

         sol = solutions.next();

         if (sol.sat())
         {
            try
            {
               VARIABLE_MANAGER.print_solution
               (
                  sol.instance().relationTuples(),
                  solution_output
               );
            }
            catch (final IOException e)
            {
               System.err.println
               (
                  "[F] Unable to write solution to file \""
                  + PARAMETERS.get_output_file()
                  + "\":"
               );

               e.printStackTrace();

               System.exit(-1);
            }
         }
      }

      try
      {
         solution_output.close();
      }
      catch (final IOException e)
      {
         System.err.println
         (
            "[F] Unable to properly close solution file \""
            + PARAMETERS.get_output_file()
            + "\":"
         );

         e.printStackTrace();

         System.exit(-1);
      }
   }
}
