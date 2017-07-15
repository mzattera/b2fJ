package java.util;

/** 
 * An object that maps keys to values. A map cannot contain duplicate keys; each key can map to at most one value. 
 *
 * @author Michael Mirwaldt
 *
 */
public interface Map<K,V> {

	/**
	 * A map entry (key-value pair).  The <tt>Map.entrySet</tt> method returns
     * a collection-view of the map, whose elements are of this class.  The
     * <i>only</i> way to obtain a reference to a map entry is from the
     * iterator of this collection-view.
     * 
	 */
	interface Entry<K,V> {
		/**
		 * Compares the specified object with this entry for equality.
		 * 
		 */
		 boolean equals(Object o);
         
		 /**
		  * Returns the key corresponding to this entry.
		  * 
		  */
		 K getKey();
         
		 /**
		  * Returns the value corresponding to this entry.
		  * 
		  */
		 V getValue();
         
		 /**
		  * Returns the hash code value for this map entry.
		  * 
		  */
		 int hashCode();
         
		 /**
		  * Replaces the value corresponding to this entry with the specified value (optional operation).
		  * 
		  */
		 V setValue(V value);         
	}
	
	/**
     * Removes all mappings from this map (optional operation).
     * 
     */
	 void clear();
	 
	/**
	 * Returns true if this map contains a mapping for the specified key.
	 * 
	 */
	boolean containsKey(Object key);
     
	/**
	 * Returns true if this map maps one or more keys to the specified value.
	 * 
	 */
	boolean containsValue(Object value);
     
	/**
	 * Returns a set view of the mappings contained in this map.
	 * 
	 */
	Set<Map.Entry<K,V>> entrySet();
     
	/**
	 * Compares the specified object with this map for equality.
	 */
	boolean equals(Object o);
     
	/**
	 * Returns the value to which this map maps the specified key.
	 * 
	 */
	V get(Object key);
     
	/**
	 * Returns the hash code value for this map.
	 * 
	 */
	int hashCode();
     
	/**
	 * Returns true if this map contains no key-value mappings.
	 * 
	 */
	boolean isEmpty();
     
	/**
	 * Returns a set view of the keys contained in this map.
	 * 
	 */
	Set<K> 	keySet();
     
	/**
	 * Associates the specified value with the specified key in this map (optional operation).
	 * 
	 */
	V put(K key, V value);
     
	/**
	 * Copies all of the mappings from the specified map to this map (optional operation).
	 * 
	 */
	void putAll(Map<? extends K,? extends V> t);
     
	/**
	 * Removes the mapping for this key from this map if it is present (optional operation).
	 * 
	 */
	V remove(Object key);
     
	/**
	 * Returns the number of key-value mappings in this map.
	 * 
	 */
	int size();
     
	/**
	 * Returns a collection view of the values contained in this map.
	 * 
	 */
	Collection<V> values();
     
}
