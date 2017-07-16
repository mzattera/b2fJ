package java.lang;

/**
 * Minimal Double implementation.
 * 
 * @author bb
 * @author Sven Köhler
 */
public final class Double extends Number implements Comparable<Double>
{
	public static final double POSITIVE_INFINITY = 1.0d / 0.0d;
	public static final double NEGATIVE_INFINITY = -1.0d / 0.0d;
	public static final double NaN = 0.0d / 0.0d;
	
	public static final int MAX_EXPONENT = 1023;
	public static final double MAX_VALUE = 0x1.fffffffffffffp+1023;
	
	public static final int MIN_EXPONENT = -1022;
	public static final double MIN_NORMAL = 0x1.0p-1022;
	public static final double MIN_VALUE = 0x1.0p-1074;
	
	public static final int SIZE = 64;
	
	//MISSING public static String toHexString(double)
	
	private double value;
	
	public Double(double val)
	{
		this.value = val;
	}
	
	@Override
	public byte byteValue()
	{
		return (byte)this.value;
	}
	
	public static int compare(double a, double b)
	{
		// normal float compare, NaN falls through
		if (a > b)
			return 1;
		if (a < b)
			return -1;
		// early out for non-zero values, NaN falls through
		if (a == b && a != 0)
			return 0;
		
		// TODO: handle NaN and negative/positive zero
		return 0;
	}
	
	public int compareTo(Double other)
	{
		return Double.compare(this.value, other.value);
	}
	
	@Override
	public double doubleValue()
	{
		return this.value;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null) return false;
		return this.value == ((Double)o).value;
	}
	
	@Override
	public float floatValue()
	{
		return (float)this.value;
	}
	
	@Override
	public int hashCode()
	{
		// todo maybe better hashing
		return this.intValue();
	}
	
	@Override
	public int intValue()
	{
		return (int)this.value;
	}
	
	public boolean isInfinite()
	{
		return Double.isInfinite(this.value);
	}
	
	public static boolean isInfinite(double v)
	{
		return v == POSITIVE_INFINITY || v == NEGATIVE_INFINITY;
	}

	public boolean isNaN()
	{
		return Double.isNaN(this.value);
	}
	
	public static boolean isNaN(double val)
	{
		return val != val;
	}
	
	@Override
	public long longValue()
	{
		return (long)this.value;
	}
	
	@Override
	public short shortValue()
	{
		return (short)this.value;
	}
	
    @Override
    public String toString()
    {
    	return "<Double>";
    }

	public static String toString(double d)
	{
		return "<Double>";
	}
	
	public static Double valueOf(double d)
	{
		return new Double(d);
	}
}
