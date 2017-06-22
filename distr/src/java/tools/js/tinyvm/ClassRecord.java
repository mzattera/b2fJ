package js.tinyvm;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import js.tinyvm.io.IByteWriter;
import js.tinyvm.io.IOUtilities;
import js.tinyvm.util.HashVector;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

/**
 * Abstraction for a class record (see vmsrc/language.h).
 */
public class ClassRecord implements WritableData
{
   int iIndex = -1;
   String iName;
   /**
    * On-demand size of the class.
    */
   int iClassSize = -1;
   JavaClass iCF;
   Binary iBinary;
   final RecordTable iMethodTable = new RecordTable("methods", false, false);
   final RecordTable iInstanceFields = new RecordTable("instance fields", true,
      false);
   final Hashtable iStaticValues = new Hashtable();
   final Hashtable iStaticFields = new Hashtable();
   final Hashtable iMethods = new Hashtable();
   final Vector iUsedMethods = new Vector();
   int iParentClassIndex;
   int iArrayElementType;
   int iFlags;
   boolean iUseAllMethods = false;

   public void useAllMethods ()
   {
      iUseAllMethods = true;
   }

   public String getName ()
   {
      return iCF.getClassName();
   }

   public int getLength ()
   {
      return IOUtilities.adjustedSize(2 + // class size
         2 + // method table offset
         2 + // instance field table offset
         1 + // number of fields
         1 + // number of methods
         1 + // parent class
         1, // flags
         2);
   }

   public void dump (IByteWriter aOut) throws TinyVMException
   {
      try
      {
         int pAllocSize = getAllocationSize();
         assert pAllocSize != 0: "Check: alloc ok";
         aOut.writeU2(pAllocSize);
         int pMethodTableOffset = iMethodTable.getOffset();
         aOut.writeU2(pMethodTableOffset);
         aOut.writeU2(iInstanceFields.getOffset());
         int pNumFields = iInstanceFields.size();
         if (pNumFields > TinyVMConstants.MAX_FIELDS)
         {
            throw new TinyVMException("Class " + iName + ": No more than "
               + TinyVMConstants.MAX_FIELDS + " fields expected");
         }
         aOut.writeU1(pNumFields);
         int pNumMethods = iMethodTable.size();
         if (pNumMethods > TinyVMConstants.MAX_METHODS)
         {
            throw new TinyVMException("Class " + iName + ": No more than "
               + TinyVMConstants.MAX_METHODS + " methods expected");
         }
         aOut.writeU1(pNumMethods);
         aOut.writeU1(iParentClassIndex);
         //aOut.writeU1 (iArrayElementType);
         aOut.writeU1(iFlags);
         IOUtilities.writePadding(aOut, 2);
      }
      catch (IOException e)
      {
         throw new TinyVMException(e.getMessage(), e);
      }
   }

   public boolean isArray ()
   {
      // TBD:
      return false;
   }

   public boolean isInterface ()
   {
      return iCF.isInterface();
   }

   public boolean hasStaticInitializer ()
   {
      Method[] methods = iCF.getMethods();
      for (int i = 0; i < methods.length; i++)
      {
         if (methods[i].getName().equals(Constants.STATIC_INITIALIZER_NAME))
         {
            return true;
         }
      }
      return false;
   }

   /**
    * (Call only after record has been processed).
    */
   public boolean hasMethod (Signature aSignature, boolean aStatic)
   {
      MethodRecord pRec = (MethodRecord) iMethods.get(aSignature);
      if (pRec == null)
         return false;
      return ((pRec.getFlags() & TinyVMConstants.M_STATIC) == 0) ^ aStatic;
   }

   public void initFlags ()
   {
      iFlags = 0;
      if (isArray())
         iFlags |= TinyVMConstants.C_ARRAY;
      if (isInterface())
         iFlags |= TinyVMConstants.C_INTERFACE;
      if (hasStaticInitializer())
         iFlags |= TinyVMConstants.C_HASCLINIT;
   }

   /**
    * @return Number of words required for object allocation.
    * @throws TinyVMException
    */
   public int getAllocationSize () throws TinyVMException
   {
      return (getClassSize() + 5) / 2;
   }

   /**
    * @return Number of bytes occupied by instance fields.
    * @throws TinyVMException
    */
   public int getClassSize () throws TinyVMException
   {
      if (iClassSize != -1)
         return iClassSize;
      iClassSize = computeClassSize();
      return iClassSize;
   }

   /**
    * @return The size of the class in 2-byte words, including any VM space.
    *         This is the exact size required for memory allocation.
    * @throws TinyVMException
    */
   public int computeClassSize () throws TinyVMException
   {
      int pSize = hasParent()? getParent().getClassSize() : 0;
      for (Iterator iter = iInstanceFields.iterator(); iter.hasNext();)
      {
         InstanceFieldRecord pRec = (InstanceFieldRecord) iter.next();
         pSize += pRec.getFieldSize();
      }
      return pSize;
   }

   public boolean hasParent ()
   {
      return !"java.lang.Object".equals(iCF.getClassName());
   }
   
   public ClassRecord getParent ()
   {
      assert hasParent(): "Precondition: hasParent()";

      ClassRecord result = iBinary.getClassRecord(iCF.getSuperclassName().replace('.', '/'));
      
      assert result != null: "Postconditon: result != null";
      return result;
   }

   public void initParent () throws TinyVMException
   {
      if (hasParent())
      {
         iParentClassIndex = iBinary.getClassIndex(getParent());
         if (iParentClassIndex == -1)
         {
            throw new TinyVMException("Superclass of " + iCF.getClassName() + " not found");
         }
      }
      else // only java.lang.Object has no super class...
      {
         iParentClassIndex = 0;
         if (!iCF.getClassName().equals("java.lang.Object"))
         {
            throw new TinyVMException("Expected java.lang.Object: "
               + iCF.getClassName());
         }
      }
   }

   public void storeReferredClasses (Hashtable aClasses,
      RecordTable aClassRecords, ClassPath aClassPath, Vector aInterfaceMethods)
      throws TinyVMException
   {
      // _logger.log(Level.INFO, "Processing CONSTANT_Class entries in " +
      // iName);

      ConstantPool pPool = iCF.getConstantPool();
      Constant[] constants = pPool.getConstantPool();
      for (int i = 0; i < constants.length; i++)
      {
         Constant pEntry = constants[i];
         if (pEntry instanceof ConstantClass)
         {
            String pClassName = ((ConstantClass) pEntry).getBytes(pPool);
            if (pClassName.startsWith("["))
            {
               // _logger.log(Level.INFO, "Skipping array: " + pClassName);
               continue;
            }
            if (aClasses.get(pClassName) == null)
            {
               ClassRecord pRec = ClassRecord.getClassRecord(pClassName,
                  aClassPath, iBinary);
               aClasses.put(pClassName, pRec);
               aClassRecords.add(pRec);
            }
         }
         else if (pEntry instanceof ConstantMethodref)
         {
            String className = ((ConstantMethodref) pEntry).getClass(pPool)
               .replace('.', '/');
            ClassRecord pClassRec = (ClassRecord) aClasses.get(className);
            if (pClassRec == null)
            {
               pClassRec = ClassRecord.getClassRecord(className, aClassPath,
                  iBinary);
               aClasses.put(className, pClassRec);
               aClassRecords.add(pClassRec);
            }
            ConstantNameAndType cnat = (ConstantNameAndType) iCF
               .getConstantPool().getConstant(
                  ((ConstantMethodref) pEntry).getNameAndTypeIndex());
            pClassRec.addUsedMethod(cnat.getName(iCF.getConstantPool()) + ":"
               + cnat.getSignature(iCF.getConstantPool()));
         }
         else if (pEntry instanceof ConstantInterfaceMethodref)
         {
            ConstantNameAndType cnat = (ConstantNameAndType) iCF
               .getConstantPool().getConstant(
                  ((ConstantInterfaceMethodref) pEntry).getNameAndTypeIndex());
            aInterfaceMethods.addElement(cnat.getName(iCF.getConstantPool())
               + ":" + cnat.getSignature(iCF.getConstantPool()));
         }
         else if (pEntry instanceof ConstantNameAndType)
         {
            if (((ConstantNameAndType) pEntry).getSignature(
               iCF.getConstantPool()).substring(0, 1).equals("("))
            {
               if (!((ConstantNameAndType) pEntry).getName(
                  iCF.getConstantPool()).substring(0, 1).equals("<"))
               {
                  aInterfaceMethods.addElement(((ConstantNameAndType) pEntry)
                     .getName(iCF.getConstantPool())
                     + ":"
                     + ((ConstantNameAndType) pEntry).getSignature(iCF
                        .getConstantPool()));
               }
            }
         }
      }
   }

   public void addUsedMethod (String aRef)
   {
      iUsedMethods.addElement(aRef);
   }

   public static String cpEntryId (Constant aEntry)
   {
      String pClassName = aEntry.getClass().getName();
      int pDotIdx = pClassName.lastIndexOf('.');
      return pDotIdx == -1? pClassName : pClassName.substring(pDotIdx + 1);
   }

   MethodRecord getMethodRecord (Signature aSig)
   {
      return (MethodRecord) iMethods.get(aSig);
   }

   MethodRecord getVirtualMethodRecord (Signature aSig)
   {
      MethodRecord pRec = getMethodRecord(aSig);
      if (pRec != null)
         return pRec;
      if (!hasParent())
      {
         return null;
      }
      return getParent().getVirtualMethodRecord(aSig);
   }

   int getMethodIndex (MethodRecord aRecord)
   {
      return iMethodTable.indexOf(aRecord);
   }

   int getApparentInstanceFieldOffset (String aName) throws TinyVMException
   {
      int pOffset = hasParent()? getParent().getClassSize() : 0;
      for (Iterator iter = iInstanceFields.iterator(); iter.hasNext();)
      {
         InstanceFieldRecord pRec = (InstanceFieldRecord) iter.next();
         if (pRec.getName().equals(aName))
            return pOffset;
         pOffset += pRec.getFieldSize();
      }
      return -1;
   }

   public int getInstanceFieldOffset (String aName) throws TinyVMException
   {
      return getApparentInstanceFieldOffset(aName) + 4;
   }

   /**
    * @return Offset relative to the start of the static state block.
    * @throws TinyVMException
    */
   public int getStaticFieldOffset (String aName) throws TinyVMException
   {
      StaticValue pValue = (StaticValue) iStaticValues.get(aName);
      if (pValue == null)
         return -1;
      return pValue.getOffset() - iBinary.iStaticState.getOffset();
   }

   public int getStaticFieldIndex (String aName)
   {
      StaticFieldRecord pRecord = (StaticFieldRecord) iStaticFields.get(aName);
      if (pRecord == null)
         return -1;
      // TBD: This indexOf call is slow
      return iBinary.iStaticFields.indexOf(pRecord);
   }

   public void storeConstants (RecordTable aConstantTable,
      RecordTable aConstantValues) throws TinyVMException
   {
      // _logger.log(Level.INFO, "Processing other constants in " + iName);

      ConstantPool pPool = iCF.getConstantPool();
      Constant[] constants = pPool.getConstantPool();
      for (int i = 0; i < constants.length; i++)
      {
         Constant pEntry = constants[i];
         if (pEntry instanceof ConstantString
            || pEntry instanceof ConstantDouble
            || pEntry instanceof ConstantFloat
            || pEntry instanceof ConstantInteger
            || pEntry instanceof ConstantLong)
         {
            ConstantRecord pRec = new ConstantRecord(pPool, pEntry);
            if (aConstantTable.indexOf(pRec) == -1)
            {
               aConstantTable.add(pRec);
               aConstantValues.add(pRec.constantValue());
            }
         }
      }
   }

   public void storeMethods (RecordTable aMethodTables,
      RecordTable aExceptionTables, HashVector aSignatures, boolean aAll)
      throws TinyVMException
   {
      // _logger.log(Level.INFO, "Processing methods in " + iName);

      Method[] methods = iCF.getMethods();
      for (int i = 0; i < methods.length; i++)
      {
         Method pMethod = methods[i];
         Signature pSignature = new Signature(pMethod.getName(), pMethod
            .getSignature());
         String meth = pMethod.getName() + ":" + pMethod.getSignature();

         if (aAll || iUseAllMethods || iUsedMethods.indexOf(meth) >= 0
            || pMethod.getName().substring(0, 1).equals("<")
            || meth.equals("run:()V"))
         {
            MethodRecord pMethodRecord = new MethodRecord(pMethod, pSignature,
               this, iBinary, aExceptionTables, aSignatures);
            iMethodTable.add(pMethodRecord);
            iMethods.put(pSignature, pMethodRecord);
         }
         else
         {
            // _logger.log(Level.INFO, "Omitting " + meth + " for class " +
            // iName);
         }
      }
      aMethodTables.add(iMethodTable);
   }

   public void storeFields (RecordTable aInstanceFieldTables,
      RecordTable aStaticFields, RecordTable aStaticState)
      throws TinyVMException
   {
      // _logger.log(Level.INFO, "Processing methods in " + iName);

      Field[] fields = iCF.getFields();
      for (int i = 0; i < fields.length; i++)
      {
         Field pField = fields[i];
         if (pField.isStatic())
         {
            StaticValue pValue = new StaticValue(pField);
            StaticFieldRecord pRec = new StaticFieldRecord(pField, this);
            String pName = pField.getName().toString();
            assert !iStaticValues.containsKey(pName): "Check: value not static";
            iStaticValues.put(pName, pValue);
            iStaticFields.put(pName, pRec);
            aStaticState.add(pValue);
            aStaticFields.add(pRec);
         }
         else
         {
            iInstanceFields.add(new InstanceFieldRecord(pField));
         }
      }
      aInstanceFieldTables.add(iInstanceFields);
   }

   public void storeCode (RecordTable aCodeSequences, boolean aPostProcess)
      throws TinyVMException
   {
      for (Iterator iter = iMethodTable.iterator(); iter.hasNext();)
      {
         MethodRecord pRec = (MethodRecord) iter.next();
         if (aPostProcess)
            pRec.postProcessCode(aCodeSequences, iCF, iBinary);
         else
            pRec.copyCode(aCodeSequences, iCF, iBinary);
      }
   }

   public static ClassRecord getClassRecord (String className, ClassPath aCP,
      Binary aBinary) throws TinyVMException
   {
      assert className != null: "Precondition: aName != null";
      assert aCP != null: "Precondition: aCP != null";
      assert aBinary != null: "Precondition: aBinary != null";
      assert className.indexOf('.') == -1: "Precondition: className is in correct form: "
         + className;

      InputStream pIn;
      try
      {
         pIn = aCP.getInputStream(className);
         assert pIn != null: "Check: pIn != null";
      }
      catch (IOException e)
      {
         throw new TinyVMException("Class " + className.replace('/', '.')
            + " (file " + className + ".class) not found in CLASSPATH " + aCP);
      }

      ClassRecord pCR = new ClassRecord();
      try
      {
         pCR.iBinary = aBinary;
         pCR.iName = className;
         InputStream pBufIn = new BufferedInputStream(pIn, 4096);
         pCR.iCF = new ClassParser(pBufIn, className).parse();
         pBufIn.close();
      }
      catch (Exception e)
      {
         // TODO refactor exceptions
         throw new TinyVMException(e.getMessage(), e);
      }

      return pCR;
   }

   public String toString ()
   {
      return iName;
   }

   public int hashCode ()
   {
      return iName.hashCode();
   }

   public boolean equals (Object aObj)
   {
      if (!(aObj instanceof ClassRecord))
         return false;
      ClassRecord pOther = (ClassRecord) aObj;
      return pOther.iName.equals(iName);
   }

   // private static final Logger _logger = Logger.getLogger("TinyVM");
}

