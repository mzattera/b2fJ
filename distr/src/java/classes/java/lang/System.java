package java.lang;

import java.io.InputStream;
import java.io.PrintStream;

import b2fj.io.ConsoleInputStream;
import b2fj.io.ConsolePrintStream;

/**
 * System utilities.
 */
public final class System {
	private System() {
	}

	/**
	 * Copies one array to another.
	 */
	// TODO: Make it native
	// public static native void arraycopy (Object src, int srcOffset, Object dest,
	// int destOffset, int length);
	public static void arraycopy(boolean[] src, int srcOffset, boolean[] dest, int destOffset, int length) {
		for (int i = 0; i < length; i++)
			dest[i + destOffset] = src[i + srcOffset];
	}

	/**
	 * Copies one array to another.
	 */
	// TODO: Make it native
	// public static native void arraycopy (Object src, int srcOffset, Object dest,
	// int destOffset, int length);
	public static void arraycopy(byte[] src, int srcOffset, byte[] dest, int destOffset, int length) {
		for (int i = 0; i < length; i++)
			dest[i + destOffset] = src[i + srcOffset];
	}

	/**
	 * Copies one array to another.
	 */
	// TODO: Make it native
	// public static native void arraycopy (Object src, int srcOffset, Object dest,
	// int destOffset, int length);
	public static void arraycopy(short[] src, int srcOffset, short[] dest, int destOffset, int length) {
		for (int i = 0; i < length; i++)
			dest[i + destOffset] = src[i + srcOffset];
	}

	/**
	 * Copies one array to another.
	 */
	// TODO: Make it native
	// public static native void arraycopy (Object src, int srcOffset, Object dest,
	// int destOffset, int length);
	public static void arraycopy(char[] src, int srcOffset, char[] dest, int destOffset, int length) {
		for (int i = 0; i < length; i++)
			dest[i + destOffset] = src[i + srcOffset];
	}

	/**
	 * Copies one array to another.
	 */
	// TODO: Make it native
	// public static native void arraycopy (Object src, int srcOffset, Object dest,
	// int destOffset, int length);
	public static void arraycopy(int[] src, int srcOffset, int[] dest, int destOffset, int length) {
		for (int i = 0; i < length; i++)
			dest[i + destOffset] = src[i + srcOffset];
	}

	/**
	 * Copies one array to another.
	 */
	// TODO: Make it native
	// public static native void arraycopy (Object src, int srcOffset, Object dest,
	// int destOffset, int length);
	public static void arraycopy(long[] src, int srcOffset, long[] dest, int destOffset, int length) {
		for (int i = 0; i < length; i++)
			dest[i + destOffset] = src[i + srcOffset];
	}

	/**
	 * Copies one array to another.
	 */
	// TODO: Make it native
	// public static native void arraycopy (Object src, int srcOffset, Object dest,
	// int destOffset, int length);
	public static void arraycopy(float[] src, int srcOffset, float[] dest, int destOffset, int length) {
		for (int i = 0; i < length; i++)
			dest[i + destOffset] = src[i + srcOffset];
	}

	/**
	 * Copies one array to another.
	 */
	// TODO: Make it native
	// public static native void arraycopy (Object src, int srcOffset, Object dest,
	// int destOffset, int length);
	public static void arraycopy(double[] src, int srcOffset, double[] dest, int destOffset, int length) {
		for (int i = 0; i < length; i++)
			dest[i + destOffset] = src[i + srcOffset];
	}

	/**
	 * Copies one array to another.
	 */
	// TODO: Make it native
	// public static native void arraycopy (Object src, int srcOffset, Object dest,
	// int destOffset, int length);
	public static void arraycopy(Object[] src, int srcOffset, Object[] dest, int destOffset, int length) {
		for (int i = 0; i < length; i++)
			dest[i + destOffset] = src[i + srcOffset];
	}

	/**
	 * Terminate the application.
	 */
	public static void exit(int code) {
		Runtime.getRuntime().exit(code);
	}

	/**
	 * Current time expressed in milliseconds. In the RCX, this is the number of
	 * milliseconds since the RCX has been on. (In Java, this would be since January
	 * 1st, 1970).
	 */
	public static native long currentTimeMillis();

	/**
	 * Returns <code>null</code>. In leJOS, there are no system properties. So this
	 * function returns <code>null</code>.
	 * 
	 * @param name name of the system property
	 * @return <code>null</code>
	 */
	public static String getProperty(String name) {
		return null;
	}

	/**
	 * Returns <code>def</code>. In leJOS, there are no system properties. So this
	 * function returns <code>def</code>.
	 * 
	 * @param name name of the system property
	 * @param def  default value that is returns if system property doesn't exist
	 * @return <code>def</code>
	 */
	public static String getProperty(String name, String def) {
		return def;
	}

	/**
	 * Get the singleton instance of Runtime.
	 */
	public static Runtime getRuntime() {
		return Runtime.getRuntime();
	}

	public static InputStream in = ConsoleInputStream.getInstance();

	/**
	 * Redirect System.in
	 * 
	 * @param out an InputStream
	 */
	public static void setIn(InputStream in) {
		System.in = in;
	}

	public static PrintStream out = ConsolePrintStream.getInstance();

	/**
	 * Redirect System.out
	 * 
	 * @param out a PrintStream
	 */
	public static void setOut(PrintStream out) {
		System.out = out;
	}

	public static PrintStream err = ConsolePrintStream.getInstance();

	/**
	 * Redirect System.err
	 * 
	 * @param err a PrintStream
	 */
	public static void setErr(PrintStream err) {
		System.err = err;
	}
}
