import java.util.*;

public class VHDLProcess
{
   private static final Map<String, VHDLProcess> FROM_ID;

   static
   {
      FROM_ID = new HashMap<String, VHDLProcess>();
   }

   private final List<String> accessed_wfm;
   private final String id;

   private VHDLProcess (final String id)
   {
      this.id = id;

      ports = new ArrayList<String>();
   }
}
