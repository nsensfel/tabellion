public class Main
{
   private static Parameters PARAMETERS;

   public static void main (final String... args)
   {
      PARAMETERS = new Parameters(args);

      if (!PARAMETERS.are_valid())
      {
         return;
      }
   }
}
