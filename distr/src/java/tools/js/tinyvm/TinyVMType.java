package js.tinyvm;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.Type;

/**
 * This class encapsulates all functionality regarding tiny vm types.
 */
public class TinyVMType
{
   //
   // All byte values
   //
   
   public static final byte T_REFERENCE_TYPE = 0;
   public static final byte T_STACKFRAME_TYPE = 1;
   public static final byte T_BOOLEAN_TYPE = Constants.T_BOOLEAN;
   public static final byte T_CHAR_TYPE = Constants.T_CHAR;
   public static final byte T_FLOAT_TYPE = Constants.T_FLOAT;
   public static final byte T_DOUBLE_TYPE = Constants.T_DOUBLE;
   public static final byte T_BYTE_TYPE = Constants.T_BYTE;
   public static final byte T_SHORT_TYPE = Constants.T_SHORT;
   public static final byte T_INT_TYPE = Constants.T_INT;
   public static final byte T_LONG_TYPE = Constants.T_LONG;
   public static final byte T_ARRAY_TYPE = T_REFERENCE_TYPE;
   public static final byte T_OBJECT_TYPE = T_REFERENCE_TYPE;

   //
   // All values
   //
   
   public static final TinyVMType T_REFERENCE = new TinyVMType(T_REFERENCE_TYPE, 4);
   public static final TinyVMType T_BOOLEAN = new TinyVMType(T_BOOLEAN_TYPE, 1);
   public static final TinyVMType T_CHAR = new TinyVMType(T_CHAR_TYPE, 2);
   public static final TinyVMType T_FLOAT = new TinyVMType(T_FLOAT_TYPE, 4);
   public static final TinyVMType T_DOUBLE = new TinyVMType(T_DOUBLE_TYPE, 8);
   public static final TinyVMType T_BYTE = new TinyVMType(T_BYTE_TYPE, 1);
   public static final TinyVMType T_SHORT = new TinyVMType(T_SHORT_TYPE, 2);
   public static final TinyVMType T_INT = new TinyVMType(T_INT_TYPE, 4);
   public static final TinyVMType T_LONG = new TinyVMType(T_LONG_TYPE, 8);
   public static final TinyVMType T_ARRAY = T_REFERENCE;
   public static final TinyVMType T_OBJECT = T_REFERENCE;

   //
   // attributes
   //
   
   /**
    * Byte value of type.
    */
   private final byte _type;
   
   /**
    * Size in bytes of type.
    */
   private final int _size;
   
   /**
    * Constructor.
    */
   private TinyVMType (byte type, int size)
   {
      _type = type;
      _size = size;
   }

   //
   // public interface
   //

   /**
    * Get tiny vm type as byte.
    */
   public byte type ()
   {
      return _type;
   }

   /**
    * Get type size in bytes.
    */
   public int size ()
   {
      return _size;
   }

   /**
    * Is the given byte a valid tiny vm type?
    * 
    * @param type tiny vm type
    */
   public static boolean isValid (byte type)
   {
      switch (type)
      {
         case T_BOOLEAN_TYPE:
         case T_CHAR_TYPE:
         case T_FLOAT_TYPE:
         case T_DOUBLE_TYPE:
         case T_BYTE_TYPE:
         case T_SHORT_TYPE:
         case T_INT_TYPE:
         case T_LONG_TYPE:
         // case T_ARRAY:
         // case T_OBJECT:
         case T_REFERENCE_TYPE:
         {
            return true;
         }
         default:
         {
            return false;
         }
      }
   }

   /**
    * Get tiny vm type from bcel signature.
    * 
    * @param type bcel signature
    * @return tiny vm type
    */
   public static TinyVMType tinyVMTypeFromSignature (String signature)
   {
      return tinyVMType(Utility.typeOfSignature(signature));
   }

   /**
    * Get tiny vm type from bcel type.
    * 
    * @param type bcel type
    * @return tiny vm type
    */
   public static TinyVMType tinyVMType (Type type)
   {
      return tinyVMType(type.getType());
   }

   /**
    * Get tiny vm type from bcel type.
    * 
    * @param type bcel type
    * @return tiny vm type
    */
   public static TinyVMType tinyVMType (byte type)
   {
      switch (type)
      {
         case Constants.T_BOOLEAN:
         {
            return T_BOOLEAN;
         }
         case Constants.T_CHAR:
         {
            return T_CHAR;
         }
         case Constants.T_FLOAT:
         {
            return T_FLOAT;
         }
         case Constants.T_DOUBLE:
         {
            return T_DOUBLE;
         }
         case Constants.T_BYTE:
         {
            return T_BYTE;
         }
         case Constants.T_SHORT:
         {
            return T_SHORT;
         }
         case Constants.T_INT:
         {
            return T_INT;
         }
         case Constants.T_LONG:
         {
            return T_LONG;
         }
         case Constants.T_ARRAY:
         {
            return T_ARRAY;
         }
         case Constants.T_OBJECT:
         {
            return T_OBJECT;
         }
         default:
         {
            assert false: "Check: Known type";
            return null;
         }
      }
   }
}
