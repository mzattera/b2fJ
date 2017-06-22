package js.tinyvm;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import js.common.AbstractTool;
import js.common.ToolProgressMonitor;
import js.tinyvm.io.BEByteWriter;
import js.tinyvm.io.IByteWriter;
import js.tinyvm.io.LEByteWriter;

/**
 * Tiny VM.
 */
public class TinyVMTool extends AbstractTool
{
   /**
    * Constructor.
    */
   public TinyVMTool (ToolProgressMonitor monitor)
   {
      super(monitor);
   }

   /**
    * Execute tiny vm.
    * 
    * @param classpath classpath
    * @param classes main classes to compile
    * @param all
    * @param stream output stream to write binary to
    * @param bigEndian write big endian output?
    * @throws TinyVMException
    */
   public void link (String classpath, String[] classes, boolean all,
      OutputStream stream, boolean bigEndian) throws TinyVMException
   {
      assert classpath != null: "Precondition: classpath != null";
      assert classes != null: "Precondition: classes != null";
      assert stream != null: "Precondition: stream != null";

      Binary binary = link(classpath, classes, all);
      binary.log(getProgressMonitor());
      dump(binary, stream, bigEndian);

   
   }

   /**
    * Link classes.
    * 
    * @param classpath class path
    * @param entryClassNames entry class names to link
    * @param all do not filter classes?
    * @return binary
    * @throws TinyVMException
    */
   public Binary link (String classpath, String[] entryClassNames, boolean all)
      throws TinyVMException
   {
      assert classpath != null: "Precondition: classpath != null";
      assert entryClassNames != null: "Precondition: entryClassNames != null";

      if (entryClassNames.length >= 256)
      {
         throw new TinyVMException("Too many entry classes (max is 255!)");
      }

      ClassPath computedClasspath = new ClassPath(classpath);
      for (int i = 0; i < entryClassNames.length; i++)
      {
         entryClassNames[i] = entryClassNames[i].replace('.', '/');
      }
      Binary result = Binary.createFromClosureOf(entryClassNames,
         computedClasspath, all);
      for (int i = 0; i < entryClassNames.length; i++)
      {
         if (!result.hasMain(entryClassNames[i]))
         {
            throw new TinyVMException("Class " + entryClassNames[i]
               + " doesn't have a static void main(String[]) method");
         }
      }

      assert result != null: "Postconditon: result != null";
      return result;
   }

   /**
    * Dump binary to stream.
    * 
    * @param binary binary
    * @param stream stream to write to
    * @param bigEndian use big endian encoding?
    * @throws TinyVMException
    */
   public void dump (Binary binary, OutputStream stream, boolean bigEndian)
      throws TinyVMException
   {
      assert binary != null: "Precondition: binary != null";
      assert stream != null: "Precondition: stream != null";

      try
      {
         OutputStream bufferedStream = new BufferedOutputStream(stream, 4096);
         IByteWriter byteWriter = bigEndian
            ? (IByteWriter) new BEByteWriter(bufferedStream)
            : (IByteWriter) new LEByteWriter(bufferedStream);
         binary.dump(byteWriter);
         bufferedStream.close();
      }
      catch (IOException e)
      {
         throw new TinyVMException(e.getMessage(), e);
      }
   }

   private static final Logger _logger = Logger.getLogger("TinyVM");
   static
   {
      _logger.setLevel(Level.OFF);
   }
}