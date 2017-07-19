/* FIXME: Finer imports */
import java.util.*;
import java.io.*;

import kodkod.ast.*;

import org.antlr.v4.runtime.*;

public class VHDLProperty
{
   private final String filename;

   public VHDLProperty (final String filename)
   {
      this.filename = filename;
   }

   public Formula generate_formula ()
   throws IOException
   {
      final PropertyLexer lexer;
      final CommonTokenStream tokens;
      final PropertyParser parser;

      lexer = new PropertyLexer(CharStreams.fromFileName(filename));
      tokens = new CommonTokenStream(lexer);
      parser = new PropertyParser(tokens);

      return parser.tag_existing().result;
   }
}
