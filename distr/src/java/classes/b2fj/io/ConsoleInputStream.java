/*
 * @(#)InputStream.java	1.36 00/02/02
 *
 * Copyright 1994-2000 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */

package b2fj.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * This InputStream reads characters from console.
 * 
 * <p>
 * This class is thread safe and it is a singleton.
 * </p>
 * 
 * @author Massimiliano "Maxi" Zattera
 * 
 * @version 1.0
 */
public class ConsoleInputStream extends InputStream {

	private static final ConsoleInputStream instance = new ConsoleInputStream();

	public static ConsoleInputStream getInstance() {
		return instance;
	}

	private ConsoleInputStream() {
	}

	/**
	 * Reads the next byte of data from the console.
	 *
	 * @return the next byte of data, or <code>-1</code> if the end of the stream is
	 *         reached.
	 */
	private native int readCharFromStdin0();

	/**
	 * Reads the next byte of data from the input stream. The value byte is returned
	 * as an <code>int</code> in the range <code>0</code> to <code>255</code>. If no
	 * byte is available because the end of the stream has been reached, the value
	 * <code>-1</code> is returned. This method blocks until input data is
	 * available, the end of the stream is detected, or an exception is thrown.
	 *
	 * <p>
	 * A subclass must provide an implementation of this method.
	 *
	 * @return the next byte of data, or <code>-1</code> if the end of the stream is
	 *         reached.
	 * @exception IOException if an I/O error occurs.
	 */
	@Override
	public synchronized int read() {
		return readCharFromStdin0();
	}

	/**
	 * Reads some number of bytes from the input stream and stores them into the
	 * buffer array <code>b</code>. The number of bytes actually read is returned as
	 * an integer. This method blocks until input data is available, end of file is
	 * detected, or an exception is thrown.
	 *
	 * <p>
	 * If <code>b</code> is <code>null</code>, a <code>NullPointerException</code>
	 * is thrown. If the length of <code>b</code> is zero, then no bytes are read
	 * and <code>0</code> is returned; otherwise, there is an attempt to read at
	 * least one byte. If no byte is available because the stream is at end of file,
	 * the value <code>-1</code> is returned; otherwise, at least one byte is read
	 * and stored into <code>b</code>.
	 *
	 * <p>
	 * The first byte read is stored into element <code>b[0]</code>, the next one
	 * into <code>b[1]</code>, and so on. The number of bytes read is, at most,
	 * equal to the length of <code>b</code>. Let <i>k</i> be the number of bytes
	 * actually read; these bytes will be stored in elements <code>b[0]</code>
	 * through <code>b[</code><i>k</i><code>-1]</code>, leaving elements
	 * <code>b[</code><i>k</i><code>]</code> through <code>b[b.length-1]</code>
	 * unaffected.
	 *
	 * <p>
	 * If the first byte cannot be read for any reason other than end of file, then
	 * an <code>IOException</code> is thrown. In particular, an
	 * <code>IOException</code> is thrown if the input stream has been closed.
	 *
	 * <p>
	 * The <code>read(b)</code> method for class <code>InputStream</code> has the
	 * same effect as:
	 * 
	 * <pre>
	 * <code> read(b, 0, b.length) </code>
	 * </pre>
	 *
	 * @param b the buffer into which the data is read.
	 * @return the total number of bytes read into the buffer, or <code>-1</code> is
	 *         there is no more data because the end of the stream has been reached.
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
	public synchronized int read(byte b[]) {
		try {
			return super.read(b, 0, b.length);
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Reads up to <code>len</code> bytes of data from the input stream into an
	 * array of bytes. An attempt is made to read as many as <code>len</code> bytes,
	 * but a smaller number may be read, possibly zero. The number of bytes actually
	 * read is returned as an integer.
	 *
	 * <p>
	 * This method blocks until input data is available, end of file is detected, or
	 * an exception is thrown.
	 *
	 * <p>
	 * If <code>b</code> is <code>null</code>, a <code>NullPointerException</code>
	 * is thrown.
	 *
	 * <p>
	 * If <code>off</code> is negative, or <code>len</code> is negative, or
	 * <code>off+len</code> is greater than the length of the array <code>b</code>,
	 * then an <code>IndexOutOfBoundsException</code> is thrown.
	 *
	 * <p>
	 * If <code>len</code> is zero, then no bytes are read and <code>0</code> is
	 * returned; otherwise, there is an attempt to read at least one byte. If no
	 * byte is available because the stream is at end of file, the value
	 * <code>-1</code> is returned; otherwise, at least one byte is read and stored
	 * into <code>b</code>.
	 *
	 * <p>
	 * The first byte read is stored into element <code>b[off]</code>, the next one
	 * into <code>b[off+1]</code>, and so on. The number of bytes read is, at most,
	 * equal to <code>len</code>. Let <i>k</i> be the number of bytes actually read;
	 * these bytes will be stored in elements <code>b[off]</code> through
	 * <code>b[off+</code><i>k</i><code>-1]</code>, leaving elements
	 * <code>b[off+</code><i>k</i><code>]</code> through <code>b[off+len-1]</code>
	 * unaffected.
	 *
	 * <p>
	 * In every case, elements <code>b[0]</code> through <code>b[off]</code> and
	 * elements <code>b[off+len]</code> through <code>b[b.length-1]</code> are
	 * unaffected.
	 *
	 * <p>
	 * If the first byte cannot be read for any reason other than end of file, then
	 * an <code>IOException</code> is thrown. In particular, an
	 * <code>IOException</code> is thrown if the input stream has been closed.
	 *
	 * <p>
	 * The <code>read(b,</code> <code>off,</code> <code>len)</code> method for class
	 * <code>InputStream</code> simply calls the method <code>read()</code>
	 * repeatedly. If the first such call results in an <code>IOException</code>,
	 * that exception is returned from the call to the <code>read(b,</code>
	 * <code>off,</code> <code>len)</code> method. If any subsequent call to
	 * <code>read()</code> results in a <code>IOException</code>, the exception is
	 * caught and treated as if it were end of file; the bytes read up to that point
	 * are stored into <code>b</code> and the number of bytes read before the
	 * exception occurred is returned. Subclasses are encouraged to provide a more
	 * efficient implementation of this method.
	 *
	 * @param b   the buffer into which the data is read.
	 * @param off the start offset in array <code>b</code> at which the data is
	 *            written.
	 * @param len the maximum number of bytes to read.
	 * @return the total number of bytes read into the buffer, or <code>-1</code> if
	 *         there is no more data because the end of the stream has been reached.
	 * @see java.io.InputStream#read()
	 */
	public synchronized int read(byte b[], int off, int len) {
		try {
			return super.read(b, off, len);
		} catch (Exception e) {
			return -1;
		}
	}
}
