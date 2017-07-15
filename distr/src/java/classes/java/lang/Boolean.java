package java.lang;

/**
 * Wrapper class for booleans.
 * @author Sven Köhler
 */
public final class Boolean implements Comparable<Boolean>
{
	public static final Boolean FALSE = new Boolean(false);
	public static final Boolean TRUE = new Boolean(true);
	
	//MISSING implements Serializable
	
	private final boolean value;
	
	public Boolean(boolean b)
	{
		this.value = b;
	}
	
	public Boolean(String s)
	{
		this.value = Boolean.parseBoolean(s);
	}
	
	public boolean booleanValue()
	{
		return this.value;
	}
	
	public int compareTo(Boolean ob)
	{
		if (this.value == ob.value)
			return 0;
		
		//false is less than true
		return this.value ? 1 : -1;
	}
	
	@Override
	public boolean equals(Object o)
	{
		//instanceof returns false for o==null
		return (o instanceof Boolean)
			&& (this.value == ((Boolean)o).value);
	}
	
	public static boolean getBoolean(String name)
	{
		return parseBoolean(System.getProperty(name));
	}
	
	@Override
	public int hashCode()
	{
		return this.value ? 1231 : 1237;
	}
	
	public static boolean parseBoolean(String s)
	{
		if (s == null || s.length() != 4)
			return false;
		
		char c0 = s.charAt(0);
		char c1 = s.charAt(1);
		char c2 = s.charAt(2);
		char c3 = s.charAt(3);
		
		return (c0 == 't' || c0 == 'T')
			&& (c1 == 'r' || c1 == 'R')
			&& (c2 == 'u' || c2 == 'U')
			&& (c3 == 'e' || c3 == 'E');
	}
	
	@Override
	public String toString()
	{
    	return String.valueOf(this.value);
	}
	
	public static String toString(boolean b)
	{
		return String.valueOf(b);
	}
	
	public static Boolean valueOf(boolean b)
	{
		return b ? TRUE : FALSE;
	}
	
	public static Boolean valueOf(String s)
	{
		return parseBoolean(s) ? TRUE : FALSE;
	}
}
