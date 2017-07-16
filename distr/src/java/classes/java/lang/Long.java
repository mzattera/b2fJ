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
			
	@Override
	public double doubleValue()
	{
		return this.value;
	}
	
	/**
	 * Unsupported: it errors.
	 */
	@Override
	public boolean equals(Object o)
	{
		throw new UnsupportedOperationException();
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
		return (int)this.value; 
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
	
    @Override
    public String toString()
    {
    	return "<Long>";
    }

	public static String toString(long v)
	{
		return "<Long>";
	}
			
	public static Long valueOf(long v)
	{
		return new Long(v);
	}
}