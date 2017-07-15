package java.util;

/**
 * This is non-public because it's not compatible with the JDK, yet.
 * 
 * @author Sven Köhler
 * @param <E> type of the elements
 */
abstract class AbstractList<E> extends AbstractCollection<E> implements List<E>
{
	protected transient int modCount = 0;
	
	public boolean contains(Object o)
	{
		return this.indexOf(o) >= 0;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
			return true;
		if (!(o instanceof List<?>))
			return false;
		
		Iterator<?> it1 = this.iterator();
		Iterator<?> it2 = ((List<?>)o).iterator();
		
		boolean n1, n2;
		while (true)
		{
			n1 = it1.hasNext();
			n2 = it2.hasNext();
			if (!n1 || !n2)
				break;
			
			Object o1 = it1.next();
			Object o2 = it2.next();
			
			if (o1 != o2 && (o1 == null || o2 == null || !o1.equals(o2)))
			{
				return false;
			}
		}
		
		return n1 == n2;
	}

	@Override
	public int hashCode()
	{
		int r = 1;
		for (Object o : this)
			r = 31 * r + (o == null ? 0 : o.hashCode());
		
		return r;
	}
	
	public Iterator<E> iterator()
	{
		return this.listIterator();
	}
	
	public ListIterator<E> listIterator()
	{
		return this.listIterator(0);
	}
}
