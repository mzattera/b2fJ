package b2fj.lang;

/**
 * An string of bytes.
 */
public final class TinyString implements CharSequence, Comparable<String> {

    final byte capacity;
    final byte[] characters;
   
    int length = 0;

    public TinyString() {
		this(0);
	}

	public TinyString(byte capacity) {
        this.capacity=capacity;
		this.characters = new byte[capacity];
        this.length=0;
		for (int i = 0; i < capacity; i++)
			characters[i] = ' ';
	}

    private TinyString(int len) {
        this.capacity=(byte)len;
		this.characters = new byte[len];
        this.length=len;
	}

	/**
	 * Copies one array to another.
	 */
	// TODO: Make it native
	// public static native void arraycopy (Object src, int srcOffset, Object dest,
	// int destOffset, int length);
	public static void arraycopy(byte[] src, int srcOffset, char[] dest, int destOffset, int length) {
		for (int i = 0; i < length; i++)
			dest[i + destOffset] = (char)(src[i + srcOffset] & 0xFF);
	}

	/**
	 * Copies one array to another.
	 */
	// TODO: Make it native
	// public static native void arraycopy (Object src, int srcOffset, Object dest,
	// int destOffset, int length);
	public static void arraycopy(char[] src, int srcOffset, byte[] dest, int destOffset, int length) {
		for (int i = 0; i < length; i++)
			dest[i + destOffset] = (byte)(src[i + srcOffset] & 0xFF);
	}


	/**
	 * Create a String from a byte array
	 * 
	 * @param b the byte array
	 */
	public TinyString(byte[] b) {
		this(b, 0, b.length);
	}

	/**
	 * Create a String from a byte array
	 * 
	 * @param b the byte array
	 * @deprecated
	 */
	@Deprecated
	public TinyString(byte[] b, int hibyte) {
		this(b, hibyte, 0, b.length);
	}

	/**
	 * Create a String from a byte array
	 * 
	 * @param b the byte array
	 */
	public TinyString(byte[] b, int off, int len) {
		this(len);
		for (int i = 0; i < len; i++)
			characters[i] = b[off + i];
	}

	/**
	 * Create a String from a byte array
	 * 
	 * @param b the byte array
	 * @deprecated
	 */
	@Deprecated
	public TinyString(byte[] b, int hibyte, int off, int len) {
		this(len);
		for (int i = 0; i < len; i++)
			characters[i] = (b[off + i]);
	}

	/**
	 * Create a String from a byte array
	 * 
	 * @param b       the byte array
	 * @param charset ignored - assumed to be US ASCII
	 */
	public TinyString(byte[] b, int off, int len, String charset) {
		this(b, off, len);
	}

	/**
	 * Create a String from a byte array
	 * 
	 * @param b       the byte array
	 * @param charset ignored - assumed to be US ASCII
	 */
	public TinyString(byte[] b, String charset) {
		this(b, 0, b.length, charset);
	}

	/**
	 * Create a String from a character array
	 * 
	 * @param c the character array
	 */
	public TinyString(char[] c) {
		this(c, 0, c.length);
	}

	/**
	 * Create a String from a character array.
	 * 
	 * @param c   the character array
	 * @param off the offset - usually 0
	 * @param len the length of the String - must not be greater than c.length
	 */
	public TinyString(char[] c, int off, int len) {
		this(len);
		arraycopy(c, off, characters, 0, len);
	}

	public TinyString(TinyString s) {
		this(s.toCharArray());
	}

    public TinyString(java.lang.String string) {
        this(string.toCharArray());
    }

	/**
	 * Return the character at the given index
	 * 
	 * @return the characters at the given index
	 */
	public char charAt(int index) {

        if (index > characters.length)
			throw new StringIndexOutOfBoundsException(index);

		return ((char)(characters[index]&0xFF));

	}

	public int compareTo(TinyString str) {
		int len1 = this.characters.length;
		int len2 = str.characters.length;

		int len = (len1 < len2) ? len1 : len2;

		for (int i = 0; i < len; i++) {
			byte c1 = this.characters[i];
			byte c2 = str.characters[i];

			if (c1 != c2)
				return (c1 < c2) ? -1 : 1;
		}

		if (len1 != len2)
			return (len1 < len2) ? -1 : 1;

		return 0;
	}

	public int compareTo(String str) {
		int len1 = this.characters.length;
		int len2 = str.length();

		int len = (len1 < len2) ? len1 : len2;

		for (int i = 0; i < len; i++) {
			byte c1 = this.characters[i];
			byte c2 = (byte)str.charAt(i);

			if (c1 != c2)
				return (c1 < c2) ? -1 : 1;
		}

		if (len1 != len2)
			return (len1 < len2) ? -1 : 1;

		return 0;
	}

	public TinyString concat(TinyString s) {
		int len1 = this.characters.length;
		int len2 = len1 + s.characters.length;

		TinyString r = new TinyString(len2);
		System.arraycopy(this.characters, 0, r.characters, 0, len1);
		System.arraycopy(s.characters, 0, r.characters, len1, len2);
		return r;
	}

	/**
	 * Compares the String with an Object
	 * 
	 * @return true if the String is equal to the object, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		// also catches other == null
		if (!(other instanceof TinyString))
			return false;

		TinyString os = (TinyString) other;
		if (os.characters.length != characters.length)
			return false;

		for (int i = 0; i < characters.length; i++) {
			if (characters[i] != os.characters[i])
				return false;
		}

		return true;
	}

	public byte[] getBytes() {
		return this.characters;
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public void getBytes(int begin, int end, byte[] dst, int off) {
		off -= begin;
		for (int i = begin; i < end; i++)
			dst[off + i] = (byte) characters[i];
	}

	/**
	 * Get bytes in US Acsii
	 * 
	 * @param charset ignored
	 * @return the ascii bytes
	 */
	public byte[] getBytes(String charset) {
		return getBytes();
	}

	public void getChars(int start, int end, char[] buffer, int off) {
		if (start < 0 || start > characters.length)
			throw new StringIndexOutOfBoundsException(start);
		if (end > characters.length)
			throw new StringIndexOutOfBoundsException(end);
		if (end < start)
			throw new StringIndexOutOfBoundsException(end - start);

		arraycopy(this.characters, start, buffer, off, end - start);
	}

	/**
	 * Special version of hash that returns the same value the same String values
	 */
	@Override
	public int hashCode() {
		// computation is compatible with JDK, otherwise Java 7 String-switch-case,
		// which is based on hashCode, will fail.
		int h = 0;
		for (int i = 0; i < this.characters.length; i++) {
			h = 31 * h + this.characters[i];
		}
		return h;
	}

	public String intern() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns true, if and only if this string is of zero length.
	 * 
	 * @return true if this string is of zero length, false otherwise.
	 */
	public boolean isEmpty() {
		return length == 0;
	}


	/**
	 * Return the length of the String in characters
	 * 
	 * @return the length of the String
	 */
	public int length() {
		return length;
	}
	public CharSequence subSequence(int start, int end) {
		return this.substring(start, end);
	}

	/**
	 * Return substring from starting position to the end of the String
	 * 
	 * @param start the starting position
	 * @return the substring
	 */
	public TinyString substring(int start) {
		return substring(start, characters.length);
	}

	/**
	 * Return substring from starting index to position before end index
	 * 
	 * @param start the start index
	 * @param end   the end index (not included)
	 * @return the substring
	 */
	public TinyString substring(int start, int end) {
		if (start < 0 || start > characters.length)
			throw new StringIndexOutOfBoundsException(start);
		if (end > characters.length)
			throw new StringIndexOutOfBoundsException(end);
		if (end < start)
			throw new StringIndexOutOfBoundsException(end - start);

		return new TinyString(characters, start, end - start);
	}

	/**
	 * Converts the String into an array of characters
	 * 
	 * @return the character Array
	 */
	public char[] toCharArray() {
		int len = characters.length;
		char[] ca = new char[len];
		arraycopy(characters, 0, ca, 0, len);
		return ca;
	}

   	/**
	 * get accesor.
	 * 
	 * @return the String itself
	 */
	public byte[] characters() {
		return this.characters;
	}

    @Override
	public java.lang.String toString() {
		return new java.lang.String(characters);
	}

	public static TinyString valueOf(boolean b) {
		return b ? new TinyString("true") : new TinyString("false");
	}

	public static TinyString valueOf(char c) {
		TinyString r = new TinyString(1);
		r.characters[0] = (byte)c;
		return r;
	}

	public static TinyString valueOf(char[] c) {
		return new TinyString(c);
	}

	public static TinyString valueOf(char[] c, int start, int length) {
		return new TinyString(c, start, length);
	}

	public static TinyString valueOf(int i) {
		return TinyString.valueOf(i, 10);
	}

	public static TinyString valueOf(long v) {
		return new TinyString(Long.toString(v));
	}

    /**
	 * For use by {@link Integer}
	 */
	static TinyString valueOf(int i, int radix) {
		int len = StringUtils.exactStringLength(i, radix);
		TinyString r = new TinyString(len);
		StringUtils.getIntBytes(r.characters, len, i, radix);
		return r;
	}

	public void ensureCapacity(byte newLen) {
		if(newLen>capacity) { throw new java.lang.ArrayIndexOutOfBoundsException(); }
	}

    public TinyString setValue(int i) {
		this.setValue(i, 10);
		return this;
	}

     /**
	 * For use by {@link Integer}
	 */
	TinyString setValue(int i, int radix) {
        //System.out.print('v');
        //System.out.println(java.lang.String.valueOf(i));
        int newLen = StringUtils.exactStringLength(i, radix);
        //System.out.println("len="+java.lang.String.valueOf(len));
        ensureCapacity((byte)newLen);
		StringUtils.getIntBytes(characters, newLen, i, radix);
        this.length=newLen;
        //System.out.println("length="+this.length);
		return this;
	}

	/**
	 * Converts an Object to a String
	 * 
	 * @return the String that represents the object
	 */
	public static TinyString valueOf(Object aObj) {
		return aObj == null ? new TinyString("null") : new TinyString(aObj.toString());
	}

	/**
	 * Appends a string with no null checking
	 */
	public TinyString append(TinyString s) {
		if (s == null)
			return this;

		// Reminder: compact code more important than speed
		byte[] sc= s.characters;
		byte sl = (byte)s.length;

		byte newlen = (byte)(this.length + sl);
		this.ensureCapacity(newlen);

		System.arraycopy (sc, 0, characters, this.length, sl);
		this.length = newlen;

		return this;
	}

	/**
	 * Appends a string with no null checking
	 */
	public TinyString append(String s) {
		if (s == null)
			return this;

		// Reminder: compact code more important than speed
		byte sl = (byte)s.length();

		byte newlen = (byte)(this.length + sl);
		this.ensureCapacity(newlen);

		for (int i = 0; i < sl; i++)
			characters[i + this.length] = (byte)(s.charAt(i) & 0xFF);
		this.length = newlen;

		return this;
	}

	public void setLength(byte newLen)
	{
		if (newLen < 0)
			throw new IndexOutOfBoundsException();

		ensureCapacity((byte)newLen);

		for (int i=length; i<newLen; i++)
			characters[i] = 0x32;

		length = newLen;
	}

	public void clear()
	{
		for (int i = 0; i < capacity; i++)
			characters[i] = ' ';
		length=0;
	}

	/**
 * Just some utility methods for the wrapper classes as well as StringBuffer and StringBuilder.
 * @author Sven Köhler
 */
private static class StringUtils
{
	//private static final int STR_NAN_LEN = 3;
	//private static final java.lang.String STR_NAN = "NaN";
	//private static final int STR_INFINITY_LEN = 8;
	//private static final java.lang.String STR_INFINITY = "Infinity";
	
	/**
	 * 123456.7
	 * 0.001234567
	 * 1.234567E-12
	 */
	//static final int MAX_FLOAT_CHARS = 15;
	
	/**
	 * 1234567.89012345 
	 * 0.00123456789012345 
	 * 1.23456789012345E-123 
	 */
	//static final int MAX_DOUBLE_CHARS = 25;
	/*
	static int parseDigit(char c, int radix)
	{
		int r = Character.digit((int)c, radix);		
		if (r < 0)
			// throw new NumberFormatException("illegal digit character");
			throw new NumberFormatException();
		
		return r;
	}
	*/

	/**
	 * Low-level convert of int to char[].
	 * 
	 * @param p position of the character after the last digit
	 */
	static int getIntBytes(byte[] buf, int p, int v, int radix)
	{
		int v2 = (v <= 0) ? v : -v;
		
		do
		{
			buf[--p] = (byte)(Character.forDigit(-(v2 % radix), radix) & 0xFF);
			v2 /= radix;
		} while (v2 != 0);
		
		if (v < 0)
			buf[--p] = '-';
		
		return p;
	}
	
	/**
	 * Exact size of buffer for {@link #getIntChars(char[], int, int, int)}.
	 */
	static int exactStringLength(int v, int radix)
	{
		int c = (v < 0) ? 1 : 0;
		do
		{
			c++;
			v /= radix;
		} while (v != 0);
		
		return c;
	}
	
	private static final class Character implements Comparable<Character>
{
	public static final int SIZE = 16;
	
	public static final int MIN_RADIX = 2;
	public static final int MAX_RADIX = 36;
	
	public static final char MIN_VALUE = '\u0000';
	public static final char MAX_VALUE = '\uFFFF';
	
	//MISSING everything
	
	private final char value;
	
	public Character(char c)
	{
		this.value = c;
	}

	public Character(byte c)
	{
		this.value = (char)(c & 0xFF);
	}

	public char charValue()
	{
		return this.charValue();
	}
	
	public int compareTo(Character ob)
	{
		if (this.value == ob.value)
			return 0;
		
		return (this.value > ob.value) ? 1 : -1;
	}
	
	@Override
	public boolean equals(Object o)
	{
		//instanceof returns false for o==null
		return (o instanceof Character)
			&& (this.value == ((Character)o).value);
	}
	
	/**
	 * This method accepts a character such as '7' or 'C' and converts
	 * it to a number value. So '7' returns 7 and 'C' returns 12,
	 * which is the hexidecimal value of C. You must specify a radix
	 * for the number system. If the digit is not defined for the specified
	 * radix or the radix is invalid, -1 is returned.
	 * 
	 * @param ch the character
	 * @param radix the radix
	 * @return the numerical value of the digit
	 */
	public static int digit(char ch, int radix)
	{
		return digit((int)ch, radix);
	}

	public static int digit(int ch, int radix)
	{
		//MISSING only handles latin1 for now
		
		if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
			return -1;
		
		if (ch >= '0' && ch <= '9')
			ch -= '0';
		else if (ch >= 'a' && ch <= 'z')
			ch -= 'a'  - 10;
		else if (ch >= 'A' && ch <= 'Z')
			ch -= 'A'  - 10;
		else
			return -1;
		
		if (ch >= radix)	
			return -1;
		
		return ch;
	}	

	public static char forDigit(int digit, int radix)
	{
		if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
			return '\u0000';
		if (digit < 0 || digit >= radix)
			return '\u0000';
		
		char r;
		if (digit < 10)
			r = (char)(digit + '0');
		else
			r = (char)(digit + ('a' - 10));
		
		return r;
	}
	
	@Override
	public int hashCode()
	{
		return this.value;
	}

	@Override
	public java.lang.String toString()
	{
		return Character.toString(this.value);
	}
	
	public static java.lang.String toString(char c)
	{
		return java.lang.String.valueOf(c);
	}
	
	public static Character valueOf(char c)
	{
		return new Character(c);
	}
	
	public static char reverseBytes(char c)
	{
		return (char)((c << 8) | (c >> 8));
	}
}

}
}