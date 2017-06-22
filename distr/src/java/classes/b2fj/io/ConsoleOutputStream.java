package b2fj.io;

import java.io.*;

/**
 * This outputs chars to the screen.
 *
 * @author  Massimiliano Zattera
 * @version 0.1
 */
public class ConsoleOutputStream extends OutputStream {

    private static final char NEW_LINE = '\n';

    private native void putCharToStdout0 (int b);

    private native void putStringToStdout0 (String s);

    /**
     * Writes the specified byte to this output stream. The general
     * contract for <code>write</code> is that one byte is written
     * to the output stream. The byte to be written is the eight
     * low-order bits of the argument <code>b</code>. The 24
     * high-order bits of <code>b</code> are ignored.
     * <p>
     * Subclasses of <code>OutputStream</code> must provide an
     * implementation for this method.
     *
     * @param      b   the <code>byte</code>.
     * @exception  IOException  if an I/O error occurs. In particular,
     *             an <code>IOException</code> may be thrown if the
     *             output stream has been closed.
     */
    public synchronized void write(int b) {
        putCharToStdout0(b);
    }

    /**
     * Writes a newline character
     * to the underlying output stream.
     */
    public synchronized void println() {
   		write(NEW_LINE);
    }

    /**
     * Writes a string to the underlying output stream.
     *
     * @param s the string to print
     */
    public synchronized void print(String s) {
    	putStringToStdout0(s);
    }

    /**
     * Writes a string to the underlying output stream.
     *
     * @param s the string to print
     */
    public synchronized void println(String s) {
    	putStringToStdout0(s);
   		write(NEW_LINE);
    }
}
