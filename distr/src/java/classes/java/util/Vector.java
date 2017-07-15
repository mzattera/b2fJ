package java.util;

/*
 * $Log: Vector.java,v $
 * Revision 1.6  2005/11/23 17:46:45  mpscholz
 * minor javadoc related changes
 *
 * Revision 1.5  2003/08/17 14:59:42  mpscholz
 * enhanced Vector
 * added Stack and Queue and associated exception classes
 *
 */

/**
 * A dynamic array.
 */
public class Vector<E>
{
	private class MyEnumeration implements Enumeration<E>
	{
		int cur = 0;

		public boolean hasMoreElements()
		{
			return (size() > cur);
		}

		public E nextElement()
		{
			return elementAt(cur++);
		}
	}

	protected Object[] elementData;
	protected int capacityIncrement;
	protected int elementCount;

	public Vector()
	{
		this(7, 0);
	}

	public Vector(int initialCapacity)
	{
		this(initialCapacity, 0);
	}

	public Vector(int initialCapacity, int capacityIncrement)
	{
		if (initialCapacity < 0)
			// throw new IllegalArgumentException("capacity is negative");
			throw new IllegalArgumentException();

		elementData = new Object[initialCapacity];
		this.capacityIncrement = capacityIncrement;
		elementCount = 0;
	}

	/**
	 * Returns a convenient Enumaration object to cycle through elements in
	 * Vector.
	 * 
	 * @return an Enumeration of all the objects in the Vector
	 */
	public Enumeration<E> elements()
	{
		return new MyEnumeration();
	}

	public synchronized void addElement(E aObj)
	{
		ensureCapacityHelper(elementCount + 1);
		elementData[elementCount++] = aObj;
	}

	public int capacity()
	{
		return elementData.length;
	}

	public void clear()
	{
		removeAllElements();
	}

	@SuppressWarnings("unchecked")
	public synchronized E elementAt(int aIndex)
	{
		if ((aIndex < 0) && (aIndex >= elementCount))
			throw new ArrayIndexOutOfBoundsException();

		return (E)elementData[aIndex];
	}

	public synchronized void ensureCapacity(int minCapacity)
	{
		ensureCapacityHelper(minCapacity);
	}

	// internal unsynchronized method for better performance

	private void ensureCapacityHelper(int minCapacity)
	{
		if (elementData.length < minCapacity)
		{
			int pNewCapacity;
			if (capacityIncrement > 0)
				pNewCapacity = elementData.length + capacityIncrement;
			else
				pNewCapacity = elementData.length * 2;

			if (pNewCapacity < minCapacity)
				pNewCapacity = minCapacity;

			Object newData[] = new Object[pNewCapacity];
			System.arraycopy(elementData, 0, newData, 0, elementCount);
			elementData = newData;
		}
	}

	@Override
	public boolean equals(Object aObj)
	{
		if (aObj instanceof Vector<?>)
			return false;
		
		Vector<?> v = (Vector<?>)aObj;
		if (this.elementCount != v.elementCount)
			return false;
		
		for (int i=0; i<elementCount; i++)
		{
			Object o1 = elementData[i];
			Object o2 = v.elementData[i];
			
			if (o1 != o2 && (o1 == null || o2 == null || !o1.equals(o2)))
			{
				return false;
			}
		}
		
		return true;
	}

	public int indexOf(Object aObj)
	{
		return indexOf(aObj, 0);
	}

	public synchronized int indexOf(Object aObj, int aIndex)
	{
		if (aObj == null)
		{
			for (int i = aIndex; i < elementCount; i++)
			{
				if (elementData[i] == null)
					return i;
			}
		}
		else
		{
			for (int i = aIndex; i < elementCount; i++)
			{
				if (elementData[i].equals(aObj))
					return i;
			}
		}

		return -1;
	}

	public synchronized void insertElementAt(E aObj, int aIndex)
	{
		if (aIndex < 0 || aIndex > elementCount)
			throw new ArrayIndexOutOfBoundsException();
		
		ensureCapacityHelper(elementCount + 1);
		System.arraycopy(elementData, aIndex, elementData, aIndex+1, elementCount - aIndex);
		elementData[aIndex] = aObj;
		elementCount++;
	}

	public boolean isEmpty()
	{
		return (elementCount == 0);
	}

	/**
	 * delivers the index of the last occurrence of the object
	 * 
	 * @param anObject the object
	 * @return the index of the last occurrence of the object or -1, if object
	 *         is not found
	 */
	public synchronized int lastIndexOf(Object anObject)
	{
		return lastIndexOf(anObject, elementCount - 1);
	}

	/**
	 * delivers the index of the last occurrence of the object starting from
	 * some index
	 * 
	 * @param anObject the object
	 * @param anIndex the starting index
	 * @return the index of the last occurrence of the object or -1, if object
	 *         is not found
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public synchronized int lastIndexOf(Object anObject, int anIndex) throws ArrayIndexOutOfBoundsException
	{
		if (anObject == null)
		{
			for (int i = anIndex; i >= 0; i--)
				if (elementData[i] == null)
					return i;
		}
		else
		{
			for (int i = anIndex; i >= 0; i--)
				if (anObject.equals(elementData[i]))
					return i;
		}
		return -1;
	}

	public void removeAllElements()
	{
		for (int i = 0; i < elementCount; i++)
			elementData[i] = null;

		elementCount = 0;
	}

	public synchronized boolean removeElement(E aObj)
	{
		int index = indexOf(aObj);
		if (index < 0)
			return false;
		
		removeElementAt(index);
		return true;
	}

	public synchronized void removeElementAt(int aIndex)
	{
		if ((aIndex < 0) || (aIndex >= elementCount))
			throw new ArrayIndexOutOfBoundsException();

		System.arraycopy(elementData, aIndex + 1, elementData, aIndex, elementCount - aIndex - 1);
		elementData[--elementCount] = null;
	}

	public synchronized void setElementAt(E aObj, int aIndex)
	{
		if (aIndex < 0 || aIndex >= elementCount)
			throw new ArrayIndexOutOfBoundsException();

		elementData[aIndex] = aObj;
	}

	public synchronized void setSize(int aSize)
	{
		if (elementCount < aSize)
		{
			ensureCapacityHelper(aSize);
		}
		else
		{
			for (int i = aSize; i < elementCount; i++)
				elementData[i] = null;
		}

		elementCount = aSize;
	}

	public synchronized int size()
	{
		return elementCount;
	}

	public synchronized Object[] toArray()
	{
		return this.toArray(new Object[elementCount]);
	}

	public synchronized <T> T[] toArray(T[] dest)
	{
		if (this.elementCount > dest.length)
			throw new UnsupportedOperationException("Array is too small and expanding is not supported.");

		System.arraycopy(this.elementData, 0, dest, 0, elementCount);		
		return dest;
	}

	@Override
	public String toString()
	{
		if (elementCount < 1)
			return "[]";
		
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		
		sb.append(String.valueOf(elementData[0]));
		for (int i = 1; i < elementCount; i++)
			 sb.append(", ").append(String.valueOf(elementData[i]));

		return sb.append(']').toString();
	}

	public synchronized void trimToSize()
	{
		if (elementCount < elementData.length)
		{
			Object[] newData = new Object[elementCount];
			System.arraycopy(elementData, 0, newData, 0, elementCount);
			elementData = newData; 
		}
	}

}
