package js.tinyvm;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * Class for creating a report of a binary.
 */
public class BinaryReport
{
   /**
    * Binary.
    */
   private Binary _binary;

   /**
    * Constructor.
    * 
    * @param binary binary to create report from.
    */
   public BinaryReport (Binary binary)
   {
      assert binary != null: "Precondition: binary != null";

      _binary = binary;
   }

   /**
    * Create report.
    * 
    * @param writer writer to write result to
    */
   public void report (Writer writer) throws TinyVMException
   {
      PrintWriter out = new PrintWriter(writer);

      // all classes
      for (int pIndex = 0; pIndex < _binary.iClassTable.size(); pIndex++)
      {
         ClassRecord pRec = (ClassRecord) _binary.iClassTable.get(pIndex);
         out.println("Class " + pIndex + ": " + pRec.iName);
      }

      // all signatures
      int pSize = _binary.iSignatures.size();
      for (int i = 0; i < pSize; i++)
      {
         Signature pSig = (Signature) _binary.iSignatures.elementAt(i);
         out.println("Signature " + i + ": " + pSig.getImage());
      }

      // all records
      out.println("Master record : " + _binary.iMasterRecord.getLength()
         + " bytes.");
      out.println("Class records : " + _binary.iClassTable.size() + " ("
         + _binary.iClassTable.getLength() + " bytes).");
      out.println("Field records : " + _binary.getTotalNumInstanceFields()
         + " (" + _binary.iInstanceFieldTables.getLength() + " bytes).");
      out.println("Method records: " + _binary.getTotalNumMethods() + " ("
         + _binary.iMethodTables.getLength() + " bytes).");
      out.println("Code          : " + _binary.iCodeSequences.size() + " ("
         + _binary.iCodeSequences.getLength() + " bytes).");

      // all tables
      out.println("Class table offset   : " + _binary.iClassTable.getOffset());
      out.println("Constant table offset: "
         + _binary.iConstantTable.getOffset());
      out
         .println("Method tables offset : " + _binary.iMethodTables.getOffset());
      out.println("Excep tables offset  : "
         + _binary.iExceptionTables.getOffset());
   }
}