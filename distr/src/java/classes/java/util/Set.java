package java.util;

/** 
 * A collection that contains no duplicate elements.  More formally, sets
 * contain no pair of elements <code>e1</code> and <code>e2</code> such that
 * <code>e1.equals(e2)</code>, and at most one null element.  As implied by
 * its name, this interface models the mathematical <i>set</i> abstraction.
 * 
 * @author Michael Mirwaldt
 *
 */
public interface Set<E> extends Collection<E> {
	
	/** 
	 * Adds the specified element to this set if it is not already present
	 * 
	 */
	boolean add(E o);
	
	/** 
	 * Adds all of the elements in the specified collection to this set if they're not already present.
	 * 
	 */
	boolean addAll(Collection<? extends E> c);
	
	/** 
	 * Removes all of the elements from this set
	 * 
	 */
	void clear() ;
	
	/** 
	 * Returns true if this set contains the specified element.
	 * 
	 */
	boolean contains(Object o);
	
	/** 
	 * Returns true if this set contains all of the elements of the specified collection.
	 * 
	 */
	boolean containsAll(Collection<?> c); 
	
	/** 
	 * Compares the specified object with this set for equality.
	 * 
	 */
	boolean equals(Object o); 

	/** Returns the hash code value for this set.
	 * 
	 */
	int hashCode(); 
	
	/** 
	 * Returns true if this set contains no elements.
	 * 
	 */
	boolean isEmpty(); 
	
	/** 
	 * Returns an iterator over the elements in this set.
	 * 
	 */
	Iterator<E> iterator();
	
	/** 
	 * Removes the specified element from this set if it is present.
	 * 
	 */
	boolean remove(Object o);
	
	/** 
	 * Removes the specified element from this set if it is present.
	 *     
	 */
	boolean removeAll(Collection<?> c);
	
	/** 
	 * Removes from this set all of its elements that are contained in the specified collection.
	 * 
	 */
	boolean retainAll(Collection<?> c);
	
    /** 
     * Retains only the elements in this set that are contained in the specified collection.
     * 
     */
    int size();
    
    /** 
     * Returns the number of elements in this set (its cardinality).
     * 
     */
    Object[] toArray();
    
    /** 
     * Returns an array containing all of the elements in this set.
     * 
     */
	<T> T[] toArray(T[] a);
}
