package js.tinyvm;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for class paths.
 */
public interface IClassPath
{
   /**
    * Get input stream with the given class byte code.
    * 
    * @param className name of class with '/' as separator
    * @throws IOException if class could not be found in class path
    */
   public InputStream getInputStream (String className) throws IOException;
}