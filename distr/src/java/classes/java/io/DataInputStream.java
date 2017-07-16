package java.io;

/**
 * Reads java data types transmitted as bytes over an InputStream.
 * 
 * @author Sven Köhler
 */
public class DataInputStream extends FilterInputStream implements DataInput {
	public DataInputStream(InputStream in) {
		super(in);
	}

	private int readByte0() throws IOException {
		int ch = in.read();
		if (ch < 0)
			throw new EOFException();
		// actually, InputStream.read() should always return values from 0 to 255
		return ch & 0xFF;
	}

	public final boolean readBoolean() throws IOException {
		return (readByte0() != 0);
	}

	public final byte readByte() throws IOException {
		return (byte) readByte0();
	}

	public final char readChar() throws IOException {
		int x = readByte0();
		x = (x << 8) | readByte0();
		return (char) x;
	}

	public final float readFloat() throws IOException {
		int x = this.readInt();
		return Float.intBitsToFloat(x);
	}

	public final void readFully(byte b[]) throws IOException {
		readFully(b, 0, b.length);
	}

	public final void readFully(byte b[], int off, int len) throws IOException {
		if (len < 0)
			// TODO is this correct?
			throw new IOException();

		while (len > 0) {
			int count = in.read(b, off, len);
			if (count < 0)
				throw new EOFException();

			off += count;
			len -= count;
		}
	}

	public final int readInt() throws IOException {
		int x = readByte0();
		x = (x << 8) | readByte0();
		x = (x << 8) | readByte0();
		x = (x << 8) | readByte0();
		return x;
	}

	public final short readShort() throws IOException {
		int x = readByte0();
		x = (x << 8) | readByte0();
		return (short) x;
	}

	public final int readUnsignedByte() throws IOException {
		int x = readByte0();
		return x;
	}

	public final int readUnsignedShort() throws IOException {
		int x = readByte0();
		x = (x << 8) | readByte0();
		return x;
	}

	public final String readUTF() throws IOException {
		return readUTF(this);
	}

	public static final String readUTF(DataInput in) throws IOException {
		int utflen = in.readUnsignedShort();
		StringBuilder sb = new StringBuilder(utflen / 3);

		int count = 0;
		while (count < utflen) {
			int c1 = in.readUnsignedByte();

			if (c1 < 0x80) {
				// ASCII character
				count++;
				sb.append((char) c1);
			} else {
				int t1 = c1 >> 4;
				if ((t1 == 0x0C) || (t1 == 0x0D)) {
					// 110x xxxx 10xx xxxx
					count += 2;
					if (count > utflen)
						throw new IOException("malformed input: partial character at end");

					int c2 = in.readUnsignedByte();
					if ((c2 & 0xC0) != 0x80)
						throw new IOException("malformed input");

					sb.append((char) (((c1 & 0x1F) << 6) | (c2 & 0x3F)));
				} else if (t1 == 0x0E) {
					// 1110 xxxx 10xx xxxx 10xx xxxx
					count += 3;
					if (count > utflen)
						throw new IOException("malformed input: partial character at end");

					int c2 = in.readUnsignedByte();
					int c3 = in.readUnsignedByte();

					if (((c2 & 0xC0) != 0x80) || ((c3 & 0xC0) != 0x80))
						throw new IOException("malformed input");

					sb.append((char) (((c1 & 0x0F) << 12) | ((c2 & 0x3F) << 6) | (c3 & 0x3F)));
				} else {
					throw new IOException("malformed input");
				}
			}
		}

		return sb.toString();
	}

	public final int skipBytes(int n) throws IOException {
		return (int) this.in.skip(n);
	}

	/**
	 * Deprecated. This method assumes ISO-8859-1 encoding and does only recognize
	 * \n and \r\n line-endings.
	 * 
	 * @deprecated broken in various ways, use BufferedReader.readLine instead
	 */
	@Deprecated
	public final String readLine() throws IOException {
		StringBuilder strb = new StringBuilder();

		// MISSING readLine() does not recognize \r line endings

		while (true) {
			int c = this.read();

			// catch EOF
			if (c < 0) {
				if (strb.length() == 0)
					return null;

				break;
			}

			if (c == '\n') {
				int p = strb.length() - 1;
				if (p >= 0 && strb.charAt(p) == '\r')
					return strb.substring(0, p);

				break;
			}

			strb.append((char) c);
		}

		return strb.toString();
	}
}