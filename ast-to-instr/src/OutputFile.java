import java.util.ArrayList;
import java.util.Collection;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class OutputFile
{
   private static Collection<OutputFile> ALL_OUTPUT_FILES;

   static
   {
      ALL_OUTPUT_FILES = new ArrayList<OutputFile>();
   }

   public static void close_all ()
   {
      for (final OutputFile f: ALL_OUTPUT_FILES)
      {
         f.close();
      }
   }

   public static OutputFile new_output_file (final String filename)
   {
      final OutputFile result;

      result = new OutputFile(filename);

      ALL_OUTPUT_FILES.add(result);

      return result;
   }

   /** Non-Static *************************************************************/
   private final String filename;
   private final BufferedWriter buffered_writer;

   private OutputFile (final String filename)
   {
      BufferedWriter bf;

      this.filename = filename;

      try
      {
         bf = new BufferedWriter(new FileWriter(new File(filename)));
      }
      catch (final Exception e)
      {
         bf = null;

         System.err.println
         (
            "[F] Could not create new output file \""
            + filename
            + "\":"
         );

         e.printStackTrace();

         System.exit(-1);
      }

      buffered_writer = bf;
   }

   public void write (final String data)
   {
      try
      {
         buffered_writer.write(data);
      }
      catch (final Exception e)
      {
         System.err.println
         (
            "[F] Could not write to output file \""
            + filename
            + "\":"
         );

         e.printStackTrace();

         System.exit(-1);
      }
   }

   public void insert_newline ()
   {
      try
      {
         buffered_writer.newLine();
      }
      catch (final Exception e)
      {
         System.err.println
         (
            "[F] Could not write to output file \""
            + filename
            + "\":"
         );

         e.printStackTrace();

         System.exit(-1);
      }
   }

   private void close ()
   {
      try
      {
         buffered_writer.close();
      }
      catch (final Exception e)
      {
         System.err.println
         (
            "[E] Could not properly close output file \""
            + filename
            + "\":"
         );

         e.printStackTrace();
      }
   }
}
