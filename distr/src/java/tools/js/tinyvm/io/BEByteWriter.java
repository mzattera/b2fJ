package js.tinyvm.io;

import java.io.IOException;
import java.io.OutputStream;

public class BEByteWriter extends ByteWriter
{
   public BEByteWriter (OutputStream stream)
   {
      super(stream);
   }

   //
   // specific write order
   //

   public void writeU1 (int aByte) throws IOException
   {
      writeByte(aByte);
   }

   public void writeU2 (int aShort) throws IOException
   {
      writeShort(aShort);
   }

   public void writeU4 (int aInt) throws IOException
   {
      writeInt(aInt);
   }

   public void writeU8 (long aLong) throws IOException
   {
      writeLong(aLong);
   }
}