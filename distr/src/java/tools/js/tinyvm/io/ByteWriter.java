package js.tinyvm.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Basic byte writer implementation (write order independant part).
 * 
 * This writes bytecode out to a binary file.
 * It is the original implementation from TinyVM; in b2fJ this is 
 * replaces by CCodeWriter that generates C code directly.
 * 
 * Left here together with BEByteWriter & LEByteWriter in case it is needed again.
 */
public abstract class ByteWriter implements IByteWriter
{
   /**
    * Data output stream used for writing.
    */
   private DataOutputStream _out;

   /**
    * Constructor.
    * 
    * @param stream stream to write to
    */
   public ByteWriter (OutputStream stream)
   {
      _out = new DataOutputStream(stream);
   }

   //
   // java write order
   //

   public void writeBoolean (boolean aBoolean) throws IOException
   {
      _out.writeBoolean(aBoolean);
   }

   public void writeByte (int aByte) throws IOException
   {
      _out.writeByte(aByte);
   }

   public void writeChar (int aChar) throws IOException
   {
      _out.writeChar(aChar);
   }

   public void writeShort (int aShort) throws IOException
   {
      _out.writeShort(aShort);
   }

   public void writeInt (int aInt) throws IOException
   {
      _out.writeInt(aInt);
   }

   public void writeLong (long aLong) throws IOException
   {
      _out.writeLong(aLong);
   }

   public void writeFloat (float aFloat) throws IOException
   {
      _out.writeFloat(aFloat);
   }

   public void writeDouble (double aDouble) throws IOException
   {
      _out.writeDouble(aDouble);
   }
   
   //
   // Direct byte writing interface
   //

   public void write (byte[] aBytes) throws IOException
   {
      _out.write(aBytes, 0, aBytes.length);
   }

   public void write (int aByte) throws IOException
   {
      _out.write(aByte);
   }

   /**
    * Current offset in file.
    */
   public int offset ()
   {
      return _out.size();
   }
}