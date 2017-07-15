package java.lang;

public final class Character implements Comparable<Character>
{
	public static final int SIZE = 16;
	
	public static final int MIN_RADIX = 2;
	public static final int MAX_RADIX = 36;
	
	public static final char MIN_VALUE = '\u0000';
	public static final char MAX_VALUE = '\uFFFF';
	
	public static final char MIN_HIGH_SURROGATE = '\uD800';
	public static final char MAX_HIGH_SURROGATE = '\uDBFF';
	public static final char MIN_LOW_SURROGATE = '\uDC00';
	public static final char MAX_LOW_SURROGATE = '\uDFFF';
	
	public static final char MIN_SURROGATE = MIN_HIGH_SURROGATE;
	public static final char MAX_SURROGATE = MAX_LOW_SURROGATE;
	
	public static final int MIN_CODE_POINT = 0;
	public static final int MIN_SUPPLEMENTARY_CODE_POINT = 0x10000;
	public static final int MAX_CODE_POINT = 0x10FFFF;	
	
	//MISSING everything
	
	private final char value;
	
	public Character(char c)
	{
		this.value = c;
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
	
	public int codePointAt(char[] c, int index)
	{
		return this.codePointAt(c, index, c.length);
	}
	
	public int codePointAt(char[] c, int index, int limit)
	{
		char c1 = c[index];
		if (Character.isHighSurrogate(c1))
		{
			int index2 = index + 1;			
			if (index2 < limit)
			{
				char c2 = c[index2];
				if (Character.isLowSurrogate(c2))
				{
					return Character.toCodePoint(c1, c2);
				}
			}
		}
		return c1;
	}
	
	public int codePointAt(CharSequence c, int index)
	{
		char c1 = c.charAt(index);
		if (Character.isHighSurrogate(c1))
		{
			int index2 = index + 1;			
			if (index2 < c.length())
			{
				char c2 = c.charAt(index2);
				if (Character.isLowSurrogate(c2))
				{
					return Character.toCodePoint(c1, c2);
				}
			}
		}
		return c1;
	}
	
	public int codePointBefore(char[] c, int index)
	{
		return codePointBefore(c, index, 0);
	}
	
	public int codePointBefore(char[] c, int index, int start)
	{
		char c2 = c[index - 1];
		if (Character.isLowSurrogate(c2))
		{
			int index2 = index - 2;			
			if (index2 >= start)
			{
				char c1 = c[index2];
				if (Character.isHighSurrogate(c1))
				{
					return Character.toCodePoint(c1, c2);
				}
			}
		}
		return c2;
	}
	
	public int codePointBefore(CharSequence c, int index)
	{
		char c2 = c.charAt(index - 1);
		if (Character.isLowSurrogate(c2))
		{
			int index2 = index - 2;			
			if (index2 >= 0)
			{
				char c1 = c.charAt(index2);
				if (Character.isHighSurrogate(c1))
				{
					return Character.toCodePoint(c1, c2);
				}
			}
		}
		return c2;
	}
	
	/**
	 * Identifies if a character is a numerical digit. 
	 * 
	 * @param ch the character
	 * @return true iff the character is a numerical digit
	 */
	public static boolean isDigit(char ch) {
		return (ch >= '0' & ch <= '9');
	}
	
	public static boolean isLowSurrogate(char low)
	{
		return low >= MIN_LOW_SURROGATE && low <= MAX_LOW_SURROGATE;
	}
		
	public static boolean isHighSurrogate(char high)
	{
		return high >= MIN_HIGH_SURROGATE && high <= MAX_HIGH_SURROGATE;
	}
	
	public static boolean isSurrogatePair(char high, char low)
	{
		return high >= MIN_HIGH_SURROGATE && high <= MAX_HIGH_SURROGATE && low >= MIN_LOW_SURROGATE && low <= MAX_LOW_SURROGATE;
	}
	
	public static boolean isValidCodePoint(int codepoint)
	{
		return codepoint >= MIN_CODE_POINT && codepoint <= MAX_CODE_POINT;
	}
	
	public static boolean isSupplementaryCodePoint(int codepoint)
	{
		return codepoint >= MIN_SUPPLEMENTARY_CODE_POINT && codepoint <= MAX_CODE_POINT;
	}
	
	public static int charCount(int codepoint)
	{
		return codepoint < MIN_SUPPLEMENTARY_CODE_POINT ? 1 : 2;
	}
	
	public static char[] toChars(int codepoint)
	{
		if (codepoint < MIN_CODE_POINT || codepoint > MAX_CODE_POINT)
			// throw new IllegalArgumentException("invalid codepoint");
			throw new IllegalArgumentException();
		
		if (codepoint < MIN_SUPPLEMENTARY_CODE_POINT)
			return new char[] { (char)codepoint };
		
		codepoint -= MIN_SUPPLEMENTARY_CODE_POINT;		
		return new char[] {
				(char)((codepoint >> 10) | MIN_HIGH_SURROGATE),
				(char)(codepoint & 0x3FF | MIN_LOW_SURROGATE),
			};
	}
	
	public static int toChars(int codepoint, char[] dst, int off)
	{
		if (codepoint < MIN_CODE_POINT || codepoint > MAX_CODE_POINT)
			//throw new IllegalArgumentException("invalid codepoint");
			throw new IllegalArgumentException();
		
		if (codepoint < MIN_SUPPLEMENTARY_CODE_POINT)
		{
			dst[off] = (char)codepoint;
			return 1;
		}
		
		codepoint -= MIN_SUPPLEMENTARY_CODE_POINT;
		dst[off] = (char)((codepoint >> 10) | MIN_HIGH_SURROGATE);
		dst[off + 1] = (char)(codepoint & 0x3FF | MIN_LOW_SURROGATE);
		return 2;
	}
	
	public static int toCodePoint(char high, char low)
	{
		//no validating required, says JDK javadocs
		return high << 10 + low + (MIN_SUPPLEMENTARY_CODE_POINT - (MIN_HIGH_SURROGATE << 10) - MIN_LOW_SURROGATE);
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

	@Override
	public boolean equals(Object o)
	{
		//instanceof returns false for o==null
		return (o instanceof Character)
			&& (this.value == ((Character)o).value);
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
	public String toString()
	{
		return Character.toString(this.value);
	}
	
	public static String toString(char c)
	{
		return String.valueOf(c);
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
