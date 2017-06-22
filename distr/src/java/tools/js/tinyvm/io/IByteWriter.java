package js.tinyvm.io;

import java.io.IOException;

public interface IByteWriter
{
   //
   // specific write order
   //
   
   public void writeU1 (int aByte) throws IOException;

   public void writeU2 (int aShort) throws IOException;

   public void writeU4 (int aInt) throws IOException;

   public void writeU8 (long aLong) throws IOException;

   //
   // java write order
   //
   
   public void writeBoolean (boolean aBoolean) throws IOException;

   public void writeByte (int aByte) throws IOException;

   public void writeChar (int aChar) throws IOException;

   public void writeShort (int aShort) throws IOException;
   
   public void writeInt (int aInt) throws IOException;

   public void writeLong (long aLong) throws IOException;

   public void writeFloat (float aFloat) throws IOException;

   public void writeDouble (double aDouble) throws IOException;

   //
   // Direct byte writing interface
   //
   
   public void write (byte[] aBytes) throws IOException;

   public void write (int aByte) throws IOException;
   
   public int offset ();
}

