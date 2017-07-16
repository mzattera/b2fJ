package java.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Writer;

/**
 * Properties class, used to store properties using a key, and retrieving
 * properties with a key. String for both. Integrates well with File for saving
 * to file system.
 * 
 * @author BB
 *
 */
public class Properties extends Hashtable<Object, Object> {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 4112578634029874840L;

	/**
	 * A property list that contains default values for any keys not found in this
	 * property list.
	 *
	 * @serial
	 */
	protected Properties defaults;

	public Properties() {
		this(null);
	}

	public Properties(Properties defaults) {
		this.defaults = defaults;
	}

	public synchronized void setProperty(String key, String value) {
		put(key, value);
	}

	private static void writeComments(BufferedWriter bw, String comments) throws IOException {
		bw.write("#");
		int len = comments.length();
		int current = 0;
		int last = 0;
		char[] uu = new char[6];
		uu[0] = '\\';
		uu[1] = 'u';
		while (current < len) {
			char c = comments.charAt(current);
			if (c > '\u00ff' || c == '\n' || c == '\r') {
				if (last != current)
					bw.write(comments.substring(last, current));
				if (c > '\u00ff') {
					uu[2] = toHex((c >> 12) & 0xf);
					uu[3] = toHex((c >> 8) & 0xf);
					uu[4] = toHex((c >> 4) & 0xf);
					uu[5] = toHex(c & 0xf);
					bw.write(new String(uu, 0, uu.length));
				} else {
					bw.newLine();
					if (c == '\r' && current != len - 1 && comments.charAt(current + 1) == '\n') {
						current++;
					}
					if (current == len - 1
							|| (comments.charAt(current + 1) != '#' && comments.charAt(current + 1) != '!'))
						bw.write("#");
				}
				last = current + 1;
			}
			current++;
		}
		if (last != current)
			bw.write(comments.substring(last, current));
		bw.newLine();
	}

	public synchronized void load(InputStream inStream) throws IOException {
		load0(new LineReader(inStream));
	}

	private void load0(LineReader lr) throws IOException {
		char[] convtBuf = new char[256]; // Changed size from 1024
		int limit;
		int keyLen;
		int valueStart;
		char c;
		boolean hasSep;
		boolean precedingBackslash;

		while ((limit = lr.readLine()) >= 0) {
			c = 0;
			keyLen = 0;
			valueStart = limit;
			hasSep = false;

			// System.out.println("line=<" + new String(lineBuf, 0, limit) + ">");
			precedingBackslash = false;
			while (keyLen < limit) {
				c = lr.lineBuf[keyLen];
				// need check if escaped.
				if ((c == '=' || c == ':') && !precedingBackslash) {
					valueStart = keyLen + 1;
					hasSep = true;
					break;
				} else if ((c == ' ' || c == '\t' || c == '\f') && !precedingBackslash) {
					valueStart = keyLen + 1;
					break;
				}
				if (c == '\\') {
					precedingBackslash = !precedingBackslash;
				} else {
					precedingBackslash = false;
				}
				keyLen++;
			}
			while (valueStart < limit) {
				c = lr.lineBuf[valueStart];
				if (c != ' ' && c != '\t' && c != '\f') {
					if (!hasSep && (c == '=' || c == ':')) {
						hasSep = true;
					} else {
						break;
					}
				}
				valueStart++;
			}
			String key = loadConvert(lr.lineBuf, 0, keyLen, convtBuf);
			String value = loadConvert(lr.lineBuf, valueStart, limit - valueStart, convtBuf);
			put(key, value);
		}
	}

	/*
	 * NOTE: Buffer sizes below were decreased as marked.
	 */
	class LineReader {
		public LineReader(InputStream inStream) {
			this.inStream = inStream;
			inByteBuf = new byte[256]; // Changed size from 8192
		}

		byte[] inByteBuf;
		char[] inCharBuf;
		char[] lineBuf = new char[128]; // Changed size from 1024
		int inLimit = 0;
		int inOff = 0;
		InputStream inStream;

		int readLine() throws IOException {
			int len = 0;
			char c = 0;

			boolean skipWhiteSpace = true;
			boolean isCommentLine = false;
			boolean isNewLine = true;
			boolean appendedLineBegin = false;
			boolean precedingBackslash = false;
			boolean skipLF = false;

			while (true) {
				if (inOff >= inLimit) {
					inLimit = inStream.read(inByteBuf);
					inOff = 0;
					if (inLimit <= 0) {
						if (len == 0 || isCommentLine) {
							return -1;
						}
						return len;
					}
				}
				if (inStream != null) {
					// The line below is equivalent to calling a
					// ISO8859-1 decoder.
					c = (char) (0xff & inByteBuf[inOff++]);
				} else {
					c = inCharBuf[inOff++];
				}
				if (skipLF) {
					skipLF = false;
					if (c == '\n') {
						continue;
					}
				}
				if (skipWhiteSpace) {
					if (c == ' ' || c == '\t' || c == '\f') {
						continue;
					}
					if (!appendedLineBegin && (c == '\r' || c == '\n')) {
						continue;
					}
					skipWhiteSpace = false;
					appendedLineBegin = false;
				}
				if (isNewLine) {
					isNewLine = false;
					if (c == '#' || c == '!') {
						isCommentLine = true;
						continue;
					}
				}

				if (c != '\n' && c != '\r') {
					lineBuf[len++] = c;
					if (len == lineBuf.length) {
						int newLength = lineBuf.length * 2;
						if (newLength < 0) {
							newLength = Integer.MAX_VALUE;
						}
						char[] buf = new char[newLength];
						System.arraycopy(lineBuf, 0, buf, 0, lineBuf.length);
						lineBuf = buf;
					}
					// flip the preceding backslash flag
					if (c == '\\') {
						precedingBackslash = !precedingBackslash;
					} else {
						precedingBackslash = false;
					}
				} else {
					// reached EOL
					if (isCommentLine || len == 0) {
						isCommentLine = false;
						isNewLine = true;
						skipWhiteSpace = true;
						len = 0;
						continue;
					}
					if (inOff >= inLimit) {
						inLimit = inStream.read(inByteBuf);
						inOff = 0;
						if (inLimit <= 0) {
							return len;
						}
					}
					if (precedingBackslash) {
						len -= 1;
						// skip the leading whitespace characters in following line
						skipWhiteSpace = true;
						appendedLineBegin = true;
						precedingBackslash = false;
						if (c == '\r') {
							skipLF = true;
						}
					} else {
						return len;
					}
				}
			}
		}
	}

	/*
	 * Converts encoded &#92;uxxxx to unicode chars and changes special saved chars
	 * to their original forms
	 */
	private String loadConvert(char[] in, int off, int len, char[] convtBuf) {
		if (convtBuf.length < len) {
			int newLen = len * 2;
			if (newLen < 0) {
				newLen = Integer.MAX_VALUE;
			}
			convtBuf = new char[newLen];
		}
		char aChar;
		char[] out = convtBuf;
		int outLen = 0;
		int end = off + len;

		while (off < end) {
			aChar = in[off++];
			if (aChar == '\\') {
				aChar = in[off++];
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = in[off++];
						if ((aChar >= '0') & (aChar <= '9')) {
							value = (value << 4) + aChar - '0';
						} else if ((aChar >= 'a') & (aChar <= 'f')) {
							value = (value << 4) + 10 + aChar - 'a';
						} else if ((aChar >= 'A') & (aChar <= 'F')) {
							value = (value << 4) + 10 + aChar - 'A';
						} else {
							throw new IllegalArgumentException();
						}
					}
					out[outLen++] = (char) value;
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					out[outLen++] = aChar;
				}
			} else {
				out[outLen++] = aChar;
			}
		}
		return new String(out, 0, outLen);
	}

	/*
	 * Converts unicodes to encoded &#92;uxxxx and escapes special characters with a
	 * preceding slash
	 */
	private String saveConvert(String theString, boolean escapeSpace, boolean escapeUnicode) {
		int len = theString.length();
		int bufLen = len * 2;
		if (bufLen < 0) {
			bufLen = Integer.MAX_VALUE;
		}
		StringBuffer outBuffer = new StringBuffer(bufLen);

		for (int x = 0; x < len; x++) {
			char aChar = theString.charAt(x);
			// Handle common case first, selecting largest block that
			// avoids the specials below
			if ((aChar > 61) && (aChar < 127)) {
				if (aChar == '\\') {
					outBuffer.append('\\');
					outBuffer.append('\\');
					continue;
				}
				outBuffer.append(aChar);
				continue;
			}
			if (aChar == ' ') {
				if (x == 0 || escapeSpace)
					outBuffer.append('\\');
				outBuffer.append(' ');
			} else if (aChar == '\t') {
				outBuffer.append('\\');
				outBuffer.append('t');
			} else if (aChar == '\n') {
				outBuffer.append('\\');
				outBuffer.append('n');
			} else if (aChar == '\r') {
				outBuffer.append('\\');
				outBuffer.append('r');
			} else if (aChar == '\f') {
				outBuffer.append('\\');
				outBuffer.append('f');
			} else if ((aChar == '=') || (aChar == ':') || (aChar == '#') || (aChar == '!')) {
				outBuffer.append('\\');
				outBuffer.append(aChar);
			} else {
				if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode) {
					outBuffer.append('\\');
					outBuffer.append('u');
					outBuffer.append(toHex((aChar >> 12) & 0xF));
					outBuffer.append(toHex((aChar >> 8) & 0xF));
					outBuffer.append(toHex((aChar >> 4) & 0xF));
					outBuffer.append(toHex(aChar & 0xF));
				} else {
					outBuffer.append(aChar);
				}
			}
		}
		return outBuffer.toString();
	}

	public void store(Writer out, String comments) throws IOException {
		store0(new BufferedWriter(out), comments, true);
	}

	private void store0(BufferedWriter bw, String comments, boolean escUnicode) throws IOException {
		if (comments != null) {
			writeComments(bw, comments);
		}
		// bw.write("#" + new Date().toString());
		// bw.newLine();
		synchronized (this) {
			for (Enumeration<Object> e = keys(); e.hasMoreElements();) {
				String key = (String) e.nextElement();
				String val = (String) get(key);
				key = saveConvert(key, true, escUnicode);
				/*
				 * No need to escape embedded and trailing spaces for value, hence pass false to
				 * flag.
				 */
				val = saveConvert(val, false, escUnicode);
				bw.write(key + "=" + val);
				bw.newLine();
			}
		}
		bw.flush();
	}

	public String getProperty(String key) {
		Object oval = super.get(key);
		String sval = (oval instanceof String) ? (String) oval : null;
		return ((sval == null) && (defaults != null)) ? defaults.getProperty(key) : sval;
	}

	public String getProperty(String key, String defaultValue) {
		String val = getProperty(key);
		return (val == null) ? defaultValue : val;
	}

	public Enumeration<?> propertyNames() {
		Hashtable<Object, Object> h = new Hashtable<Object, Object>();
		enumerate(h);
		return h.keys();
	}

	public void list(PrintStream out) {
		out.println("- properties -");
		Hashtable<Object, Object> h = new Hashtable<Object, Object>();
		enumerate(h);
		for (Enumeration<Object> e = h.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String val = (String) h.get(key);
			if (val.length() > 40) {
				val = val.substring(0, 37) + "...";
			}
			out.println(key + "=" + val);
		}
	}

	private synchronized void enumerate(Hashtable<Object, Object> h) {
		if (defaults != null)
			defaults.enumerate(h);

		for (Enumeration<Object> e = keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			h.put(key, get(key));
		}
	}

	// private synchronized void enumerateStringProperties(Hashtable<Object,Object>
	// h) {
	// if (defaults != null) {
	// defaults.enumerateStringProperties(h);
	// }
	// for (Enumeration<Object> e = keys() ; e.hasMoreElements() ;) {
	// Object k = e.nextElement();
	// Object v = get(k);
	// if (k instanceof String && v instanceof String) {
	// h.put(k, v);
	// }
	// }
	// }

	private static char toHex(int nibble) {
		return hexDigit[(nibble & 0xF)];
	}

	/** A table of hex digits */
	private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };
}