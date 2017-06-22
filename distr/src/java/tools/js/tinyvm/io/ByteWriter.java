package js.tinyvm.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Basic byte writer implementation (write order independant part)
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
      // debug(1, aBoolean? 1 : 0);
      _out.writeBoolean(aBoolean);
   }

   public void writeByte (int aByte) throws IOException
   {
      // debug(1, aByte);
      _out.writeByte(aByte);
   }

   public void writeChar (int aChar) throws IOException
   {
      // debug(2, aChar);
      _out.writeChar(aChar);
   }

   public void writeShort (int aShort) throws IOException
   {
      // debug(2, aShort);
      _out.writeShort(aShort);
   }

   public void writeInt (int aInt) throws IOException
   {
      // debug(4, aInt);
      _out.writeInt(aInt);
   }

   public void writeLong (long aLong) throws IOException
   {
      // debug(8, aLong);
      _out.writeLong(aLong);
   }

   public void writeFloat (float aFloat) throws IOException
   {
      // debug(4, Float.floatToIntBits(aFloat));
      _out.writeFloat(aFloat);
   }

   public void writeDouble (double aDouble) throws IOException
   {
      // debug(8, Double.doubleToLongBits(aDouble));
      _out.writeDouble(aDouble);
   }

   //
   // Direct byte writing interface
   //

   public void write (byte[] aBytes) throws IOException
   {
      // debug(aBytes);
      _out.write(aBytes, 0, aBytes.length);
   }

   public void write (int aByte) throws IOException
   {
      // debug(1, aByte);
      _out.write(aByte);
   }

   /**
    * Current offset in file.
    */
   public int offset ()
   {
      return _out.size();
   }

   //
   // debug
   //

 /*  protected void debug (byte[] bytes)
   {
      String offset = "00000000" + Integer.toHexString(offset());
      offset = offset.substring(offset.length() - 4);
      System.out.println(offset + ": " + bytes.length + " bytes");
      System.out.flush();
   }

   protected void debug (int width, long aValue)
   {
      String offset = "00000000" + Integer.toHexString(offset());
      offset = offset.substring(offset.length() - 4);
      String value = "0000000000000000" + Long.toHexString(aValue);
      value = value.substring(value.length() - width * 2);
      System.out.println(offset + ": " + value);
      System.out.flush();
   }*/
}