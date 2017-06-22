package js.tinyvm;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import js.tinyvm.io.IByteWriter;
import js.tinyvm.io.IOUtilities;
import js.tinyvm.util.HashVector;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.Type;

public class MethodRecord implements WritableData
{
   Method iMethod;
   ClassRecord iClassRecord;
   RecordTable iExceptionTable = null;
   CodeSequence iCodeSequence = null;

   int iSignatureId; // DONE
   int iNumLocals; // DONE
   int iNumOperands; // DONE
   int iNumParameters; // DONE
   int iNumExceptionHandlers; // DONE
   int iFlags; // DONE

   public MethodRecord (Method aEntry, Signature aSignature,
      ClassRecord aClassRec, Binary aBinary, RecordTable aExceptionTables,
      HashVector aSignatures) throws TinyVMException
   {
      iClassRecord = aClassRec;
      iMethod = aEntry;
      Code pCodeAttrib = iMethod.getCode();
      boolean pNoBody = iMethod.isAbstract() || iMethod.isNative();
      assert pCodeAttrib != null || pNoBody: "Check: body is present";
      assert pCodeAttrib == null || !pNoBody: "Check: no body is present";
      aSignatures.addElement(aSignature);
      iSignatureId = aSignatures.indexOf(aSignature);
      if (iSignatureId >= TinyVMConstants.MAX_SIGNATURES)
      {
         throw new TinyVMException(
            "The total number of unique signatures exceeds "
               + TinyVMConstants.MAX_SIGNATURES);
      }
      iNumLocals = pCodeAttrib == null? 0 : pCodeAttrib.getMaxLocals();
      if (iNumLocals > TinyVMConstants.MAX_LOCALS)
      {
         throw new TinyVMException("Method " + aClassRec.getName() + "."
            + iMethod.getName() + " has " + iNumLocals + " local words. Only "
            + TinyVMConstants.MAX_LOCALS + " are allowed.");
      }
      iNumOperands = pCodeAttrib == null? 0 : pCodeAttrib.getMaxStack();
      if (iNumOperands > TinyVMConstants.MAX_OPERANDS)
      {
         throw new TinyVMException("Method " + aClassRec.getName() + "."
            + iMethod.getName() + " has an operand stack "
            + " whose potential size is " + iNumOperands + ". " + "Only "
            + TinyVMConstants.MAX_OPERANDS + " are allowed.");
      }
      iNumParameters = getNumParamWords(iMethod);
      if (iNumParameters > TinyVMConstants.MAX_PARAMETER_WORDS)
      {
         throw new TinyVMException("Method " + aClassRec.getName() + "."
            + iMethod.getName() + " has " + iNumParameters
            + " parameter words. Only " + TinyVMConstants.MAX_PARAMETER_WORDS
            + " are allowed.");
      }
      if (iMethod.isNative() && !aBinary.isSpecialSignature(aSignature))
      {
         _logger.log(Level.WARNING, "Native method signature " + aSignature
            + " unrecognized. You are probably using JDK APIs "
            + " or libraries that cannot be run under leJOS.");
      }

      if (pCodeAttrib != null)
      {
         iExceptionTable = new RecordTable("exceptions", true, false);
         CodeException[] pExcepTable = pCodeAttrib.getExceptionTable();
         iNumExceptionHandlers = pExcepTable.length;
         if (iNumExceptionHandlers > TinyVMConstants.MAX_EXCEPTION_HANDLERS)
         {
            throw new TinyVMException("Method " + aClassRec.getName() + "."
               + iMethod.getName() + " has " + iNumExceptionHandlers
               + " exception handlers. Only "
               + TinyVMConstants.MAX_EXCEPTION_HANDLERS + " are allowed.");
         }
         storeExceptionTable(pExcepTable, aBinary, aClassRec.iCF);
         aExceptionTables.add(iExceptionTable);
      }
      initFlags();
   }

   public int getFlags ()
   {
      return iFlags;
   }

   public ClassRecord getClassRecord ()
   {
      return iClassRecord;
   }

   public void initFlags ()
   {
      iFlags = 0;
      if (iMethod.isNative())
         iFlags |= TinyVMConstants.M_NATIVE;
      if (iMethod.isSynchronized())
         iFlags |= TinyVMConstants.M_SYNCHRONIZED;
      if (iMethod.isStatic())
         iFlags |= TinyVMConstants.M_STATIC;
   }

   public void copyCode (RecordTable aCodeSequences, JavaClass aClassFile,
      Binary aBinary)
   {
      Code pCodeAttrib = iMethod.getCode();
      if (pCodeAttrib != null)
      {
         iCodeSequence = new CodeSequence();
         copyCode(pCodeAttrib.getCode(), aClassFile, aBinary);
         aCodeSequences.add(iCodeSequence);
      }
   }

   public void postProcessCode (RecordTable aCodeSequences,
      JavaClass aClassFile, Binary aBinary) throws TinyVMException
   {
      Code pCodeAttrib = iMethod.getCode();
      if (pCodeAttrib != null)
      {
         postProcessCode(pCodeAttrib.getCode(), aClassFile, aBinary);
      }
   }

   /**
    * Number of parameter words, including <code>this</code>.
    * 
    * @param aMethod bcel method object
    */
   public static int getNumParamWords (Method aMethod)
   {
      Type[] types = aMethod.getArgumentTypes();
      int pWords = 0;
      for (int i = 0; i < types.length; i++)
      {
         assert types[i].getType() <= 0xF: "Check: known type";
         switch (types[i].getType())
         {
            case Constants.T_LONG:
            case Constants.T_DOUBLE:
               pWords += 2;
               break;
            default:
               pWords++;
         }
      }
      return pWords + (aMethod.isStatic()? 0 : 1);
   }

   public void storeExceptionTable (CodeException[] aExcepTable,
      Binary aBinary, JavaClass aCF)
   {
      for (int i = 0; i < aExcepTable.length; i++)
      {
         CodeException pExcep = aExcepTable[i];
         try
         {
            iExceptionTable.add(new ExceptionRecord(pExcep, aBinary, aCF));
         }
         catch (Throwable t)
         {
            t.printStackTrace();
         }
      }
   }

   public void copyCode (byte[] aCode, JavaClass aClassFile, Binary aBinary)
   {
      if (aCode == null)
         return;
      iCodeSequence.setBytes(aCode);
   }

   public void postProcessCode (byte[] aCode, JavaClass aClassFile,
      Binary aBinary) throws TinyVMException
   {
      if (aCode == null)
         return;
      CodeUtilities pUtils = new CodeUtilities(iMethod.getName().toString(),
         aClassFile, aBinary);
      byte[] pNewCode = pUtils.processCode(aCode);
      iCodeSequence.setBytes(pNewCode);
   }

   public int getLength ()
   {
      return IOUtilities.adjustedSize(2 + // signature
         2 + // exception table offset
         2 + // code offset
         1 + // number of locals
         1 + // max. operands
         1 + // number of parameters
         1 + // number of exception handlers
         1, // flags
         2);
   }

   public void dump (IByteWriter aOut) throws TinyVMException
   {
      try
      {
         aOut.writeU2(iSignatureId);
         aOut.writeU2(iExceptionTable == null? 0 : iExceptionTable.getOffset());
         aOut.writeU2(iCodeSequence == null? 0 : iCodeSequence.getOffset());
         aOut.writeU1(iNumLocals);
         aOut.writeU1(iNumOperands);
         aOut.writeU1(iNumParameters);
         aOut.writeU1(iNumExceptionHandlers);
         aOut.writeU1(iFlags);
         IOUtilities.writePadding(aOut, 2);
      }
      catch (IOException e)
      {
         throw new TinyVMException(e.getMessage(), e);
      }
   }

   public int getNumParameterWords ()
   {
      return iNumParameters;
   }

   public int getSignatureId ()
   {
      return iSignatureId;
   }

   public boolean equals (Object aOther)
   {
      if (!(aOther instanceof MethodRecord))
         return false;
      return ((MethodRecord) aOther).iMethod.equals(iMethod);
   }

   public int hashCode ()
   {
      return iMethod.hashCode();
   }

   private static final Logger _logger = Logger.getLogger("TinyVM");
}

