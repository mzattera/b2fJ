package b2fj.io;

import java.io.PrintStream;

/**
 * This PrintStream prints to console.
 * 
 * <p>
 * This class is thread safe and it is a singleton.
 * </p>
 * 
 * @author Massimiliano "Maxi" Zattera
 * 
 * @version 1.0
 */
public final class ConsolePrintStream extends PrintStream {

	private static ConsolePrintStream instance = null;

	public static ConsolePrintStream getInstance() {
		if (instance == null) {
			synchronized (Class.lock) {
				instance = new ConsolePrintStream();
			}
		}
		return instance;
	}

	private ConsolePrintStream() {
		super(ConsoleOutputStream.getInstance());
		
		// The below is a temporary patch for  https://github.com/mzattera/b2fJ/issues/8
		out = ConsoleOutputStream.getInstance();
	}

	/**
	 * Writes a String using printf().
	 * 
	 * @param s
	 */
	private static native void putStringToStdout0(String s);

	/**
	 * All <code>print()</code> methods delegate to this one; therefore, it is
	 * enough for subclasses to override this method.
	 * 
	 * @param s The string to print.
	 */
	@Override
	protected void write(String s) {
		synchronized (out) {
			putStringToStdout0(s);
		}
	}

	/**
	 * All <code>print()</code> methods delegate to this one; therefore, it is
	 * enough for subclasses to override this method.
	 * 
	 * @param s The string to print.
	 * 
	 */
	@Override
	protected void writeln(String s) {
		synchronized (out) {
			putStringToStdout0(s);
			((ConsoleOutputStream) out).write('\n');
		}
	}
}
