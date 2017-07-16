/**
 * Just some utility methods for the wrapper classes as well as StringBuffer and StringBuilder.
 * @author Sven Köhler
 */
class MyStringUtils
{
	private static final int STR_NAN_LEN = 3;
	private static final String STR_NAN = "NaN";
	private static final int STR_INFINITY_LEN = 8;
	private static final String STR_INFINITY = "Infinity";
	
	/**
	 * 123456.7
	 * 0.001234567
	 * 1.234567E-12
	 */
	static final int MAX_FLOAT_CHARS = 15;
	
	/**
	 * 1234567.89012345 
	 * 0.00123456789012345 
	 * 1.23456789012345E-123 
	 */
	static final int MAX_DOUBLE_CHARS = 25;
	
	static int parseDigit(char c, int radix)
	{
		int r = Character.digit((int)c, radix);		
		if (r < 0)
			// throw new NumberFormatException("illegal digit character");
			throw new NumberFormatException();
		
		return r;
	}
	
	/**
	 * For the parseInt/parseLong methods.
	 */
	static void throwNumberFormat(String s, int radix)
	{
		if (s == null)
			// throw new NumberFormatException("string is null");
			throw new NumberFormatException();
		
		if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
			// throw new NumberFormatException("given radix is invalid");
			throw new NumberFormatException();
	}
	
	/**
	 * For the toString() methods of Integer/Long.
	 */
	static int invalidRadixTo10(int radix)
	{
		if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
			return 10;
		
		return radix;
	}

	/**
	 * Low-level convert of int to char[].
	 * 
	 * @param p position of the character after the last digit
	 */
	static int getIntChars(char[] buf, int p, int v, int radix)
	{
		int v2 = (v <= 0) ? v : -v;
		
		do
		{
			buf[--p] = Character.forDigit(-(v2 % radix), radix);
			v2 /= radix;
		} while (v2 != 0);
		
		if (v < 0)
			buf[--p] = '-';
		
		return p;
	}
	
	static void reverseChars(char[] buf, int start, int end, int len)
	{
		len = Math.min(len, (end - start) >> 1);
		int end2 = start + len;
		int base = start + end - 1;
	
		for (int i = start; i < end2; i++)
		{
			int j = base - i;
			char tmp = buf[i];
			buf[i] = buf[j];
			buf[j] = tmp;
		}
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
	
	private static float pow10f_bf(float r, int e)
	{
		// bf stands for "Big exponent First"
		if (e > 0)
		{
			if ((e & 0x020) != 0) r *= 1E+32f;
			if ((e & 0x010) != 0) r *= 1E+16f;
			if ((e & 0x008) != 0) r *= 1E+8f;
			if ((e & 0x004) != 0) r *= 1E+4f;
			if ((e & 0x002) != 0) r *= 1E+2f;
			if ((e & 0x001) != 0) r *= 1E+1f;
		}
		else if (e < 0)
		{
			e = -e;
			if ((e & 0x020) != 0) r *= 1E-32f;
			if ((e & 0x010) != 0) r *= 1E-16f;
			if ((e & 0x008) != 0) r *= 1E-8f;
			if ((e & 0x004) != 0) r *= 1E-4f;
			if ((e & 0x002) != 0) r *= 1E-2f;
			if ((e & 0x001) != 0) r *= 1E-1f;			
		}
		
		return r;
	}
	
	private static float pow10f_sf(float r, int e)
	{
		// sf stands for "Small exponent First"
		if (e > 0)
		{
			if ((e & 0x001) != 0) r *= 1E+1f;
			if ((e & 0x002) != 0) r *= 1E+2f;
			if ((e & 0x004) != 0) r *= 1E+4f;
			if ((e & 0x008) != 0) r *= 1E+8f;
			if ((e & 0x010) != 0) r *= 1E+16f;
			if ((e & 0x020) != 0) r *= 1E+32f;
		}
		else
		{
			e = -e;
			if ((e & 0x001) != 0) r *= 1E-1f;
			if ((e & 0x002) != 0) r *= 1E-2f;
			if ((e & 0x004) != 0) r *= 1E-4f;
			if ((e & 0x008) != 0) r *= 1E-8f;
			if ((e & 0x010) != 0) r *= 1E-16f;
			if ((e & 0x020) != 0) r *= 1E-32f;			
		}
		
		return r;
	}
	
	static int getFloatChars(float x, char[] sb, int p)
	{
		if (x != x)
		{
			STR_NAN.getChars(0, STR_NAN_LEN, sb, p);
			return p + STR_NAN_LEN;
		}
		
		//we need to detect -0.0 to be compatible with JDK
		int bits = Float.floatToRawIntBits(x); 
		if ((bits & 0x80000000) != 0)
		{
			sb[p++] = '-';
			x = -x;
		}
		
		if (x == 0)
		{
			sb[p] = '0';
			sb[p+1] = '.';
			sb[p+2] = '0';
			return p+3;
		}
		if (x == Float.POSITIVE_INFINITY)
		{
			STR_INFINITY.getChars(0, STR_INFINITY_LEN, sb, p);
			return p + STR_INFINITY_LEN;
		}
		
		int exp;		
		if (x >= Float.MIN_NORMAL)
			exp = 31;
		else
		{
			exp = 0;
			bits = Float.floatToRawIntBits(x * 0x1p31f);
		}
		
		exp += (bits >> 23) & 0xFF;
		exp = ((exp * 5050455) >> 24) - 48;
		
		// at this point, the following should always hold:
		//   floor(log(10, x)) - 1 < exp <= floor(log(10, x))
		x = pow10f_bf(x, 6 - exp);
		
		int tmp = 10000000;
		int digits = (int)(x + 0.5f);
		
		if (digits >= tmp)
		{
			exp++;
			digits = (int)(x * 0.1f + 0.5f);
		}
		
		// algorithm shows true value of subnormal doubles
		// unfortunatly, the mantisse of subnormal values gets very short
		// TODO automatically adjust digit count for subnormal values
		
		int leading = 0;
		int trailing = 0;
		if (exp >= 0)
		{
			if (exp < 6)
			{
				leading = exp;
				exp = 0;
			}
		}
		else if (exp > -4)
		{
			sb[p++] = '0';
			leading = -1;
			trailing = -exp;
			exp = 0;
		}

		for (int i=0; i<=leading; i++)
		{
			int d = digits / (tmp /= 10);
			sb[p++] = (char)('0' + d);
			digits -= tmp * d;
		}		
		sb[p++] = '.';
		for (int i=1; i<trailing; i++)
			sb[p++] = '0';		
		
		do
		{
			int d = digits / (tmp /= 10);
			sb[p++] = (char)('0' + d);
			digits -= tmp * d;
		}
		while (digits > 0);
		
		if (exp != 0)
		{
			sb[p++] = 'E';
			if (exp < 0)
			{
				sb[p++] = '-';
				exp = -exp;
			}			
			if (exp >= 10)
				sb[p++] = (char)(exp / 10 + '0');
			sb[p++] = (char)(exp % 10 + '0');
		}

		return p;
	}

	private static boolean checkString(String s, int p, String c)
	{
		int l = c.length();
		for (int i=0; i<l; i++)
			if (s.charAt(p+i) != c.charAt(i))
				return false;
		
		return true;
	}
	
	/**
	 * Roughly equals abs(minimal exponent of subnormal float in base 10) + digits of int 
	 */
	private static final int STR_TO_FLOAT_MAXEXP = 60; 
	
	static float stringToFloat(String s)
	{
		int r = 0;
		int exp = 0;
		
		int l = s.length();
		
		if (l <= 0)
			throw new NumberFormatException();
		
		int p;
		boolean neg;
		if (s.charAt(0) == '-') {
				p = 1;
				neg = true;
		} else if (s.charAt(0) == '+') {
				p = 1;
				neg = false;
		} else {
				p = 0;
				neg = false;				
		}
		
		if (l-p == STR_NAN_LEN) {
				if (checkString(s, p, STR_NAN))
					return Float.NaN;
		} else if (l-p == STR_INFINITY_LEN) {
				if (checkString(s, p, STR_INFINITY))
					return neg ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
		}
		
		boolean digits = false;
		
		while (p < l)
		{
			char c = s.charAt(p);
			if (c < '0' || c > '9')
			{
				if (c == '.' || c == 'e' || c == 'E')
					break;
				
				throw new NumberFormatException();
			}
			
			digits = true;
			
			if (r <= (Integer.MAX_VALUE - 9) / 10)
				r = r * 10 + (c - '0');
			else
				exp++;
			
			p++;
		}
		
		if (p < l && s.charAt(p) == '.')
		{
			p++;
			
			while (p < l)
			{
				char c = s.charAt(p);
				if (c < '0' || c > '9')
				{
					if (c == 'e' || c == 'E')
						break;
					
					throw new NumberFormatException();
				}
				
				digits = true;
				
				if (r <= (Integer.MAX_VALUE - 9) / 10)
				{
					r = r * 10 + (c - '0');				
					exp--;
				}				
				p++;
			}			
		}
		
		if (!digits)
			throw new NumberFormatException();
		
		if (p < l)
		{
			//at this point, s.charAt(p) has to be 'e' or 'E'
			p++;
			
			boolean digitsexp = false;
			
			boolean negexp = false;
			if (p < l)
			{
				if (s.charAt(p) == '-') {
						negexp = true;
						exp = -exp;
						p++;
				} else if (s.charAt(p) == '+') {
						p++;
				} else {
						negexp = false;
				}
			}
			else
				negexp = false;
			
			int exp2 = 0;
			while (p < l)
			{
				char c = s.charAt(p);
				if (c < '0' || c > '9')
					throw new NumberFormatException();
				
				digitsexp = true;
				
				if (exp2 + exp < STR_TO_FLOAT_MAXEXP)
					exp2 = exp2 * 10 + (c - '0');
				
				p++;
			}
			
			if (!digitsexp)
				throw new NumberFormatException();
			
			exp2 += exp;
			exp = negexp ? -exp2 : exp2;
		}
		
		float r2;
		if (exp < -STR_TO_FLOAT_MAXEXP)
			r2 = 0.0f;
		else if (exp > STR_TO_FLOAT_MAXEXP)
			r2 = Float.POSITIVE_INFINITY;
		else
			r2 = pow10f_sf(r, exp);
		
		return neg ? -r2 : r2;
	}
	
}
