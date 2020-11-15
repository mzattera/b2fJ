/**
 * 
 */
package b2fj.util;

/**
 * THis is an utility class to do some debugging.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class Debug {

	/**
	 * Writes a String using printf(). Use this to bypass console synchronized
	 * output.
	 * 
	 * @param s
	 */
	private static native void putStringToStdout0(String s);

	/**
	 * Writes a String using printf(). Use this to bypass console synchronized
	 * output.
	 * 
	 * @param s
	 */
	public static void print(String s) {
		putStringToStdout0(s);
	}

	/**
	 * Causes JVM to dump object data.
	 * 
	 * @param o
	 */
	private static native void dump0(Object o);

	/**
	 * Causes JVM to dump object data.
	 * 
	 * @param o
	 */
	public static void dump(Object o) {
		putStringToStdout0("* DUMP *\n");
		if (o != null) {
			putStringToStdout0("DtaAdr: " + o.hashCode() + "\n");
			dump0(o);
		} else {
			putStringToStdout0("null\n");
		}
		putStringToStdout0("********\n");
	}

	/**
	 * Exits on error with error code -1.
	 * 
	 * @param msg Error message (can be null).
	 */
	public static void exit (String msg) {
		exit (msg, -1);
	}	

	/**
	 * Exits on error.
	 * 
	 * @param msg Error message (can be null).
	 * @param error Error code being returned upon exit.
	 */
	public static void exit (String msg, int error) {
		putStringToStdout0(msg);
		System.exit(error);
	}	
}
