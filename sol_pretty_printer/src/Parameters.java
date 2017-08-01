import java.util.List;
import java.util.ArrayList;

public class Parameters
{
   private final List<String> sol_files;
   private final List<String> pp_files;
   private final List<String> model_files;

   private final boolean are_valid;

   public static void print_usage ()
   {
      System.out.println
      (
         "Sol-Pretty-Printer\n"
         + "USAGE:\n"
         + "\tjava Main <MODEL_FILE>+ <SOL_AND_PP_FILES>+\n"
         + "PARAMETERS:\n"
         + "\t- <MODEL_FILE>\tInstr. model files.\n"
         + "\t- <SOL_AND_PP_FILES>\tOne solution file, followed by one pretty"
         + " print file."
         + "NOTES:\n"
         + "\t- Model files have a \".mod\" extension.\n"
         + "\t- Solution files have a \".sol\" extension.\n"
         + "\t- Pretty-print files have a \".pp\" extension.\n"
         + "\t- Solution files may contain any number of solutions.\n"
      );
   }

   public Parameters (final String... args)
   {
      boolean has_error, prev_was_a_sol;

      model_files = new ArrayList<String>();
      sol_files = new ArrayList<String>();
      pp_files = new ArrayList<String>();

      if (args.length < 2)
      {
         print_usage();

         are_valid = false;

         return;
      }

      has_error = false;
      prev_was_a_sol = false;

      for (int i = 1; i < args.length; ++i)
      {
         if (args[i].endsWith(".mod"))
         {
            model_files.add(args[i]);
         }
         else if (args[i].endsWith(".sol"))
         {
            sol_files.add(args[i]);

            if (prev_was_a_sol)
            {
               System.err.println
               (
                  "[F] Two solution files followed one another. You must give"
                  + "<SOLUTION_FILE> <PRETTY_PRINT_FILE> pairs as parameters."
               );

               System.exit(-1);
            }

            prev_was_a_sol = true;
         }
         else if (args[i].endsWith(".pp"))
         {
            if (!prev_was_a_sol)
            {
               System.err.println
               (
                  "[F] Two pretty print files followed one another. You must"
                  + " give <SOLUTION_FILE> <PRETTY_PRINT_FILE> pairs as"
                  + " parameters."
               );

               System.exit(-1);
            }

            prev_was_a_sol = false;
         }
         else
         {
            System.err.println
            (
               "[E] Unknown file type \""
               + args[i]
               + "\"."
            );

            has_error = true;
         }
      }

      are_valid = !has_error;
   }

   public List<String> get_model_files ()
   {
      return model_files;
   }

   public List<String> get_solution_files ()
   {
      return sol_files;
   }

   public List<String> get_pretty_print_files ()
   {
      return pp_files;
   }

   public boolean are_valid ()
   {
      return are_valid;
   }
}
