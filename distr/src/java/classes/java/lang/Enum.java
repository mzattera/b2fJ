package java.lang;

/**
 * 
 * @author Sven Köhler
 */
public abstract class Enum<E extends Enum<E>> implements Comparable<E>
{
	//MISSING implements Serializable
	
	private final int ordinal;
	private final String name;
	
	protected Enum(String name, int ordinal)
	{
		this.name = name;
		this.ordinal = ordinal;
	}
	
	/**
	 * Notice this only compares ordinals, even for two different Enum types, since Class is not supported.
	 */
	public final int compareTo(E o)
	{
		// the declaration of tmp is required to build with Oracle JDK 1.7 
		// when using o.ordinal directly, javac will complain that ordinal is private
		Enum<E> tmp = o;
		if (this.ordinal == tmp.ordinal)
			return 0;
		
		return (this.ordinal > tmp.ordinal) ? 1 : -1;
	}
	
	@Override
	public final boolean equals(Object o)
	{
		return this==o;
	}

	
	@Override
	public final int hashCode()
	{
		return super.hashCode();
	}
	
	public final String name()
	{
		return this.name;
	}
	
	public final int ordinal()
	{
		return this.ordinal;
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
	
	/**
	 * @deprecated not implemented in leJOS.
	 * Here because otherwise javac will complain. 
	 */
	@Deprecated
	@SuppressWarnings("unused")
	public static<T extends Enum<T>> T valueOf(Class enumclas, String name)
	{
		throw new UnsupportedOperationException();	
	}
}
