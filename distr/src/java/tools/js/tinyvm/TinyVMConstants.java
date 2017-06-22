package js.tinyvm;


public class TinyVMConstants
{
   public static final String VERSION = "2.1.0";
   public static final int VERIFY_LEVEL = 2;

   public static final int MAGIC_MASK = 0xCAF6;

   public static final int F_SIZE_SHIFT = 12;
   public static final int F_OFFSET_MASK = 0x0FFF;

   public static final int M_ARGS_SHIFT = 12;
   public static final int M_SIG_MASK = 0x0FFF;

   public static final int MAX_CLASSES = 256;
   public static final int MAX_FIELDS = 255;
   public static final int MAX_METHODS = 255;
   public static final int MAX_PARAMETER_WORDS = 16;
   public static final int MAX_SIGNATURES = M_SIG_MASK + 1;
   public static final int MAX_OPERANDS = 255;
   public static final int MAX_LOCALS = 255;
   public static final int MAX_EXCEPTION_HANDLERS = 255;
   public static final int MAX_CODE = 0xFFFF;
   public static final int MAX_CONSTANTS = 256;
   public static final int MAX_FIELD_OFFSET = F_OFFSET_MASK;
   public static final int MAX_STRING_CONSTANT_LENGTH = 255;

   public static final int C_INITIALIZED = 0x01;
   public static final int C_ARRAY = 0x02;
   public static final int C_HASCLINIT = 0x04;
   public static final int C_INTERFACE = 0x08;

   public static final int M_NATIVE = 0x01;
   public static final int M_SYNCHRONIZED = 0x02;
   public static final int M_STATIC = 0x04;
}