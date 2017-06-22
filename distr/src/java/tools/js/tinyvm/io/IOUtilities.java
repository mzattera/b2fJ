package js.tinyvm.io;

import java.io.IOException;

/**
 * Helper class for padding.
 */
public class IOUtilities
{
   /**
    * Write padding bytes.
    * 
    * @param writer writer to write to
    * @param aMinRecSize size of one record
    * @throws IOException
    */
   public static void writePadding (IByteWriter writer, int aMinRecSize)
      throws IOException
   {
      int pRegSize = writer.offset();
      int pPad = adjustedSize(pRegSize, aMinRecSize) - pRegSize;
      writer.write(new byte[pPad]);
   }

   /**
    * Size in bytes corrected to record boundaries.
    * 
    * @param aSize current size
    * @param aMinRecSize size after padding
    */
   public static int adjustedSize (int aSize, int aMinRecSize)
   {
      int pMod = aSize % aMinRecSize;
      if (pMod != 0)
      {
         return aSize + aMinRecSize - pMod;
      }
      return aSize;
   }
}

