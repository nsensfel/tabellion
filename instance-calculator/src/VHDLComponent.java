import java.util.*;

public class VHDLComponent
{
   private static final Map<String, VHDLComponent> FROM_ID;

   static
   {
      FROM_ID = new HashMap<String, VHDLComponent>();
   }

   private final Map<String, String> port_map;
   private final String id;

   private VHDLComponent (final String id)
   {
      this.id = id;

      port_map = new HashMap<String, String>();
   }
}
