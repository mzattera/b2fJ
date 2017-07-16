package java.lang;

/**
 * An immutable string of characters.
 */
public final class String implements CharSequence, Comparable<String>
{
	// NOTE: The state of this class is mapped to
	// native code (see vmsrc/classes.h).

	final char[] characters;

	public String()
	{
		this(0);
	}

	private String(int len)
	{
		characters = new char[len];
	}

	/**
	 * Create a String from a byte array
	 * 
	 * @param b the byte array
	 */
	public String(byte[] b)
	{
		this(b, 0, b.length);
	}

	/**
	 * Create a String from a byte array
	 * 
	 * @param b the byte array
	 * @deprecated
	 */
	@Deprecated
	public String(byte[] b, int hibyte)
	{
		this(b, hibyte, 0, b.length);
	}

	/**
	 * Create a String from a byte array
	 * 
	 * @param b the byte array
	 */
	public String(byte[] b, int off, int len)
	{
		this(len);
		for (int i = 0; i < len; i++)
			characters[i] = (char)(b[off + i] & 0xFF);
	}

	/**
	 * Create a String from a byte array
	 * 
	 * @param b the byte array
	 * @deprecated
	 */
	@Deprecated
	public String(byte[] b, int hibyte, int off, int len)
	{
		this(len);
		for (int i = 0; i < len; i++)
			characters[i] = (char)(b[off + i] & 0xFF | (hibyte << 8));
	}

	/**
	 * Create a String from a byte array
	 * 
	 * @param b the byte array
	 * @param charset ignored - assumed to be US ASCII
	 */
	public String(byte[] b, int off, int len, String charset)
	{
		this(b, off, len);
	}

	/**
	 * Create a String from a byte array
	 * 
	 * @param b the byte array
	 * @param charset ignored - assumed to be US ASCII
	 */
	public String(byte[] b, String charset)
	{
		this(b, 0, b.length, charset);
	}

	/**
	 * Create a String from a character array
	 * 
	 * @param c the character array
	 */
	public String(char[] c)
	{
		this(c, 0, c.length);
	}

	/**
	 * Create a String from a character array.
	 * 
	 * @param c the character array
	 * @param off the offset - usually 0
	 * @param len the length of the String - must not be greater than c.length
	 */
	public String(char[] c, int off, int len)
	{
		this(len);
		System.arraycopy(c, off, characters, 0, len);
	}
	
	public String(int[] codePoints, int off, int len)
	{
		int clen = 0;
		for (int i=0; i<len; i++)
			clen += codePoints[off + i] < Character.MIN_SUPPLEMENTARY_CODE_POINT ? 1 : 2;
		this.characters = new char[clen];
		clen = 0;
		for (int i=0; i<len; i++)
		{
			int cp = codePoints[off + i];
			clen += Character.toChars(cp, this.characters, clen);
		}
	}
	
	public String(String s)
	{
		this.characters = s.characters;
	}

	public String(StringBuffer sb)
	{
		this(sb.toString());
	}

	public String(StringBuilder sb)
	{
		this(sb.toString());
	}

	/**
	 * Return the character at the given index
	 * 
	 * @return the characters at the given index
	 */
	public char charAt(int index)
	{
		if (index > characters.length)
			throw new StringIndexOutOfBoundsException(index);

		return characters[index];
	}
	
	public int codePointAt(int index)
	{	
		char c1 = characters[index];
		if (c1 >= Character.MIN_HIGH_SURROGATE && c1 <= Character.MAX_HIGH_SURROGATE && index + 1 < characters.length)
		{
			char c2 = characters[index + 1];
			if (c2 >= Character.MIN_LOW_SURROGATE && c2 <= Character.MAX_LOW_SURROGATE)
				return Character.toCodePoint(c1, c2);
		}
		return c1;
	}
	
	public int codePointBefore(int index)
	{
		char c1 = characters[index - 1];
		if (c1 >= Character.MIN_LOW_SURROGATE && c1 <= Character.MAX_LOW_SURROGATE && index > 1)
		{
			char c2 = characters[index - 2];
			if (c2 >= Character.MIN_HIGH_SURROGATE && c2 <= Character.MAX_HIGH_SURROGATE)
				return Character.toCodePoint(c2, c1);
		}
		return c1;
	}
	
	public int codePointCount(int begin, int end)
	{
		if (begin < 0 || begin > end || end > characters.length)
			throw new IndexOutOfBoundsException();
		
		int r = 0;
		for (int i=begin; i<end;)
		{
			char c1 = characters[i++];
			if (c1 >= Character.MIN_HIGH_SURROGATE && c1 <= Character.MAX_HIGH_SURROGATE && i < end)
			{
				char c2 = characters[i];
				if (c2 >= Character.MIN_LOW_SURROGATE && c2 <= Character.MAX_LOW_SURROGATE)
					i++;
			}
			r++;
		}
		return r;
	}
	
	public int compareTo(String str)
	{
		int len1 = this.characters.length;
		int len2 = str.characters.length;
		
		int len = (len1 < len2) ? len1 : len2;
		
		for (int i=0; i<len; i++)
		{
			char c1 = this.characters[i];
			char c2 = this.characters[i];
			
			if (c1 != c2)
				return (c1 < c2) ? -1 : 1;
		}
		
		if (len1 != len2)
			return (len1 < len2) ? -1 : 1;
		
		return 0;
	}
	
	public String concat(String s)
	{
		int len1 = this.characters.length;
		int len2 = len1 + s.characters.length;
		
		String r = new String(len2);
		System.arraycopy(this.characters, 0, r.characters, 0, len1);
		System.arraycopy(s.characters, 0, r.characters, len1, len2);
		return r;
	}
	
	public boolean contentEquals(CharSequence s)
	{
		int len = s.length();
		if (len != characters.length)
			return false;
		
		for (int i=0; i<len; i++)
			if (this.characters[i] != s.charAt(i))
				return false;
		
		return true;
	}
	
	public boolean contentEquals(StringBuffer sb)
	{
		return contentEquals((CharSequence)sb);
	}
	
	public static String copyValueOf(char[] data)
	{
		return String.copyValueOf(data, 0, data.length);
	}
	
	public static String copyValueOf(char[] data, int off, int len)
	{
		return new String(data, off, len);
	}

	/**
	 * Compares the String with an Object
	 * 
	 * @return true if the String is equal to the object, false otherwise
	 */
	@Override
	public boolean equals(Object other)
	{
		if (other == this)
			return true;
		// also catches other == null
		if (!(other instanceof String))
			return false;

		String os = (String)other;
		if (os.characters.length != characters.length)
			return false;

		for (int i = 0; i < characters.length; i++)
		{
			if (characters[i] != os.characters[i])
				return false;
		}

		return true;
	}
	
	public boolean equalsIgnoreCase(String s)
	{
		return this.toLowerCase().equals(s.toLowerCase());
	}

	public byte[] getBytes()
	{
		byte[] b = new byte[characters.length];
		for (int i = 0; i < characters.length; i++)
			b[i] = (byte)characters[i];
		return b;
	}
	
	/**
	 * @deprecated
	 */
	@Deprecated
	public void getBytes(int begin, int end, byte[] dst, int off)
	{
		off -= begin;
		for (int i = begin; i < end; i++)
			dst[off+i] = (byte)characters[i];
	}

	/**
	 * Get bytes in US Acsii
	 * 
	 * @param charset ignored
	 * @return the ascii bytes
	 */
	public byte[] getBytes(String charset)
	{
		return getBytes();
	}

	public void getChars(int start, int end, char[] buffer, int off)
	{
		if (start < 0 || start > characters.length)
			throw new StringIndexOutOfBoundsException(start);
		if (end > characters.length)
			throw new StringIndexOutOfBoundsException(end);
		if (end < start)
			throw new StringIndexOutOfBoundsException(end - start);

		System.arraycopy(this.characters, start, buffer, off, end - start);
	}

	/**
	 * Special version of hash that returns the same value the same String
	 * values
	 */
	@Override
	public int hashCode()
	{
		// computation is compatible with JDK, otherwise Java 7 String-switch-case,
		// which is based on hashCode, will fail.
		int h = 0;
		for (int i = 0; i < this.characters.length; i++)
		{
			h = 31 * h + this.characters[i];
		}
		return h;
	}

	/**
	 * Find the index of a character.
	 * 
	 * @param ch The character to find.
	 * @return The index of the character. -1 means it wasn't found.
	 */
	public int indexOf(int ch)
	{
		return indexOf(ch, 0);
	}

	/**
	 * Returns the index within this string of the first occurrence of the
	 * specified character, starting the search at the specified index.
	 * 
	 * @param ch a character (Unicode code point).
	 * @param fromIndex the index to start the search from.
	 * @return the index of the first occurrence of the character in the
	 *         character sequence represented by this object that is greater
	 *         than or equal to <code>fromIndex</code>, or <code>-1</code> if
	 *         the character does not occur.
	 */
	public int indexOf(int ch, int fromIndex)
	{
		int max = characters.length;
		char v[] = characters;

		if (fromIndex < 0)
			fromIndex = 0;
		
		for (int i=fromIndex; i<max; i++)
			if (v[i] == ch)
				return i;
		
		return -1;
	}

	/**
	 * Finds the location of a string within this string
	 * 
	 * @param str the String
	 * @return Index of string location, -1 if string does not exist.
	 */
	public int indexOf(String str)
	{
		return indexOf(str, 0);
	}

	/**
	 * Find location of String starting at a given index
	 * 
	 * @param str the String
	 * @param fromIndex the starting position
	 * @return Index of string location, -1 if string does not exist.
	 */
	public int indexOf(String str, int fromIndex)
	{
		return String.indexOf(characters, 0, characters.length, str.characters, 0,
			str.characters.length, fromIndex);
	}
	
	public String intern()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns true, if and only if this string is of zero length.
	 * 
	 * @return true if this string is of zero length, false otherwise.
	 */
	public boolean isEmpty()
	{
		return characters.length == 0;
	}
	
	public int lastIndexOf(char ch)
	{
		return lastIndexOf(ch, characters.length - 1);
	}
	
	public int lastIndexOf(char ch, int fromIndex)
	{
		int max = characters.length;
		char v[] = characters;

		if (fromIndex >= max)
			fromIndex = max - 1;
		
		for (int i=fromIndex; i>=0; i--)
			if (v[i] == ch)
				return i;
		
		return -1;
	}

	/**
	 * Find the last occurrence of a String
	 * 
	 * @param str the String
	 * @return index of string location, -1 if string does not exist.
	 */
	public int lastIndexOf(String str)
	{
		return lastIndexOf(str, characters.length - 1);
	}

	/**
	 * Find last occurrence of s string from a given index
	 * 
	 * @param str the String
	 * @param fromIndex the starting point
	 * @return index of string location, -1 if string does not exist.
	 */
	public int lastIndexOf(String str, int fromIndex)
	{
		return lastIndexOf(characters, 0, characters.length, str.characters, 0,
			str.characters.length, fromIndex);
	}

	/**
	 * Return the length of the String in characters
	 * 
	 * @return the length of the String
	 */
	public int length()
	{
		return characters.length;
	}
	
	public int offsetByCodePoints(int idx, int codePointCount)
	{
		for (; codePointCount > 0; codePointCount--)
		{
			char c1 = characters[idx++];
			if (c1 >= Character.MIN_HIGH_SURROGATE && c1 <= Character.MAX_HIGH_SURROGATE && idx < characters.length)
			{
				char c2 = characters[idx];
				if (c2 >= Character.MIN_LOW_SURROGATE && c2 <= Character.MAX_LOW_SURROGATE)
					idx++;
			}
		}
		for (; codePointCount < 0; codePointCount++)
		{
			char c1 = characters[--idx];
			if (c1 >= Character.MIN_LOW_SURROGATE && c1 <= Character.MAX_LOW_SURROGATE && idx > 0)
			{
				char c2 = characters[idx - 1];
				if (c2 >= Character.MIN_HIGH_SURROGATE && c2 <= Character.MAX_HIGH_SURROGATE)
					idx--;
			}
		}
		return idx;
	}
	
	public String replace(char oldChar, char newChar)
	{
		int len = characters.length;
		String r = new String(this.characters, 0, len);
		for (int i=0; i<len; i++)
			if (r.characters[i] == oldChar)
				r.characters[i] = newChar;
		return r;
	}
	
	public boolean startsWith(String s)
	{
		return startsWith(s, 0);
	}
	
	public boolean startsWith(String s, int begin)
	{
		int len = s.length();
		if (begin + len > this.characters.length)
			return false;
		for (int i=0; i<len; i++)
			if (this.characters[begin + i] != s.characters[i])
				return false;
		return true;
	}

	public CharSequence subSequence(int start, int end)
	{
		return this.substring(start, end);
	}

	/**
	 * Return substring from starting position to the end of the String
	 * 
	 * @param start the starting position
	 * @return the substring
	 */
	public String substring(int start)
	{
		return substring(start, characters.length);
	}

	/**
	 * Return substring from starting index to position before end index
	 * 
	 * @param start the start index
	 * @param end the end index (not included)
	 * @return the substring
	 */
	public String substring(int start, int end)
	{
		if (start < 0 || start > characters.length)
			throw new StringIndexOutOfBoundsException(start);
		if (end > characters.length)
			throw new StringIndexOutOfBoundsException(end);
		if (end < start)
			throw new StringIndexOutOfBoundsException(end - start);

		return new String(characters, start, end - start);
	}

	/**
	 * Converts the String into an array of characters
	 * 
	 * @return the character Array
	 */
	public char[] toCharArray()
	{
		int len = characters.length;
		char[] ca = new char[len];
		System.arraycopy(characters, 0, ca, 0, len);
		return ca;
	}

	public String toLowerCase()
	{

		char[] c1 = new char[characters.length];
		for (int i = 0; i < c1.length; i++)
		{
			c1[i] = characters[i];
			if (characters[i] >= 'A' & characters[i] <= 'Z')
				c1[i] += 32;
		}
		return new String(c1);
	}

	public String toUpperCase()
	{
		char[] c1 = new char[characters.length];
		for (int i = 0; i < c1.length; i++)
		{
			c1[i] = characters[i];
			if (characters[i] >= 'a' & characters[i] <= 'z')
				c1[i] -= 32;
		}
		return new String(c1);
	}
	
	/**
	 * Returns itself.
	 * 
	 * @return the String itself
	 */
	@Override
	public String toString()
	{
		return this;
	}
	
	public String trim()
	{
		int len = characters.length;
		if (len == 0 || (characters[0] > 0x20 && characters[len-1] > 0x20))
			return this;
		
		int start = 0;
		while (start < len && this.characters[start] <= 0x20)
			start++;
		int end = len;		
		while (end > start && this.characters[end - 1] <= 0x20)
			end--;
		return new String(this.characters, start, end - start);
	}

	public static String valueOf(boolean b)
	{
		return b ? "true" : "false";
	}

	public static String valueOf(char c)
	{
		String r = new String(1);
		r.characters[0] = c;
		return r;
	}

	public static String valueOf(char[] c)
	{
		return new String(c);
	}

	public static String valueOf(char[] c, int start, int length)
	{
		return new String(c, start, length);
	}

	// Maxi: We do like this, to save memory and not reallocate the array at each conversion which,
	// without garbage collection will result in an out of memory
	private static final char[] sb = new char[StringUtils.MAX_FLOAT_CHARS];

	public static String valueOf(float f)
	{
		int p = 0;
		synchronized (sb) {
			p = StringUtils.getFloatChars(f, sb, 0);
		}
		return new String(sb, 0, p);
	}

	public static String valueOf(int i)
	{
		return String.valueOf(i, 10);
	}

	public static String valueOf(long v)
	{
		return Long.toString(v);
	}

	/**
	 * Converts an Object to a String
	 * 
	 * @return the String that represents the object
	 */
	public static String valueOf(Object aObj)
	{
		return aObj == null ? "null" : aObj.toString();
	}

	/**
	 * For use by {@link Integer}
	 */
	static String valueOf(int i, int radix)
	{
		int len = StringUtils.exactStringLength(i, radix);
		String r = new String(len);
		StringUtils.getIntChars(r.characters, len, i, radix);
		return r;
	}
	
	/**
	 * Code shared by String and StringBuffer to do searches. The source is the
	 * character array being searched, and the target is the string being
	 * searched for.
	 * 
	 * @param source the characters being searched.
	 * @param sourceOffset offset of the source string.
	 * @param sourceCount count of the source string.
	 * @param target the characters being searched for.
	 * @param targetOffset offset of the target string.
	 * @param targetCount count of the target string.
	 * @param fromIndex the index to begin searching from.
	 */
	static int lastIndexOf(char[] source, int sourceOffset, int sourceCount, char[] target,
		int targetOffset, int targetCount, int fromIndex)
	{
		/*
		 * Check arguments; return immediately where possible. For consistency,
		 * don't check for null str.
		 */
		int rightIndex = sourceCount - targetCount;
		if (fromIndex < 0)
		{
			return -1;
		}
		if (fromIndex > rightIndex)
		{
			fromIndex = rightIndex;
		}
		/* Empty string always matches. */
		if (targetCount == 0)
		{
			return fromIndex;
		}

		int strLastIndex = targetOffset + targetCount - 1;
		char strLastChar = target[strLastIndex];
		int min = sourceOffset + targetCount - 1;
		int i = min + fromIndex;

		startSearchForLastChar : while (true)
		{
			while (i >= min && source[i] != strLastChar)
			{
				i--;
			}
			if (i < min)
			{
				return -1;
			}
			int j = i - 1;
			int start = j - (targetCount - 1);
			int k = strLastIndex - 1;

			while (j > start)
			{
				if (source[j--] != target[k--])
				{
					i--;
					continue startSearchForLastChar;
				}
			}
			return start - sourceOffset + 1;
		}
	}

	/**
	 * Code shared by String and StringBuffer to do searches. The source is the
	 * character array being searched, and the target is the string being
	 * searched for.
	 * 
	 * @param source the characters being searched.
	 * @param sourceOffset offset of the source string.
	 * @param sourceCount count of the source string.
	 * @param target the characters being searched for.
	 * @param targetOffset offset of the target string.
	 * @param targetCount count of the target string.
	 * @param fromIndex the index to begin searching from.
	 */
	static int indexOf(char[] source, int sourceOffset, int sourceCount, char[] target,
		int targetOffset, int targetCount, int fromIndex)
	{
		if (fromIndex >= sourceCount)
		{
			return (targetCount == 0 ? sourceCount : -1);
		}
		if (fromIndex < 0)
		{
			fromIndex = 0;
		}
		if (targetCount == 0)
		{
			return fromIndex;
		}

		char first = target[targetOffset];
		int max = sourceOffset + (sourceCount - targetCount);

		for (int i = sourceOffset + fromIndex; i <= max; i++)
		{
			/* Look for first character. */
			if (source[i] != first)
			{
				while (++i <= max && source[i] != first);
			}

			/* Found first character, now look at the rest of v2 */
			if (i <= max)
			{
				int j = i + 1;
				int end = j + targetCount - 1;
				for (int k = targetOffset + 1; j < end && source[j] == target[k]; j++, k++);

				if (j == end)
				{
					/* Found whole string. */
					return i - sourceOffset;
				}
			}
		}
		return -1;
	}
}
