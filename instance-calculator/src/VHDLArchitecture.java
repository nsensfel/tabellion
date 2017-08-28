import java.util.*;

public class VHDLArchitecture
{
   private static final Map<String, VHDLArchitecture> FROM_ID;

   static
   {
      FROM_ID = new HashMap<String, VHDLArchitecture>();
   }

   private final List<String> processes;
   private final List<String> components;
   private final String id;

   private VHDLArchitecture (final String id)
   {
      this.id = id;

      ports = new ArrayList<String>();
   }
}
