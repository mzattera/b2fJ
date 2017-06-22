package js.tinyvm;

import java.io.IOException;

import js.tinyvm.io.IByteWriter;

import org.apache.bcel.classfile.Field;

public class InstanceFieldRecord implements WritableData
{
   Field iField;
   TinyVMType iType;

   public InstanceFieldRecord (Field aEntry) throws TinyVMException
   {
      iField = aEntry;
      iType = TinyVMType.tinyVMType(iField.getType());
   }

   public String getName ()
   {
      return iField.getName();
   }

   public int getLength ()
   {
      return 1;
   }

   public void dump (IByteWriter aOut) throws TinyVMException
   {
      try
      {
         aOut.writeU1((int) iType.type());
      }
      catch (IOException e)
      {
         throw new TinyVMException(e.getMessage(), e);
      }
   }

   public int getFieldSize () throws TinyVMException
   {
      return iType.size();
   }

   public boolean equals (Object aOther)
   {
      if (!(aOther instanceof InstanceFieldRecord))
         return false;
      return ((InstanceFieldRecord) aOther).iField.equals(iField);
   }

   public int hashCode ()
   {
      return iField.hashCode();
   }
}

