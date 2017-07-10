/**
 * 
 */
package js.tinyvm.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * A byte writer implementation (write order independant part) that writes
 * directly into a c byte[].
 * 
 * This is new in b2fJ
 *
 * @author maxi
 *
 */
public abstract class CCodeWriter implements IByteWriter {

	// Max number of bytes to write in a row
	public static final int MAX_PER_ROW = 16;

	// Position of the next byte to be printed in the row.
	private int _rowPos = 0;

	// offset (number of bytes alreayd written);
	private int _offset = 0;

	// true if we must print a comma before next byte we output
	private boolean _needComma = false;

	/**
	 * Data output stream used for writing.
	 */
	private PrintStream _out = null;

	/**
	 * Constructor.
	 * 
	 * @param stream
	 *            stream to write to
	 */
	public CCodeWriter(OutputStream stream) {
		_out = new PrintStream(stream);
        _out.println("/*");
        _out.println(" *");
        _out.println(" * This is a machine-generated file; do not modify.");
        _out.println(" * This contains the Java code to be executed as an array of bytes,");
        _out.println(" * so it can be linked directly in the JVM code, instead of loading it.");
        _out.println(" */");
        _out.println();
        _out.println("#ifndef _JAVA_CODE_H_");
        _out.println("#define _JAVA_CODE_H_");
        _out.println();
        _out.println("#include \"platform_config.h\"");
        _out.println();
        _out.println("byte javaClassFileContent[] = {");
	}

	/**
	 * Closes the output stream.
	 */
	public void finalize() {
		if (_out != null) {
			try {
				_out.println("\n};");
				_out.println("#endif");
				_out.close();
			} catch (Exception e) {
			}
		}
	}

	//
	// specific write order
	//

	/*
	 * (non-Javadoc)
	 * 
	 * @see js.tinyvm.io.IByteWriter#writeU1(int)
	 */
	@Override
	public void writeU1(int aByte) throws IOException {
		write(aByte);
	}

	//
	// java write order (Big Endian)
	//

	/*
	 * (non-Javadoc)
	 * 
	 * @see js.tinyvm.io.IByteWriter#writeBoolean(boolean)
	 */
	@Override
	public void writeBoolean(boolean aBoolean) throws IOException {
		write(aBoolean ? 1 : 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see js.tinyvm.io.IByteWriter#writeByte(int)
	 */
	@Override
	public void writeByte(int aByte) throws IOException {
		write(aByte);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see js.tinyvm.io.IByteWriter#writeChar(int)
	 */
	@Override
	public void writeChar(int aChar) throws IOException {
		writeShort(aChar);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see js.tinyvm.io.IByteWriter#writeShort(int)
	 */
	@Override
	public void writeShort(int aShort) throws IOException {
		write((aShort >>> 8) & 0xFF);
		write((aShort >>> 0) & 0xFF);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see js.tinyvm.io.IByteWriter#writeInt(int)
	 */
	@Override
	public void writeInt(int aInt) throws IOException {
		write((aInt >>> 24) & 0xFF);
		write((aInt >>> 16) & 0xFF);
		write((aInt >>> 8) & 0xFF);
		write((aInt >>> 0) & 0xFF);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see js.tinyvm.io.IByteWriter#writeLong(long)
	 */
	@Override
	public void writeLong(long aLong) throws IOException {
		writeInt((int) ((aLong >>> 32) & 0xFFFF));
		writeInt((int) ((aLong >>> 0) & 0xFFFF));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see js.tinyvm.io.IByteWriter#writeFloat(float)
	 */
	@Override
	public void writeFloat(float aFloat) throws IOException {
		writeInt(Float.floatToIntBits(aFloat));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see js.tinyvm.io.IByteWriter#writeDouble(double)
	 */
	@Override
	public void writeDouble(double aDouble) throws IOException {
		writeLong(Double.doubleToLongBits(aDouble));
	}

	//
	// Direct byte writing interface
	//

	/*
	 * (non-Javadoc)
	 * 
	 * @see js.tinyvm.io.IByteWriter#write(byte[])
	 */
	@Override
	public void write(byte[] aBytes) throws IOException {
		for (byte b : aBytes)
			write(b);
	}

	/*
	 * Prints a single byte out; some magic is needed to have a nice formatting.
	 */
	@Override
	public void write(int aByte) throws IOException {
		if (_needComma)
			_out.print(", ");
		if (_rowPos >= MAX_PER_ROW) {
			_out.println();
			_rowPos = 0;
		}
		if (_rowPos == 0)
			_out.print('\t');
		_out.print(aByte);
		_needComma = true;
		_rowPos++;
		_offset++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see js.tinyvm.io.IByteWriter#offset()
	 */
	@Override
	public int offset() {
		return _offset;
	}
}
