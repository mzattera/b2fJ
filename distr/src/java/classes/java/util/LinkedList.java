package java.util;

/**
 * 
 * @author Michael Mirwaldt
 *
 */
public class LinkedList<E> extends AbstractList<E> {
	
	//TODO review

	private transient final Entry<E> headerEntry = new Entry<E>(null, null, null);
	private transient int size = 0;
	
	static class Entry<E> {
		E element;
		Entry<E> previousEntry;
		Entry<E> nextEntry;
		
		private Entry(E elem, Entry<E> nextEntry, Entry<E> previousEntry) {
			super();
			this.element = elem;
			this.previousEntry = previousEntry;
			this.nextEntry = nextEntry;
		}		
	}
	
	public LinkedList() {
		super();
		headerEntry.nextEntry = headerEntry.previousEntry = headerEntry;
	}
	
    public LinkedList(Collection<? extends E> c) {
    	this();
    	addAll(c);
    }

	public boolean add(E e) {
		addBefore(e, headerEntry);
		return true;
	}

	public boolean remove(Object o) {
        if (o==null) {
            for (Entry<E> e = headerEntry.nextEntry; e != headerEntry; e = e.nextEntry) {
                if (e.element==null) {
                    remove(e);
                    return true;
                }
            }
        } else {
            for (Entry<E> e = headerEntry.nextEntry; e != headerEntry; e = e.nextEntry) {
                if (o.equals(e.element)) {
                    remove(e);
                    return true;
                }
            }
        }
        return false;
	}

	public int size() {
		return size;
	}

	public void clear() {
        Entry<E> e = headerEntry.nextEntry;
        while (e != headerEntry) {
            Entry<E> next = e.nextEntry;
            e.nextEntry = e.previousEntry = null;
            e.element = null;
            e = next;
        }
        headerEntry.nextEntry = headerEntry.previousEntry = headerEntry;
        size = 0;
        modCount++;
	}

	public void add(int index, E e) {
		Entry<E> entry = (index == this.size) ? headerEntry : entry(index);
		addBefore(e, entry);	
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if (c.isEmpty())
			return false;
		
		for (E e : c)
			addBefore(e, this.headerEntry);
				
		return true;
	}
	
	public boolean addAll(int index, Collection<? extends E> c) {
		Entry<E> entry = (index == this.size) ? headerEntry : entry(index);
		if (c.isEmpty())
			return false;
		
		for (E e : c)
			addBefore(e, entry);
				
		return true;
	}

	public E get(int index) {
		return entry(index).element;
	}

	public E set(int index, E e) {
		Entry<E> entry = entry(index);
		E oldValue = entry.element;
		entry.element = e;
		return oldValue;
	}

	public E remove(int index) {
		Entry<E> e = entry(index);
		return remove(e);
	}

	private E remove(Entry<E> e) {
		if (e == headerEntry)
		    throw new NoSuchElementException();

	    E result = e.element;
		e.previousEntry.nextEntry = e.nextEntry;
		e.nextEntry.previousEntry = e.previousEntry;
        e.nextEntry = e.previousEntry = null;
        e.element = null;
		size--;
		modCount++;
	    
		return result;
	}

	public int indexOf(Object o) {
		int index = 0;
        if (o==null) {
            for (Entry<E> e = headerEntry.nextEntry; e != headerEntry; e = e.nextEntry) {
                if (e.element==null)
                    return index;
                index++;
            }
        } 
        else {
            for (Entry<E> e = headerEntry.nextEntry; e != headerEntry; e = e.nextEntry) {
                if (o.equals(e.element))
                    return index;
                index++;
            }
        }
        return -1;
	}

	public int lastIndexOf(Object o) {
        int index = size;
        if (o==null) {
            for (Entry<E> e = headerEntry.previousEntry; e != headerEntry; e = e.previousEntry) {
                index--;
                if (e.element==null)
                    return index;
            }
        } 
        else {
            for (Entry<E> e = headerEntry.previousEntry; e != headerEntry; e = e.previousEntry) {
                index--;
                if (o.equals(e.element))
                    return index;
            }
        }
        return -1;
	}

	public ListIterator<E> listIterator(int index) {
		Entry<E> e = (index == this.size) ? headerEntry : entry(index);
		return new MyIterator(e, index);
	}

	public List<E> subList(int start, int end) {
		//TODO implement
		throw new UnsupportedOperationException();
	}
	
    private Entry<E> addBefore(E e, Entry<E> entry) {
    	Entry<E> newEntry = new Entry<E>(e, entry, entry.previousEntry);
    	newEntry.previousEntry.nextEntry = newEntry;
    	newEntry.nextEntry.previousEntry = newEntry;
    	size++;
    	modCount++;
    	return newEntry;
	}
    
    /**
     * Returns the indexed entry.
     */
    private Entry<E> entry(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: "+index+
                                                ", Size: "+size);
        Entry<E> e = headerEntry;
        if (index < (size/2)) {
            for (int i = 0; i <= index; i++)
                e = e.nextEntry;
        } else {
            for (int i = size; i > index; i--)
                e = e.previousEntry;
        }
        return e;
    }
    
    private class MyIterator implements ListIterator<E>
	{
		private Entry<E> nextEntry;
		private int nextPos;
		private int modcount;

		public MyIterator(Entry<E> curEntry, int curPos)
		{
			this.nextEntry = curEntry;
			this.nextPos = curPos;
			this.modcount = LinkedList.this.modCount;
		}
		
		private void checkModCount()
		{
			if (this.modcount != LinkedList.this.modCount)
				throw new ConcurrentModificationException();
		}

		public boolean hasNext()
		{
			return this.nextEntry != LinkedList.this.headerEntry;
		}

		public boolean hasPrevious()
		{
			return this.nextEntry.previousEntry != LinkedList.this.headerEntry;
		}

		public E next()
		{
			this.checkModCount();
			Entry<E> r = this.nextEntry;
			if (r == LinkedList.this.headerEntry)
				throw new NoSuchElementException();
			
			this.nextPos++;
			this.nextEntry = r.nextEntry;
			return r.element;
		}

		public E previous()
		{
			this.checkModCount();
			Entry<E> newEntry = nextEntry.previousEntry;
			if (newEntry == LinkedList.this.headerEntry)
				throw new NoSuchElementException();
			
			this.nextPos--;
			this.nextEntry = newEntry;
			return newEntry.element;
		}

		public void remove()
		{
			this.checkModCount();
			Entry<E> e = this.nextEntry.previousEntry;
			if (e == LinkedList.this.headerEntry)
				// TODO check with JDK implementation, documentation is much more strict when to throw this 
				throw new IllegalStateException();
			
			LinkedList.this.remove(e);
			this.nextPos--;
		}
		
		public void set(E e)
		{
			this.checkModCount();
			Entry<E> entry = this.nextEntry.previousEntry;
			if (entry == LinkedList.this.headerEntry)
				// TODO check with JDK implementation, documentation is much more strict when to throw this 
				throw new IllegalStateException();
	
			entry.element = e;
		}

		public void add(E e)
		{
			this.checkModCount();
			LinkedList.this.addBefore(e, this.nextEntry);
			this.modcount = LinkedList.this.modCount;
			this.nextPos++;
		}

		public int nextIndex()
		{
			return this.nextPos;
		}

		public int previousIndex()
		{
			return this.nextPos - 1;
		}

	}

}
