package js.tinyvm;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import js.tinyvm.io.IByteWriter;

public class CodeSequence extends WritableDataWithOffset
{
   private byte[] iBytes;

   public void setBytes (byte[] aBytes)
   {
      iBytes = aBytes;
   }

   public int getLength ()
   {
      if (iBytes == null)
         return 0;
      return iBytes.length;
   }

   public void dump (IByteWriter aOut) throws TinyVMException
   {
      try
      {
         if (iBytes == null)
         {
            _logger.log(Level.WARNING, "Not writing code sequence");
            return;
         }
         aOut.write(iBytes);
      }
      catch (IOException e)
      {
         throw new TinyVMException(e.getMessage(), e);
      }
   }

   private static final Logger _logger = Logger.getLogger("TinyVM");
}

