package java.lang;

/**
 * Wrapper class for bytes.
 * @author Sven Köhler
 */
public final class Byte extends Number
{
	public static final byte MAX_VALUE = 127;
	public static final byte MIN_VALUE = -128;
	public static final int SIZE = 8;
	
	//MISSING public static Byte decode(String)
	
	private final byte value;
	
	public Byte(byte value)
	{
		this.value = value;
	}
	
	public Byte(String s)
	{
		this.value = Byte.parseByte(s);
	}

	@Override
	public byte byteValue()
	{
		return this.value;
	}
	
	public int compareTo(Object o)
	{
		Byte ob = (Byte)o;
		if (this.value == ob.value)
			return 0;
		
		return (this.value > ob.value) ? 1 : -1;
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
		return (o instanceof Byte)
			&& (this.value == ((Byte)o).value);
	}

	@Override
	public float floatValue()
	{
		return this.value;
	}
	
	@Override
	public int hashCode()
	{
		return this.value;
	}

	@Override
	public int intValue()
	{
		return this.value;
	}

	@Override
	public long longValue()
	{
		return this.value;
	}
	
	public static byte parseByte(String s) throws NumberFormatException
	{
		return Byte.parseByte(s, 10);
	}
	
	public static byte parseByte(String s, int radix) throws NumberFormatException
	{
		int tmp = Integer.parseInt(s, radix);
		if (tmp < Byte.MIN_VALUE || tmp > Byte.MAX_VALUE)
			// throw new NumberFormatException("number is too big");
			throw new NumberFormatException();
		
		return (byte)tmp;
	}

	@Override
	public short shortValue()
	{
		return this.value;
	}
	
	@Override
	public String toString()
	{
    	return String.valueOf(this.value, 10);
	}
	
	public static String toString(byte b)
	{
    	return String.valueOf(b, 10);
	}
	
	public static Byte valueOf(byte b)
	{
		return new Byte(b);
	}
	
	public static Byte valueOf(String s)
	{
		return Byte.valueOf(s, 10);
	}
	
	public static Byte valueOf(String s, int radix)
	{
		return Byte.valueOf(Byte.parseByte(s, radix));
	}
}
