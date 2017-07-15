package java.lang;

/**
 * Wrapper class for long integers.
 * @author Sven Köhler
 */
public final class Long extends Number
{
	public static final long MAX_VALUE = 0x7FFFFFFFFFFFFFFFL;
	public static final long MIN_VALUE = 0x8000000000000000L;	
	public static final int SIZE = 64;
	
	private final long value;
	
	public Long(long value)
	{
		this.value = value;
	}
	
	public static int bitCount(long v)
	{
		//See http://www-graphics.stanford.edu/~seander/bithacks.html#CountBitsSetParallel
		
		//first sum up every 1st and 2nd bit, the result are 2bit counters
		//but do it with some nice trick:
		//  11 - (11 >> 1) = 10
		//  10 - (10 >> 1) = 01
		//  01 - (01 >> 1) = 01
		//  00 - (00 >> 1) = 00
		v = v - ((v >>> 1) & 0x5555555555555555L);		
		//then sum up every 1st and 2nd of the 2-bit counters => 4bit counters
		v = (v & 0x3333333333333333L)  + ((v >>> 2) & 0x3333333333333333L);
		//then sum up every i-th and (i+1)-th of the 4bit counters and throw away some of them
		v = (v + (v >>> 4)) & 0x0F0F0F0F0F0F0F0FL;
		//at this point, we have 8bit counters. now we just need to sum them up:
		int i = ((int)v) + ((int)(v >>> 32));
		i += (i >>> 16);
		i += (i >>> 8);
		return i & 0xFF;
	}
		
	@Override
	public double doubleValue()
	{
		return this.value;
	}
	
	@Override
	public boolean equals(Object o)
	{
		//instanceof returns false for o==null
		return (o instanceof Long)
			&& (this.value == ((Long)o).value);
	}
	
	@Override
	public float floatValue()
	{
		return this.value;
	}
	
	public static Long getLong(String name, int def)
	{
		return getLong(name, new Integer(def));
	}
	
	@Override
	public int hashCode()
	{
		return ((int)this.value) ^ ((int)(this.value >>> 32)); 
	}
	
	public static long highestOneBit(long v)
	{
		//first set all bits below the highest bit:
		v |= (v >>> 1);
		v |= (v >>> 2);
		v |= (v >>> 4);
		v |= (v >>> 8);
		v |= (v >>> 16);
		v |= (v >>> 32);
		//then substract the lower bits
		return v - (v >>> 1);
	}
	
	@Override
	public int intValue()
	{
		return (int)this.value;
	}
	
	@Override
	public long longValue()
	{
		return this.value;
	}
	
	
	@Override
	public short shortValue()
	{
		return (short)this.value;
	}
	
	public static String toBinaryString(long v)
	{
		return toUnsignedString(v, 64, 1, 1);
	}
	
	public static String toOctalString(long v)
	{
		return toUnsignedString(v, 22, 7, 3);
	}
	
	public static String toHexString(long v)
	{
		return toUnsignedString(v, 16, 15, 4);
	}
	
	private static String toUnsignedString(long v, int maxlen, int mask, int shift)
	{
		char[] buf = new char[maxlen];
		int p = maxlen;
		
		do
		{
			buf[--p] = Character.forDigit(mask & (int)v, 16);
			v >>>= shift;
		} while (v != 0);
		
		return new String(buf, p, maxlen-p);
	}
		
	public static Long valueOf(long v)
	{
		return new Long(v);
	}
}
