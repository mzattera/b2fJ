package js.tinyvm;

import java.io.IOException;

import js.tinyvm.io.IByteWriter;

import org.apache.bcel.classfile.Field;

public class StaticValue extends WritableDataWithOffset
{
   Field _field;
   TinyVMType iType;

   public StaticValue (Field aEntry) throws TinyVMException
   {
      _field = aEntry;
      iType = TinyVMType.tinyVMType(_field.getType());
   }

   public int getLength () throws TinyVMException
   {
      return iType.size();
   }

   public void dump (IByteWriter writer) throws TinyVMException
   {
      try
      {
         // Static values must be dumped in Big Endian order
         switch (iType.type())
         {
            case TinyVMType.T_BOOLEAN_TYPE:
               writer.writeBoolean(false);
               break;
            case TinyVMType.T_BYTE_TYPE:
               writer.writeByte(0);
               break;
            case TinyVMType.T_CHAR_TYPE:
               writer.writeChar(0);
               break;
            case TinyVMType.T_SHORT_TYPE:
               writer.writeShort(0);
               break;
            // case TinyVMType.T_ARRAY_TYPE:
            // case TinyVMType.T_OBJECT_TYPE:
            case TinyVMType.T_REFERENCE_TYPE:
            case TinyVMType.T_INT_TYPE:
               writer.writeInt(0);
               break;
            case TinyVMType.T_FLOAT_TYPE:
               writer.writeFloat((float) 0.0);
               break;
            case TinyVMType.T_LONG_TYPE:
               writer.writeLong(0L);
               break;
            case TinyVMType.T_DOUBLE_TYPE:
               writer.writeInt(0);
               writer.writeFloat((float) 0.0);
               break;
            default:
               assert false: "Check: valid type: " + iType;
         }
      }
      catch (IOException e)
      {
         throw new TinyVMException(e.getMessage(), e);
      }
   }

   public boolean equals (Object aOther)
   {
      if (!(aOther instanceof StaticValue))
         return false;
      return ((StaticValue) aOther).iType == iType;
   }

   public int hashCode ()
   {
      return iType.type();
   }
}

