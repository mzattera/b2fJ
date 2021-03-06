package js.tinyvm.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Writes to C code file.
 * 
 * Writes lo-byte first, hi-byte last. In longs, hi-word goes first, but each
 * word is written as though with writeU4.
 * 
 * @author maxi
 */
public class LECCodeWriter extends CCodeWriter
{
   public LECCodeWriter (OutputStream stream)
   {
      super(stream);
   }

   //
   // specific write order
   //

   public void writeU2 (int aShort) throws IOException
   {
      write((aShort >>> 0) & 0xFF);
      write((aShort >>> 8) & 0xFF);
   }

   public void writeU4 (int aInt) throws IOException
   {
      write((aInt >>> 0) & 0xFF);
      write((aInt >>> 8) & 0xFF);
      write((aInt >>> 16) & 0xFF);
      write((aInt >>> 24) & 0xFF);
   }

   public void writeU8 (long aLong) throws IOException
   {
      write((int) ((aLong >>> 32) & 0xFF));
      write((int) ((aLong >>> 40) & 0xFF));
      write((int) ((aLong >>> 48) & 0xFF));
      write((int) ((aLong >>> 56) & 0xFF));

      write((int) ((aLong >>> 0) & 0xFF));
      write((int) ((aLong >>> 8) & 0xFF));
      write((int) ((aLong >>> 16) & 0xFF));
      write((int) ((aLong >>> 24) & 0xFF));
   }
}
