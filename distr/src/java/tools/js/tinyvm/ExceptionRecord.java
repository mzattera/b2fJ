package js.tinyvm;

import java.io.IOException;

import js.tinyvm.io.IByteWriter;
import js.tinyvm.io.IOUtilities;

import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.JavaClass;

public class ExceptionRecord implements WritableData
{
   CodeException iExcep;
   int iClassIndex;

   public ExceptionRecord (CodeException aExcep, Binary aBinary, JavaClass aCF)
      throws Exception
   {
      iExcep = aExcep;
      int pCPIndex = aExcep.getCatchType();
      if (pCPIndex == 0)
      {
         // An index of 0 means ANY.
         iClassIndex = aBinary.getClassIndex("java/lang/Throwable");
      }
      else
      {
         ConstantClass pCls = (ConstantClass) aCF.getConstantPool()
            .getConstant(pCPIndex);
         String pName = pCls.getBytes(aCF.getConstantPool());
         iClassIndex = aBinary.getClassIndex(pName);
      }
      if (iClassIndex == -1)
      {
         throw new TinyVMException("Exception not found: " + iExcep);
      }
   }

   public int getLength ()
   {
      return IOUtilities.adjustedSize(2 + // start
         2 + // end
         2 + // handler
         1, // class index
         2);
   }

   public void dump (IByteWriter aOut) throws TinyVMException
   {
      int pStart = iExcep.getStartPC();
      int pEnd = iExcep.getEndPC();
      int pHandler = iExcep.getHandlerPC();
      if (pStart > TinyVMConstants.MAX_CODE || pEnd > TinyVMConstants.MAX_CODE
         || pHandler > TinyVMConstants.MAX_CODE)
      {
         throw new TinyVMException("Exception handler with huge PCs");
      }

      try
      {
         aOut.writeU2(pStart);
         aOut.writeU2(pEnd);
         aOut.writeU2(pHandler);
         aOut.writeU1(iClassIndex);
         IOUtilities.writePadding(aOut, 2);
      }
      catch (IOException e)
      {
         throw new TinyVMException(e.getMessage(), e);
      }
   }

   public boolean equals (Object aOther)
   {
      if (!(aOther instanceof ExceptionRecord))
         return false;
      return ((ExceptionRecord) aOther).iExcep.equals(iExcep);
   }

   public int hashCode ()
   {
      return iExcep.hashCode();
   }
}

