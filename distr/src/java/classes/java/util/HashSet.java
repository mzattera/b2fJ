package java.util;

/**
 * 
 * @author Michael Mirwaldt
 * @deprecated Current implementation is inefficient. Don't use it if your care about performance.
 *  Will be replaced with efficient implementation.
 */
@Deprecated
public class HashSet<E> extends AbstractCollection<E> implements Set<E> {
	//TODO rework this implementation (Michael Mirwaldt promised to deliver update)

	private Map<Integer, LinkedList<E>> map = new HashMap<Integer, LinkedList<E>>();
	
	private transient int modCount = 0;
	private transient int size = 0;

	public HashSet() {
		super();
	}
	
	public HashSet(Collection<? extends E> rhs) {
		super();
		addAll(rhs);
	}

	public boolean add(E e) {
		final int hash = hash(e.hashCode());
		if(!map.containsKey(hash)) {
			modCount++;
			size++;
			final LinkedList<E> linkedList = new LinkedList<E>();
			linkedList.add(e);
			map.put(hash, linkedList);
			return true;
		}

		final List<E> list = map.get(hash);
		final boolean contains = list.contains(e);
		if(!contains) {
			modCount++;
			size++;
			list.add(e);
		}
		return !contains;
	}

	public void clear() {
		modCount++;
		size = 0;
		map.clear();		
	}

	public boolean contains(Object o) {
		final int hash = hash(o.hashCode());
		return map.containsKey(hash) && map.get(hash).contains(o);
	}

	public Iterator<E> iterator() {
		return new SetIterator(0);
	}

	public boolean remove(Object o) {
		final int hash = hash(o.hashCode());
		if(!contains(o))
			return false;
			
		final List<E> list = map.get(hash);
		final boolean contains = list.contains(o);
		if(contains) {
			modCount++;
			size--;
			list.remove(o);
		}
		if(list.isEmpty()) {
			map.remove(hash);
		}
		return contains;
	}

	public int size() {
		return size;
	}
	
	private static <E> List<E> flattenToList(Map<Integer, LinkedList<E>> map) {
		List<E> allElements = new ArrayList<E>();
		for (LinkedList<E> list : map.values()) {
			allElements.addAll(list);
		}
		return allElements;
	}

	class SetIterator implements Iterator<E> {

		private int modcount;
		private int nextpos;
		private List<E> allElements;
		
		private SetIterator(int index) {
			super();
			allElements = flattenToList(HashSet.this.map);
			nextpos = index;
			this.modcount = HashSet.this.modCount;
		}

		private void checkModCount()
		{
			if (this.modcount != HashSet.this.modCount)
				throw new ConcurrentModificationException();
		}
		
		public boolean hasNext()
		{
			return this.nextpos < allElements.size();
		}

		public E next() {
			this.checkModCount();
			if (!this.hasNext())
				throw new NoSuchElementException();
			
			return allElements.get(this.nextpos++);
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	@Override
	public int hashCode() {
		int h = 0;
		Iterator<E> i = iterator();
		while (i.hasNext()) {
		    E obj = i.next();
	            if (obj != null)
	                h += obj.hashCode();
	        }
		return h;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
		    return true;

		if (!(o instanceof Set))
		    return false;
		Collection<?> c = (Collection<?>) o;
		if (c.size() != size())
		    return false;
        try {
            return containsAll(c);
        } catch (ClassCastException unused)   {
            return false;
        } catch (NullPointerException unused) {
            return false;
        }
	}
	
	private static int hash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
}
