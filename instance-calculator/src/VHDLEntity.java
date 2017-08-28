import java.util.*;

public class VHDLEntity
{
   private static final Map<String, VHDLEntity> FROM_ID;

   static
   {
      FROM_ID = new HashMap<String, VHDLEntity>();
   }

   private final List<String> ports;
   private final String id;

   private VHDLEntity (final String id)
   {
      this.id = id;

      ports = new ArrayList<String>();
   }
}
