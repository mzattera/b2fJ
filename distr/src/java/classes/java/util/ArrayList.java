package java.util;

/**
 * An expandable array.
 * 
 * @author Andre Nijholt
 * @author Sven Köhler
 * @param <E> type of the elements
 */
public class ArrayList<E> extends AbstractList<E> implements RandomAccess
{
	private static final int INITIAL_CAPACITY = 10;
	private static final int CAPACITY_INCREMENT_NUM = 3;	//numerator of the increment factor
	private static final int CAPACITY_INCREMENT_DEN = 2;	//denominator of the increment factor
	
	//MISSING implements Clonable
	//MISSING implements Serializable
	
	private class MyIterator implements ListIterator<E>
	{
		private int modcount;
		private int lastpos;
		private int nextpos;

		public MyIterator(int index)
		{
			this.lastpos = -1;
			this.nextpos = index;
			this.modcount = ArrayList.this.modCount;
		}
		
		private void checkModCount()
		{
			if (this.modcount != ArrayList.this.modCount)
				throw new ConcurrentModificationException();
		}

		private void checkLastPos()
		{
			if (this.lastpos < 0)
				throw new IllegalStateException();
		}
		
		public boolean hasNext()
		{
			return this.nextpos < ArrayList.this.size();
		}

		public boolean hasPrevious()
		{
			return this.nextpos > 0;
		}

		public E next()
		{
			this.checkModCount();
			if (!this.hasNext())
				throw new NoSuchElementException();
			
			return ArrayList.this.get(this.lastpos = this.nextpos++);
		}

		public E previous()
		{
			this.checkModCount();
			if (!this.hasPrevious())
				throw new NoSuchElementException();
			
			return ArrayList.this.get(this.lastpos = --this.nextpos);
		}

		public void remove()
		{
			this.checkLastPos();
			this.checkModCount();
			ArrayList.this.remove(this.lastpos);
			this.modcount = ArrayList.this.modCount;
			this.lastpos = -1;
		}
		
		public void set(E e)
		{
			this.checkLastPos();
			this.checkModCount();
			ArrayList.this.set(this.lastpos, e);
		}

		public void add(E e)
		{
			this.checkModCount();
			ArrayList.this.add(this.nextpos, e);
			this.modcount = ArrayList.this.modCount;
			this.lastpos = -1;
		}

		public int nextIndex()
		{
			return this.nextpos;
		}

		public int previousIndex()
		{
			return this.nextpos - 1;
		}

	}
	

	private int elementCount;
	private Object[] elementData;
	
	/**
	 * Create an array list.
	 */
	public ArrayList()
	{
		elementCount = 0;
		elementData = new Object[INITIAL_CAPACITY];
	}
	
	public ArrayList(Collection<? extends E> c)
	{
		this(c.size());
		this.addAll(c);
	}

	/**
   	 * Create an array list.
	 *
	 * @param initialCapacity The initial size of the array list.
	 */
	public ArrayList(int initialCapacity)
	{
		if (initialCapacity < 0)
			// throw new IllegalArgumentException("capacity is negative");
			throw new IllegalArgumentException();
		
		elementCount = 0;
		elementData = new Object[initialCapacity];
	}

	/**
	 * Create an array list.
	 * 
	 * @param elements The initial elements in the array list.
	 * @deprecated not in JDK
	 */
	@Deprecated
	public ArrayList(E[] elements)
	{
		this((elements.length * 13) / 10);
		addAll(elements);
	}

	/**
	 * Add a element at a specific index.
	 *
	 * @param index The index at which the element should be added.
	 * @param element The element to add.
	 */
	public void add(int index, E element) 
	{
		if (index < 0 || index > elementCount)
			throw new IndexOutOfBoundsException();

		ensureCapacity(elementCount + 1);
    	//overlapping source/target area should work
		System.arraycopy(elementData, index, elementData, index+1, elementCount-index);
		elementData[index] = element;
		elementCount++;
	}

	/**
	 * Add a element at the end of the array list.
	 * 
	 * @param element The element to add.
	 */
	public boolean add(E element)
	{
		ensureCapacity(elementCount + 1);
		elementData[elementCount++] = element;
		return true;
	}

	/**
	 * Add all elements from the array to the array list.
	 * 
	 * @param elements The array of elements to add.
	 * @deprecated not in JDK
	 */
	@Deprecated
	public void addAll(E[] elements)
	{
		int len = elements.length;
		if (len <= 0)
			return;
		
		ensureCapacity(elementCount + len);

		System.arraycopy(elements, 0, elementData, elementCount, len);
		elementCount += len;
	}

	/**
	 * Add all elements from the array to the array list at a specific index.
	 * 
	 * @param index The index to start adding elements.
	 * @param elements The array of elements to add.
	 * @deprecated not in JDK
	 */
	@Deprecated
	public void addAll(int index, E[] elements)
	{
		if (index < 0 || index > elementCount)
			throw new IndexOutOfBoundsException();
		
		int len = elements.length;
		if (len <= 0)
			return;
		
		ensureCapacity(elementCount + len);
		System.arraycopy(elementData, index, elementData, index+len, elementCount - index);
		
		System.arraycopy(elements, 0, elementData, index, len);
		
		elementCount += len;
	}

	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		return this.addAll(elementCount, c);
	}

	public boolean addAll(int index, Collection<? extends E> c)
	{
		if (index < 0 || index > elementCount)
			throw new IndexOutOfBoundsException();
		
		int size = c.size();
		if (size <= 0)
			return false;
		
		this.ensureCapacity(elementCount + size);		
		System.arraycopy(elementData, index, elementData, index+size, elementCount - index);

		Iterator<? extends E> i = c.iterator();
		for (int j=0; j<size; j++)
			elementData[index + j] = i.next();
		
		elementCount += size;		
		return true;
	}

	/**
	 * Clear the array list.
	 */
	public void clear()
	{
		modCount++;
		
		for (int i = 0; i < elementCount; i++)
			elementData[i] = null;
		
		elementCount = 0;
	}
	
	/**
	 * Ensure that we have sufficient capacity in the array to store
	 * the requested number of elements. Expand the array if required.
	 * @param minCapacity
	 */
	public void ensureCapacity(int minCapacity)
	{
		modCount++;
		
		int el = elementData.length;
		if (el < minCapacity)
		{
			el = el * CAPACITY_INCREMENT_NUM / CAPACITY_INCREMENT_DEN + 1;
			while (el < minCapacity)
				el = el * CAPACITY_INCREMENT_NUM / CAPACITY_INCREMENT_DEN + 1;
			
			Object[] newData = new Object[el];
			System.arraycopy(elementData, 0, newData, 0, elementCount);
			elementData = newData;
		}
	}

	/**
	 * Get a specific element.
	 * 
	 * @param index The index of the wanted element.
	 * @return The wanted element.
	 */
	@SuppressWarnings("unchecked")
	public E get(int index)
	{
		if (index < 0 || index >= elementCount)
			throw new IndexOutOfBoundsException();
		
		return (E)elementData[index];
	}

	/**
	 * Get the first index of a specific element.
	 * 
	 * @param element The wanted element.
	 * @return The index of the wanted element, or -1 if not found.
	 */
	public int indexOf(Object element)
	{
		if (element == null)
		{
			for (int i = 0; i < elementCount; i++)
				if (elementData[i] == null)
					return i;
		}
		else
		{
			for (int i = 0; i < elementCount; i++)
				if (element.equals(elementData[i]))
					return i;
		}
		return -1;
	}

	/**
	 * Get the last index of a specific element.
	 * 
	 * @param element The wanted element.
	 * @return The index of the wanted element, or -1 if not found.
	 */
	public int lastIndexOf(Object element)
	{
		if (element == null)
		{
			for (int i = elementCount -1; i >= 0; i--)
				if (elementData[i] == null)
					return i;
		}
		else
		{
			for (int i = elementCount -1; i >= 0; i--)
				if (element.equals(elementData[i]))
					return i;
		}
		return -1;
	}

	/**
	 * Remove a element at a specific index.
	 *
	 * @param index The index of the element to remove.
	 * @return the removed element.
	 */
	@SuppressWarnings("unchecked")
	public E remove(int index)
	{
		if (index < 0 || index >= elementCount)
			throw new IndexOutOfBoundsException();

		modCount++;
		
		Object element = elementData[index];
		System.arraycopy(elementData, index + 1, elementData, index, elementCount - index - 1);
		elementData[--elementCount] = null;

		return (E)element;
	}

	public boolean remove(Object o)
	{
		int i = this.indexOf(o);
		if (i < 0)
			return false;
		
		this.remove(i);
		return true;
	}
	
	protected void removeRange(int start, int end)
	{
		if (start <= 0)
			throw new IndexOutOfBoundsException();
		if (end > elementCount)
			throw new IndexOutOfBoundsException();
		if (start > end)
			throw new IndexOutOfBoundsException();		
		if (start == end)		
			return;
		
		this.modCount++;
		
		int oldcount = elementCount;
		int newcount = oldcount - end + start;
		
		System.arraycopy(elementData, end, elementData, start, oldcount - end);
		for (int i=newcount; i<oldcount; i++)
			elementData[i] = null;
		
		elementCount = newcount;
	}

	/**
	 * Replace an element at a specific index with a new element.
	 * 
	 * @param index The index of the element to set.
	 * @param element The new element.
	 * @return the old element.
	 */
	@SuppressWarnings("unchecked")
	public E set(int index, E element)
	{
		if (index >= elementCount)
			throw new IndexOutOfBoundsException();

		Object o = elementData[index];
		elementData[index] = element;
		
		return (E)o;
	}

	/**
	 * Get the number of elements in this array list.
	 * 
	 * @return the number of elements.
	 */
	public int size()
	{
		return elementCount;
	}
	
	public ListIterator<E> listIterator(int index)
	{
		return new MyIterator(index);
	}

	public List<E> subList(int start, int end)
	{
		//TODO implement
		throw new UnsupportedOperationException("subList not yet supported");
	}
	
	public void trimToSize()
	{
		if (elementCount < elementData.length)
		{
			modCount++;
			
			Object[] newData = new Object[elementCount];
			System.arraycopy(elementData, 0, newData, 0, elementCount);
			elementData = newData;
		}
	}
}
