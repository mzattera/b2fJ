package b2fj.lang;

/**
 * Just some utility methods for the wrapper classes as well as StringBuffer and StringBuilder.
 * @author Sven Köhler
 */
class StringUtils
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
	
	
}
