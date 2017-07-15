package java.util;

/**
 * This is non-public because it's not compatible with the JDK, yet.
 * 
 * @author Sven Köhler
 * @param <E> type of the elements
 */
abstract class AbstractCollection<E> implements Collection<E>
{
	public boolean addAll(Collection<? extends E> c)
	{
		boolean r = false;
		for (E element : c)
			r |= this.add(element);
		
		return r;
	}

	public boolean containsAll(Collection<?> c)
	{
		for (Object o : c)
			if (!this.contains(o))
				return false;
		
		return true;
	}

	public boolean isEmpty()
	{
		return this.size() <= 0;
	}

	public boolean removeAll(Collection<?> c)
	{
		boolean r = false;
		for (Object o : c)
			r |= this.remove(o);
		
		return r;
	}

	public boolean retainAll(Collection<?> c)
	{
		boolean r = false;
		for (Iterator<E> i = this.iterator(); i.hasNext();)
		{
			E element = i.next();			
			if (!c.contains(element))
			{
				r = true;
				i.remove();
			}
		}
		
		return r;
	}

	public Object[] toArray()
	{
		int size = this.size();
		return this.toArray(new Object[size]);
	}

	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] dest)
	{
		int j = 0;
		int max = dest.length;
		
		for (E element : this)
		{
			if (j >= max)
				throw new UnsupportedOperationException("Array is too small and expanding is not supported.");
			
			//whether elements are compatible with dest can only be checked at runtime
			dest[j++] = (T)element; 
		}
		
		return dest;
	}

    @Override
	public String toString()
    {
    	StringBuilder sb = new StringBuilder();
    	sb.append('[');
    	
    	Iterator<?> it = this.iterator();
    	if (it.hasNext())
    	{
    		sb.append(it.next());
    		while (it.hasNext())
    		{
    			//Note: if <code>this</code> is returned by the iterator, the JDK appends "(this Collection)"
    			//However: this is not documented anywhere, and it workarounds a stack overflow only for very simple scenarios.
    			//Also related: http://stackoverflow.com/questions/995477/why-does-java-tostring-loop-infinitely-on-indirect-cycles
    			sb.append(", ");
    			sb.append(it.next());
			}
    	}
    	sb.append(']');
    	return sb.toString();
    }
}
