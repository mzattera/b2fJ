package java.util;

/**
 * 
 * @author Michael Mirwaldt
 * @deprecated Current implementation is inefficient. Don't use it if your care about performance.
 *  Will be replaced with efficient implementation.
 */
@Deprecated
public class HashMap<K,V> implements Map<K, V> {
	//TODO rework this implementation (Michael Mirwaldt promised to deliver update)
	
	private static final int DEFAULT_ARRAY_LIST_SIZE = 32;

	private LinkedListForEntries<K,V>[] mapList;
	
	private transient int modCount = 0;
	private transient int size = 0;
	
	private transient final int ARRAY_LIST_SIZE;
	
	public HashMap() {
		this(DEFAULT_ARRAY_LIST_SIZE);
	}
	
	/**
	 * 
	 * @param arrayListSize size for array list
	 * 
	 */
	@SuppressWarnings("unchecked")
	public HashMap(int arrayListSize) {
		ARRAY_LIST_SIZE = arrayListSize;
		mapList = new LinkedListForEntries[arrayListSize];
	}
	
	public HashMap(Map<K, V> m) {
		this(DEFAULT_ARRAY_LIST_SIZE);
		putAll(m);
	}
	
	public HashMap(Map<K, V> m, int arrayListSize) {
		this(arrayListSize);
		putAll(m);
	}
	
	public static class Entry<K,V> implements Map.Entry<K,V> {
		final K key;
		V value;
		Entry<K,V> previousEntry;
		Entry<K,V> nextEntry;
		
		/**
		 * Creates new entry.
		 */
		Entry(K k, V v, Entry<K,V> nextEntry, Entry<K,V> previousEntry) {
			value = v;
			key = k;
			this.previousEntry = previousEntry;
			this.nextEntry = nextEntry;
		}

		public final K getKey() {
			return key;
		}

		public final V getValue() {
			return value;
		}

		public final V setValue(V newValue) {
			V oldValue = value;
			value = newValue;
			return oldValue;
		}

		@Override
		public final boolean equals(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			@SuppressWarnings("rawtypes")
			Map.Entry e = (Map.Entry)o;
			Object k1 = getKey();
			Object k2 = e.getKey();
			if (k1 == k2 || (k1 != null && k1.equals(k2))) {
				Object v1 = getValue();
				Object v2 = e.getValue();
				if (v1 == v2 || (v1 != null && v1.equals(v2)))
					return true;
			}
			return false;
		}

		@Override
		public final int hashCode() {
			return (key==null   ? 0 : key.hashCode()) ^
			(value==null ? 0 : value.hashCode());
		}

		@Override
		public final String toString() {
			return getKey() + "=" + getValue();
		}
	    
	}
	
	static class LinkedListForEntries<K,V> implements Iterable<Entry<K, V>> {

		protected transient final Entry<K,V> headerEntry = new Entry<K,V>(null, null, null, null);
		
		public LinkedListForEntries() {
			super();
			headerEntry.nextEntry = headerEntry.previousEntry = headerEntry;
		}

		public boolean add(K k, V v) {
			addBefore(k, v, headerEntry);
			return true;
		}

		public V remove(K k) {
			Entry<K, V> entry = entry(k);
			if(entry!=null)
				return remove(entry);
			return null;
		}
		
		public void clear() {
	        Entry<K,V> e = headerEntry.nextEntry;
	        while (e != headerEntry) {
	            Entry<K,V> next = e.nextEntry;
	            e.nextEntry = e.previousEntry = null;
	            e.value= null;
	            e = next;
	        }
	        headerEntry.nextEntry = headerEntry.previousEntry = headerEntry;
		}
		
		public V get(K k) {
			Entry<K,V> entry = entry(k);
			if(entry==null) {
				return null;
			}
			return entry.value;
		}
		
		public boolean contains(K k) {
			return entry(k)!=null;
		}

		public V set(K k, V v) {
			Entry<K, V> entry = entry(k);
			V oldValue = entry.value;
			entry.value = v;
			return oldValue;
		}

		private V remove(Entry<K,V> e) {
			if (e == headerEntry)
			    throw new NoSuchElementException();

		    V result = e.value;
			e.previousEntry.nextEntry = e.nextEntry;
			e.nextEntry.previousEntry = e.previousEntry;
	        e.nextEntry = e.previousEntry = null;
	        e.value = null;
		    
			return result;
		}
		
	    protected Entry<K,V> addBefore(K k,V v, Entry<K,V> entry) {
	    	Entry<K,V> newEntry = new Entry<K,V>(k, v, entry, entry.previousEntry);
	    	newEntry.previousEntry.nextEntry = newEntry;
	    	newEntry.nextEntry.previousEntry = newEntry;
	    	return newEntry;
	   }
	    
	    /**
	     * Returns the indexed entry.
	     */
	    protected Entry<K, V> entry(K k) {
	        if (k==null) {
	            for (Entry<K,V> e = headerEntry.nextEntry; e != headerEntry; e = e.nextEntry) {
	                if (e.key==null) {
	                	return e;
	                }
	            }
	        } else {
	            for (Entry<K,V> e = headerEntry.nextEntry; e != headerEntry; e = e.nextEntry) {
	                if (k.equals(e.key)) {
	                	return e;
	                }
	            }
	        }
	        return null;
	    }
	    
	    class LinkedListIterator implements Iterator<Entry<K, V>> {

	    	Entry<K, V> curEntry = LinkedListForEntries.this.headerEntry;
	    	
			public boolean hasNext() {
				return curEntry.nextEntry != LinkedListForEntries.this.headerEntry;
			}

			public Entry<K, V> next() {
				Entry<K, V> entry = curEntry.nextEntry;
				curEntry = curEntry.nextEntry;
				return entry;
			}

			public void remove() {
				throw new UnsupportedOperationException();				
			}
	    	
	    }

		public Iterator<Entry<K, V>> iterator() {
			return new LinkedListIterator();
		}
	}

	public void clear() {
		for (int i = 0; i < mapList.length; i++) {
			LinkedListForEntries<K, V> linkedList = getList(i);
			if(linkedList!=null) {
				linkedList.clear();
				mapList[i] = null;
			}
		}
		size = 0;
		modCount++;		
	}

	public boolean containsKey(Object key) {
		return getEntry(key)!=null;
	}

	public boolean containsValue(Object value) {
		return values().contains(value);
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		//TODO implement
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	public V get(Object key) {
		LinkedListForEntries<K, V> linkedList = getList(hash(key.hashCode()));
		if(linkedList!=null) {
			return linkedList.get((K) key);
		}
		return null;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public Set<K> keySet() {
		Set<K> keySet = new HashSet<K>();
		for (java.util.Map.Entry<K, V> entry : entrySet()) {
			keySet.add(entry.getKey());
		}
		return keySet;
	}

	public V put(K key, V value) {
		if(containsKey(key)) {
			Entry<K,V> entry = getEntry(key);
			V oldValue = entry.value;
			entry.setValue(value);
			return oldValue;
		}

		final int hash = hash(key.hashCode());
		final LinkedListForEntries<K,V> list = getList(hash);
		modCount++;
		size++;
		if(list==null) {
			final LinkedListForEntries<K,V> newList = new LinkedListForEntries<K,V>();
			newList.add(key, value);
			setList(hash, newList);
		}
		else {
			list.add(key, value);
		}
		return null;
	}

	public void putAll(Map<? extends K, ? extends V> t) {
        for (Map.Entry<? extends K, ? extends V> e : t.entrySet()) {
        	put(e.getKey(), e.getValue());
        } 				
	}

	public V remove(Object key) {
		if(containsKey(key)) {
			final int hash = hash(key.hashCode());
			final LinkedListForEntries<K, V> list = getList(hash);
			Entry<K, V> entry = getEntry(key);
			V oldValue = entry.getValue();
			list.remove(entry);
			modCount++;
			size--;
			return oldValue;
		}
		return null;
	}

	public int size() {
		return size;
	}

	public Collection<V> values() {
		List<V> list = new ArrayList<V>();
		for (java.util.Map.Entry<K, V> entry : entrySet()) {
			list.add(entry.getValue());
		}
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	private Entry<K,V> getEntry(Object key) {
		final int hash = hash(key.hashCode());
		final LinkedListForEntries<K,V> list = getList(hash);
		if(list!=null) {		
			return list.entry((K) key);			
		}	
		return null;
	}
	
	 /**
     * Compares the specified object with this map for equality.  Returns
     * <tt>true</tt> if the given object is also a map and the two maps
     * represent the same mappings.  More formally, two maps <tt>m1</tt> and
     * <tt>m2</tt> represent the same mappings if
     * <tt>m1.entrySet().equals(m2.entrySet())</tt>.  This ensures that the
     * <tt>equals</tt> method works properly across different implementations
     * of the <tt>Map</tt> interface.
     *
     * <p>This implementation first checks if the specified object is this map;
     * if so it returns <tt>true</tt>.  Then, it checks if the specified
     * object is a map whose size is identical to the size of this map; if
     * not, it returns <tt>false</tt>.  If so, it iterates over this map's
     * <tt>entrySet</tt> collection, and checks that the specified map
     * contains each mapping that this map contains.  If the specified map
     * fails to contain such a mapping, <tt>false</tt> is returned.  If the
     * iteration completes, <tt>true</tt> is returned.
     *
     * @param o object to be compared for equality with this map
     * @return <tt>true</tt> if the specified object is equal to this map
     */
    @Override
	public boolean equals(Object o) {
	if (o == this)
	    return true;

	if (!(o instanceof Map))
	    return false;
	@SuppressWarnings("unchecked")
	Map<K,V> m = (Map<K,V>) o;
	if (m.size() != size())
	    return false;

        try {
            Iterator<java.util.Map.Entry<K, V>> i = entrySet().iterator();
            while (i.hasNext()) {
                java.util.Map.Entry<K, V> e = i.next();
		K key = e.getKey();
                V value = e.getValue();
                if (value == null) {
                    if (!(m.get(key)==null && m.containsKey(key)))
                        return false;
                } else {
                    if (!value.equals(m.get(key)))
                        return false;
                }
            }
        } catch (ClassCastException unused) {
            return false;
        } catch (NullPointerException unused) {
            return false;
        }

	return true;
    }

    /**
     * Returns the hash code value for this map.  The hash code of a map is
     * defined to be the sum of the hash codes of each entry in the map's
     * <tt>entrySet()</tt> view.  This ensures that <tt>m1.equals(m2)</tt>
     * implies that <tt>m1.hashCode()==m2.hashCode()</tt> for any two maps
     * <tt>m1</tt> and <tt>m2</tt>, as required by the general contract of
     * {@link Object#hashCode}.
     *
     * <p>This implementation iterates over <tt>entrySet()</tt>, calling
     * {@link Map.Entry#hashCode hashCode()} on each element (entry) in the
     * set, and adding up the results.
     *
     * @return the hash code value for this map
     * @see Map.Entry#hashCode()
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     */
    @Override
	public int hashCode() {
		int h = 0;
		Iterator<java.util.Map.Entry<K, V>> i = entrySet().iterator();
		while (i.hasNext())
		    h += i.next().hashCode();
		return h;
    }
	

    private LinkedListForEntries<K, V> getList(int i) {
		return mapList[i];
	}
	

    private void setList(int i, LinkedListForEntries<K, V> list) {
		if (i < 0 || i >= mapList.length)
			throw new IndexOutOfBoundsException();
		mapList[i] = list;		
	}

	
	private int hash(int hashCode) {
		return hashCode % ARRAY_LIST_SIZE;
	}
	
}
