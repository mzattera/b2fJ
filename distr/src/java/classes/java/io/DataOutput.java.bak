/*
 * Extracted from DataOutputStream.java.
 * May contain parts of the original Sun Microsystems code.
 */
package java.io;

/**
 * @author Sven Köhler
 */
public interface DataOutput {

	/**
	 * Writes <code>len</code> bytes from the specified byte array 
	 * starting at offset <code>off</code> to the underlying output stream. 
	 * If no exception is thrown, the counter <code>written</code> is 
	 * incremented by <code>len</code>.
	 *
	 * @param		b	  the data.
	 * @param		off	the start offset in the data.
	 * @param		len	the number of bytes to write.
	 * @exception  IOException  if an I/O error occurs.
	 * @see		  java.io.FilterOutputStream#out
	 */
	void write(byte b[], int off, int len) throws IOException;

	/**
	 * Writes the specified byte (the low eight bits of the argument 
	 * <code>b</code>) to the underlying output stream. If no exception 
	 * is thrown, the counter <code>written</code> is incremented by 
	 * <code>1</code>.
	 * <p>
	 * Implements the <code>write</code> method of <code>OutputStream</code>.
	 *
	 * @param		b	the <code>byte</code> to be written.
	 * @exception  IOException  if an I/O error occurs.
	 * @see		  java.io.FilterOutputStream#out
	 */
	void write(int b) throws IOException;

	/**
	 * Writes a <code>boolean</code> to the underlying output stream as 
	 * a 1-byte value. The value <code>true</code> is written out as the 
	 * value <code>(byte)1</code>; the value <code>false</code> is 
	 * written out as the value <code>(byte)0</code>. If no exception is 
	 * thrown, the counter <code>written</code> is incremented by 
	 * <code>1</code>.
	 *
	 * @param		v	a <code>boolean</code> value to be written.
	 * @exception  IOException  if an I/O error occurs.
	 * @see		  java.io.FilterOutputStream#out
	 */
	void writeBoolean(boolean v) throws IOException;

	/**
	 * Writes out a <code>byte</code> to the underlying output stream as 
	 * a 1-byte value. If no exception is thrown, the counter 
	 * <code>written</code> is incremented by <code>1</code>.
	 *
	 * @param		v	a <code>byte</code> value to be written.
	 * @exception  IOException  if an I/O error occurs.
	 * @see		  java.io.FilterOutputStream#out
	 */
	void writeByte(int v) throws IOException;

	void writeBytes(String s) throws IOException;

	/**
	 * Writes a <code>char</code> to the underlying output stream as a 
	 * 2-byte value, high byte first. If no exception is thrown, the 
	 * counter <code>written</code> is incremented by <code>2</code>.
	 *
	 * @param		v	a <code>char</code> value to be written.
	 * @exception  IOException  if an I/O error occurs.
	 * @see		  java.io.FilterOutputStream#out
	 */
	void writeChar(int v) throws IOException;

	void writeChars(String s) throws IOException;

	/**
	 * Converts the float argument to an <code>int</code> using the 
	 * <code>floatToIntBits</code> method in class <code>Float</code>, 
	 * and then writes that <code>int</code> value to the underlying 
	 * output stream as a 4-byte quantity, high byte first. If no 
	 * exception is thrown, the counter <code>written</code> is 
	 * incremented by <code>4</code>.
	 *
	 * @param		v	a <code>float</code> value to be written.
	 * @exception  IOException  if an I/O error occurs.
	 * @see		  java.io.FilterOutputStream#out
	 * @see		  java.lang.Float#floatToIntBits(float)
	 */
	void writeFloat(float v) throws IOException;

	/**
	 * Writes an <code>int</code> to the underlying output stream as four
	 * bytes, high byte first. If no exception is thrown, the counter 
	 * <code>written</code> is incremented by <code>4</code>.
	 *
	 * @param		v	an <code>int</code> to be written.
	 * @exception  IOException  if an I/O error occurs.
	 * @see		  java.io.FilterOutputStream#out
	 */
	void writeInt(int v) throws IOException;

	/**
	 * Writes a <code>short</code> to the underlying output stream as two
	 * bytes, high byte first. If no exception is thrown, the counter 
	 * <code>written</code> is incremented by <code>2</code>.
	 *
	 * @param		v	a <code>short</code> to be written.
	 * @exception  IOException  if an I/O error occurs.
	 * @see		  java.io.FilterOutputStream#out
	 */
	void writeShort(int v) throws IOException;

	void writeUTF(String s) throws IOException;
}