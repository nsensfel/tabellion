import java.util.Map;
import java.util.HashMap;

public class Waveforms
{
   private static final Map<String, String> FROM_WAVEFORM;
   private static final Map<String, String> TO_WAVEFORM;

   static
   {
      FROM_WAVEFORM = new HashMap<String, String>();
      TO_WAVEFORM = new HashMap<String, String>();
   }

   private Waveforms () {} /* Utility class. */

   public static void register_map (final String wfm_id, final String elem_id)
   {
      FROM_WAVEFORM.put(wfm_id, elem_id);
      TO_WAVEFORM.put(elem_id, wfm_id);
   }

   public static String get_id_from_waveform_id (final String wfm_id)
   {
      final String result;

      result = FROM_WAVEFORM.get(wfm_id);

      if (result == null)
      {
         System.err.println
         (
            "[F] There is no element associated with waveform \""
            + wfm_id
            + "\". Is the model complete?"
         );

         System.exit(-1);
      }

      return result;
   }

   public static String get_waveform_id_from_id (final String src_id)
   {
      final String result;

      result = TO_WAVEFORM.get(src_id);

      if (result == null)
      {
         System.err.println
         (
            "[F] There is no waveform associated with the element \""
            + src_id
            + "\". Is the model complete?"
         );

         System.exit(-1);
      }

      return result;
   }

   public static String find_id_from_waveform_id (final String wfm_id)
   {
      return FROM_WAVEFORM.get(wfm_id);
   }

   public static String find_waveform_id_from_id (final String src_id)
   {
      return TO_WAVEFORM.get(src_id);
   }
}
