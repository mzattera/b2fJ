package js.tinyvm;

import js.tinyvm.io.IByteWriter;

public interface WritableData
{
   /**
    * Dump.
    * 
    * @param writer writer to write binary to
    * @throws TinyVMException
    */
   public void dump (IByteWriter writer) throws TinyVMException;

   /**
    * Returns the length of the record, in bytes.
    * 
    * @throws TinyVMException
    */
   public int getLength () throws TinyVMException;
}