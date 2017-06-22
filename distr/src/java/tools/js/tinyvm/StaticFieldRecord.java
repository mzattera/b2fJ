package js.tinyvm;

import java.io.IOException;

import js.tinyvm.io.IByteWriter;

import org.apache.bcel.classfile.Field;

public class StaticFieldRecord implements WritableData
{
   ClassRecord iClassRecord;
   Field iField;

   public StaticFieldRecord (Field aEntry, ClassRecord aRec)
   {
      iField = aEntry;
      iClassRecord = aRec;
   }

   public String getName ()
   {
      return iField.getName();
   }

   public int getLength ()
   {
      return 2;
   }

   public void dump (IByteWriter aOut) throws TinyVMException
   {
      TinyVMType type = TinyVMType.tinyVMType(iField.getType());
      int pOffset = iClassRecord.getStaticFieldOffset(iField.getName());
      assert pOffset >= 0 && pOffset <= 0x0FFF: "Check offset in range";

      try
      {
         aOut.writeU2((type.type() << 12) | pOffset);
      }
      catch (IOException e)
      {
         throw new TinyVMException(e.getMessage(), e);
      }
   }

   public boolean equals (Object aOther)
   {
      if (!(aOther instanceof StaticFieldRecord))
         return false;
      return ((StaticFieldRecord) aOther).iField.equals(iField)
         && ((StaticFieldRecord) aOther).iClassRecord.equals(iClassRecord);
   }

   public int hashCode ()
   {
      return iField.hashCode();
   }
}

