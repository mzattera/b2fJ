package java.lang;

import b2fj.io.ConsoleOutputStream;
import java.io.PrintStream;

/**
 * System utilities.
 */
public final class System
{
//  public static final PrintStream out = new PrintStream(new ConsoleOutputStream());
  public static final ConsoleOutputStream out = new ConsoleOutputStream();

  private System() {}

  /**
   * Copies one array to another.
   */
  //public static native void arraycopy (Object src, int srcOffset, Object dest, int destOffset, int length);
  static void arraycopy (char[] src, int srcOffset, char[] dest, int destOffset, int length)
  {
    for (int i = 0; i < length; i++)
      dest[i + destOffset] = src[i + srcOffset];
  }

  /**
   * Terminate the application.
   */
  public static native void exit(int code);

  /**
   * Current time expressed in milliseconds. In the RCX, this is the number
   * of milliseconds since the RCX has been on. (In Java, this would
   * be since January 1st, 1970).
   */
  public static native long currentTimeMillis();

  /**
   * Get the singleton instance of Runtime.
   */
  public static Runtime getRuntime() {
  	return Runtime.getRuntime();
  }
}

