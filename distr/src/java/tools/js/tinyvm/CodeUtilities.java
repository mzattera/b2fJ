package js.tinyvm;

import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Utility;

public class CodeUtilities implements OpCodeConstants, OpCodeInfo
{
   String iFullName;
   JavaClass iCF;
   Binary iBinary;

   public CodeUtilities (String aMethodName, JavaClass aCF, Binary aBinary)
   {
      iFullName = fullMethod(aCF, aMethodName);
      iCF = aCF;
      iBinary = aBinary;
   }

   public void exitOnBadOpCode (int aOpCode) throws TinyVMException
   {
      throw new TinyVMException("Unsupported " + OPCODE_NAME[aOpCode] + " in "
         + iFullName + ".\n"
         + "The following features/conditions are currently unsupported:\n"
         + "- Switch statements.\n"
         + "- Integer increment constant too large. (If > 255, declare it).\n"
         + "- Arithmetic or logical operations on variables of type long.\n"
         + "- Remainder operations on floats or doubles.\n"
         + "- Too many constants or locals ( > 255).\n"
         + "- Method code too long ( > 64 Kb!).\n" + "");
   }

   public static String fullMethod (JavaClass aCF, String aMethodName)
   {
      return aCF.getClassName() + ":" + aMethodName;
   }

   public int processConstantIndex (int aPoolIndex) throws TinyVMException
   {
      Constant pEntry = iCF.getConstantPool().getConstant(aPoolIndex); // TODO catch all (runtime) exceptions
      if (!(pEntry instanceof ConstantInteger)
         && !(pEntry instanceof ConstantFloat)
         && !(pEntry instanceof ConstantString)
         && !(pEntry instanceof ConstantDouble)
         && !(pEntry instanceof ConstantLong))
      {
         throw new TinyVMException("Classfile error: LDC-type instruction "
            + "does not refer to a suitable constant. ");
      }

      ConstantRecord pRecord = new ConstantRecord(iCF.getConstantPool(),
         (Constant) pEntry);
      int pIdx = iBinary.getConstantIndex(pRecord);

      if (pIdx == -1)
      {
         throw new TinyVMException("Bug CU-2: Didn't find constant " + pEntry
            + " of class " + iCF.getClassName());
      }

      return pIdx;
   }

   public int processClassIndex (int aPoolIndex) throws TinyVMException
   {
      Constant pEntry = iCF.getConstantPool().getConstant(aPoolIndex); // TODO catch all (runtime) exceptions
      if (!(pEntry instanceof ConstantClass))
      {
         throw new TinyVMException("Classfile error: Instruction requiring "
            + "CONSTANT_Class entry got "
            + (pEntry == null? "null" : pEntry.getClass().getName()));
      }
      ConstantClass pClassEntry = (ConstantClass) pEntry;
      String pClassName = pClassEntry.getBytes(iCF.getConstantPool());
      if (pClassName.startsWith("["))
      {
         throw new TinyVMException("In " + iFullName
            + ": Operations instanceof or " + "checkcast on array classes ("
            + pClassName + " in this case) are not yet supported by TinyVM.");
      }
      int pIdx = iBinary.getClassIndex(pClassName);
      if (pIdx == -1)
      {
         throw new TinyVMException("Bug CU-3: Didn't find class " + pEntry
            + " from class " + iCF.getClassName());
      }
      return pIdx;
   }

   public int processMultiArray (int aPoolIndex) throws TinyVMException
   {
      Constant pEntry = iCF.getConstantPool().getConstant(aPoolIndex); // TODO catch all (runtime) exceptions
      if (!(pEntry instanceof ConstantClass))
      {
         throw new TinyVMException("Classfile error: Instruction requiring "
            + "CONSTANT_Class entry got "
            + (pEntry == null? "null" : pEntry.getClass().getName()));
      }
      ConstantClass pClassEntry = (ConstantClass) pEntry;
      // TODO fix this?
      int[] pTypeDim = getTypeAndDimensions(pClassEntry.getBytes(iCF
         .getConstantPool()));

      assert pTypeDim[0] <= 0xFF: "Check: correct type";
      assert pTypeDim[1] > 0 && pTypeDim[1] <= 0xFF: "Check: correct dimension";

      return pTypeDim[0] << 8 | pTypeDim[1];
   }

   public static int[] getTypeAndDimensions (String aMultiArrayDesc)
   {
      int i = 0;
      while (aMultiArrayDesc.charAt(i) == '[')
         i++;
      return new int[]
      {
         Utility.typeOfSignature(aMultiArrayDesc.substring(i)), i
      };
   }

   /**
    * @return The word that should be written as parameter of putstatic,
    *         getstatic, getfield, or putfield.
    * @throws TinyVMException
    */
   int processField (int aFieldIndex, boolean aStatic) throws TinyVMException
   {
      Constant pEntry = iCF.getConstantPool().getConstant(aFieldIndex); // TODO catch all (runtime) exceptions
      if (!(pEntry instanceof ConstantFieldref))
      {
         throw new TinyVMException("Classfile error: Instruction requiring "
            + "CONSTANT_Fieldref entry got "
            + (pEntry == null? "null" : pEntry.getClass().getName()));
      }
      ConstantFieldref pFieldEntry = (ConstantFieldref) pEntry;
      String className = pFieldEntry.getClass(iCF.getConstantPool()).replace(
         '.', '/');
      ClassRecord pClassRecord = iBinary.getClassRecord(className);
      if (pClassRecord == null)
      {
         throw new TinyVMException("Bug CU-3: Didn't find class " + className
            + " from class " + iCF.getClassName());
      }
      ConstantNameAndType cnat = (ConstantNameAndType) iCF.getConstantPool()
         .getConstant(pFieldEntry.getNameAndTypeIndex());
      String pName = cnat.getName(iCF.getConstantPool());
      if (aStatic)
      {
         int pClassIndex = iBinary.getClassIndex(pClassRecord);
         assert pClassIndex >= 0 && pClassIndex <= 0xFF: "Check: class index in range";
         int pFieldIndex = pClassRecord.getStaticFieldIndex(pName);
         assert pFieldIndex >= 0 && pFieldIndex <= 0xFF: "Check: field index in range";

         return (pClassIndex << 8) | pFieldIndex;
      }
      else
      {
         int pOffset = pClassRecord.getInstanceFieldOffset(pName);
         if (pOffset == -1)
         {
            throw new TinyVMException("Error: Didn't find field " + className
               + ":" + pName + " from class " + iCF.getClassName());
         }
         assert pOffset <= TinyVMConstants.MAX_FIELD_OFFSET: "Check: field offset in range";
         TinyVMType fieldType = TinyVMType.tinyVMTypeFromSignature(cnat
            .getSignature(iCF.getConstantPool()));

         return (fieldType.type() << TinyVMConstants.F_SIZE_SHIFT) | pOffset;
      }
   }

   /**
    * @return The word that should be written as parameter of an invocation
    *         opcode.
    * @throws TinyVMException
    */
   int processMethod (int aMethodIndex, boolean aSpecial)
      throws TinyVMException
   {
      Constant pEntry = iCF.getConstantPool().getConstant(aMethodIndex); // TODO catch all (runtime) exceptions
      if (!(pEntry instanceof ConstantCP))
      {
         throw new TinyVMException("Classfile error: Instruction requiring "
            + "CONSTANT_MethodRef or CONSTANT_InterfaceMethodRef " + "got "
            + (pEntry == null? "null" : pEntry.getClass().getName()));
      }
      ConstantCP pMethodEntry = (ConstantCP) pEntry;
      String className = pMethodEntry.getClass(iCF.getConstantPool()).replace(
         '.', '/');
      ClassRecord pClassRecord = iBinary.getClassRecord(className);
      if (pClassRecord == null)
      {
         throw new TinyVMException("Bug CU-4: Didn't find class " + className
            + " from class " + iCF.getClassName());
      }
      ConstantNameAndType pNT = (ConstantNameAndType) iCF.getConstantPool()
         .getConstant(pMethodEntry.getNameAndTypeIndex());
      Signature pSig = new Signature(pNT.getName(iCF.getConstantPool()), pNT
         .getSignature(iCF.getConstantPool()));
      MethodRecord pMethod = pClassRecord.getVirtualMethodRecord(pSig);
      if (pMethod == null)
      {
         throw new TinyVMException("Method " + pSig + " not found  in "
            + className);
      }
      ClassRecord pTopClass = pMethod.getClassRecord();
      if (aSpecial)
      {
         int pClassIndex = iBinary.getClassIndex(pTopClass);
         assert pClassIndex != -1 && pClassIndex < TinyVMConstants.MAX_CLASSES: "Check: class index in range";
         int pMethodIndex = pTopClass.getMethodIndex(pMethod);
         assert pMethodIndex != -1
            && pMethodIndex < TinyVMConstants.MAX_METHODS: "Check: method index in range";
         // _logger.log(Level.INFO, "processMethod: special: " + pClassIndex
         //    + ", " + pMethodIndex);
         return (pClassIndex << 8) | (pMethodIndex & 0xFF);
      }
      else
      {
         int pNumParams = pMethod.getNumParameterWords() - 1;
         assert pNumParams < TinyVMConstants.MAX_PARAMETER_WORDS: "Check: number of parameters not to high";
         int pSignature = pMethod.getSignatureId();
         assert pSignature < TinyVMConstants.MAX_SIGNATURES: "Check: signature in range";

         return (pNumParams << TinyVMConstants.M_ARGS_SHIFT) | pSignature;
      }
   }

   public byte[] processCode (byte[] aCode) throws TinyVMException
   {
      byte[] pOutCode = new byte[aCode.length];
      int i = 0;
      while (i < aCode.length)
      {
         pOutCode[i] = aCode[i];
         int pOpCode = pOutCode[i] & 0xFF;

         i++;
         switch (pOpCode)
         {
            case OP_LDC:
               // Bug reported by Jochen Hiller, fixed by Lawrie Griffiths
               // convert aCode[i] not to an int, mask it to an unsigned byte
               // instead
               // pOutCode[i] = (byte) processConstantIndex ((int) aCode[i]);
               pOutCode[i] = (byte) processConstantIndex(aCode[i] & 0xFF);
               i++;
               break;
            case OP_LDC2_W:
               int pIdx1 = processConstantIndex((aCode[i] & 0xFF) << 8
                  | (aCode[i + 1] & 0xFF));
               pOutCode[i++] = (byte) (pIdx1 >> 8);
               pOutCode[i++] = (byte) (pIdx1 & 0xFF);
               break;
            case OP_ANEWARRAY:
               // Opcode is changed: ANEWARRAY -> NEWARRAY
               pOutCode[i - 1] = (byte) OP_NEWARRAY;
               pOutCode[i++] = (byte) TinyVMType.T_ARRAY_TYPE;
               pOutCode[i++] = (byte) OP_NOP;
               break;
            case OP_MULTIANEWARRAY:
               int pIdx2 = processMultiArray((aCode[i] & 0xFF) << 8
                  | (aCode[i + 1] & 0xFF));
               // Write element type
               pOutCode[i++] = (byte) (pIdx2 >> 8);
               // Write total number of dimensions
               pOutCode[i++] = (byte) (pIdx2 & 0xFF);
               // Skip requested dimensions for allocation
               pOutCode[i] = aCode[i];
               i++;
               break;
            case OP_NEW:
            case OP_CHECKCAST:
            case OP_INSTANCEOF:
               int pIdx3 = processClassIndex((aCode[i] & 0xFF) << 8
                  | (aCode[i + 1] & 0xFF));
               assert pIdx3 < TinyVMConstants.MAX_CLASSES: "Check: class index in range";
               pOutCode[i++] = (byte) (pIdx3 >> 8);
               pOutCode[i++] = (byte) (pIdx3 & 0xFF);
               break;
            case OP_PUTSTATIC:
            case OP_GETSTATIC:
               int pWord1 = processField((aCode[i] & 0xFF) << 8
                  | (aCode[i + 1] & 0xFF), true);
               pOutCode[i++] = (byte) (pWord1 >> 8);
               pOutCode[i++] = (byte) (pWord1 & 0xFF);
               break;
            case OP_PUTFIELD:
            case OP_GETFIELD:
               int pWord2 = processField((aCode[i] & 0xFF) << 8
                  | (aCode[i + 1] & 0xFF), false);
               pOutCode[i++] = (byte) (pWord2 >> 8);
               pOutCode[i++] = (byte) (pWord2 & 0xFF);
               break;
            case OP_INVOKEINTERFACE:
               // Opcode is changed:
               pOutCode[i - 1] = (byte) OP_INVOKEVIRTUAL;
               int pWord3 = processMethod((aCode[i] & 0xFF) << 8
                  | (aCode[i + 1] & 0xFF), false);
               pOutCode[i++] = (byte) (pWord3 >> 8);
               pOutCode[i++] = (byte) (pWord3 & 0xFF);
               pOutCode[i++] = (byte) OP_NOP; // before: count
               pOutCode[i++] = (byte) OP_NOP; // before: 0
               break;
            case OP_INVOKESPECIAL:
            case OP_INVOKESTATIC:
               // Opcode is changed:
               int pWord4 = processMethod((aCode[i] & 0xFF) << 8
                  | (aCode[i + 1] & 0xFF), true);
               pOutCode[i++] = (byte) (pWord4 >> 8);
               pOutCode[i++] = (byte) (pWord4 & 0xFF);
               break;
            case OP_INVOKEVIRTUAL:
               // Opcode is changed:
               int pWord5 = processMethod((aCode[i] & 0xFF) << 8
                  | (aCode[i + 1] & 0xFF), false);
               pOutCode[i++] = (byte) (pWord5 >> 8);
               pOutCode[i++] = (byte) (pWord5 & 0xFF);
               break;
            case OP_LOOKUPSWITCH:
            case OP_TABLESWITCH:
            case OP_WIDE:
            case OP_GOTO_W:
            case OP_JSR_W:
            case OP_LDC_W:
            case OP_LADD:
            case OP_LSUB:
            case OP_LMUL:
            case OP_LDIV:
            case OP_LREM:
            case OP_LNEG:
            case OP_LCMP:
            case OP_FREM:
            case OP_DREM:
            case OP_LSHL:
            case OP_LSHR:
            case OP_LUSHR:
            case OP_LAND:
            case OP_LOR:
            case OP_LXOR:
               exitOnBadOpCode(pOpCode);
               break;
            case OP_BREAKPOINT:
            {
               throw new TinyVMException("Invalid opcode detected: " + pOpCode
                  + " " + OPCODE_NAME[pOpCode]);
            }
            default:
               int pArgs = OPCODE_ARGS[pOpCode];
               if (pArgs == -1)
               {
                  throw new TinyVMException("Bug CU-1: Got " + pOpCode + " in "
                     + iFullName + ".");
               }
               for (int ctr = 0; ctr < pArgs; ctr++)
                  pOutCode[i + ctr] = aCode[i + ctr];
               i += pArgs;
               break;
         }
      }
      return pOutCode;
   }

   // private static final Logger _logger = Logger.getLogger("TinyVM");
}