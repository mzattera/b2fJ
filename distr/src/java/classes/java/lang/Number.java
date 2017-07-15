package java.lang;

/**
 * Superclass for the difference wrapper classes.
 * @author Sven Köhler
 */
public abstract class Number
{
	//MISSING implements Serializable
	
	/**
	 * Just a constructor doing nothing.
	 */
	public Number()
	{
		//nothing to do
	}
	
	/**
	 * Return the byte value of this Number.
	 * @return the byte value
	 */
	public byte byteValue()
	{
		return (byte)this.intValue();
	}
	
	/**
	 * Return the short value of this Number.
	 * @return the short value
	 */
	public short shortValue()
	{
		return (short)this.intValue();
	}
	
	/**
	 * Return the int value of this Number.
	 * @return the int value
	 */
	public abstract int intValue();
	
	/**
	 * Return the long value of this Number.
	 * @return the long value
	 */
	public abstract long longValue();
	
	/**
	 * Return the float value of this Number.
	 * @return the float value
	 */
	public abstract float floatValue();
	
	/**
	 * Return the double value of this Number.
	 * @return the double value
	 */
	public abstract double doubleValue();
}
