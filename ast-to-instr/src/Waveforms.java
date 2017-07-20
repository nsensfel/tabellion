import java.util.Map;
import java.util.HashMap;

public class Waveforms
{
   private static final Map<IDs, IDs> TO_WAVEFORM;

   static
   {
      TO_WAVEFORM = new HashMap<IDs, IDs>();
   }

   private Waveforms () {} /* Utility class. */

   public static IDs get_associated_waveform_id (final IDs source)
   {
      IDs result;

      result = TO_WAVEFORM.get(source);

      if (result == null)
      {
         result = IDs.generate_new_id("waveform");

         TO_WAVEFORM.put(source, result);

         /* TODO: remove, it's for debug. */
         System.out.println
         (
            "[WFM] ("
            + source.get_value()
            + "->"
            + result.get_value()
            + ")"
         );
      }

      return result;
   }
}
