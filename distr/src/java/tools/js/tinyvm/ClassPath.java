package js.tinyvm;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class path.
 */
public class ClassPath implements IClassPath
{
   /**
    * BCEL class path.
    */
   private org.apache.bcel.util.ClassPath _classPath;

   /**
    * Constructor.
    * 
    * @param classPath string class path separated with File.pathSeparatorChar
    */
   public ClassPath (String classPath)
   {
      _classPath = new org.apache.bcel.util.ClassPath(classPath);
   }

   /*
    * (non-Javadoc)
    * 
    * @see js.tinyvm.IClassPath#getInputStream(java.lang.String)
    */
   public InputStream getInputStream (String className) throws IOException
   {
      return _classPath.getClassFile(className).getInputStream();
   }
   
   public String toString() {
	   return _classPath.toString();
   }
}