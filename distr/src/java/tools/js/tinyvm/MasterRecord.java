package js.tinyvm;

import java.io.IOException;

import js.tinyvm.io.IByteWriter;
import js.tinyvm.io.IOUtilities;

/**
 * Master record.
 */
public class MasterRecord implements WritableData
{
   /**
    * The binary.
    */
   Binary _binary;

   /**
    * Constructor.
    * 
    * @param binary
    */
   public MasterRecord (Binary binary)
   {
      assert binary != null: "Precondition: binary != null";

      _binary = binary;
   }

   //
   // Writable interface
   //

   /**
    * Dump.
    */
   public void dump (IByteWriter writer) throws TinyVMException
   {
      assert writer != null: "Precondition: writer != null";

      int pMagicNumber = TinyVMConstants.MAGIC_MASK;
      int pConstantTableOffset = _binary.iConstantTable.getOffset();
      int pStaticFieldsOffset = _binary.iStaticFields.getOffset();
      int pStaticStateOffset = _binary.iStaticState.getOffset();
      int pStaticStateLength = (_binary.iStaticState.getLength() + 1) / 2;
      int pNumStaticFields = _binary.iStaticFields.size();
      int pEntryClassesOffset = _binary.iEntryClassIndices.getOffset();
      int pNumEntryClasses = _binary.iEntryClassIndices.size();
      assert pNumEntryClasses < TinyVMConstants.MAX_CLASSES: "Check: not too much classes";
      int pLastClass = _binary.iClassTable.size() - 1;
      assert pLastClass >= 0 && pLastClass < TinyVMConstants.MAX_CLASSES: "Check: class index in range";

      try
      {
         writer.writeU2(pMagicNumber);
         writer.writeU2(pConstantTableOffset);
         writer.writeU2(pStaticFieldsOffset);
         writer.writeU2(pStaticStateOffset);
         writer.writeU2(pStaticStateLength);
         writer.writeU2(pNumStaticFields);
         writer.writeU2(pEntryClassesOffset);
         writer.writeU1(pNumEntryClasses);
         writer.writeU1(pLastClass);
         IOUtilities.writePadding(writer, 2);
      }
      catch (IOException e)
      {
         throw new TinyVMException(e.getMessage(), e);
      }
   }

   /**
    * Length.
    */
   public int getLength ()
   {
      return IOUtilities.adjustedSize(16, 2);
   }
}

