package java.util;

/**
 * @author Sven Köhler
 * @param <E> type of the elements
 */
public interface Collection<E> extends Iterable<E>
{
	boolean add(E e);
	boolean remove(Object o);
	
	boolean addAll(Collection<? extends E> c);
	boolean removeAll(Collection<?> c);
	boolean retainAll(Collection<?> c);
	
	boolean contains(Object o);
	boolean containsAll(Collection<?> c);
	
	boolean equals(Object o);
	int hashCode();
	
	int size();
	boolean isEmpty();
	void clear();
		
	Iterator<E> iterator();
	
	Object[] toArray();
	<T> T[] toArray(T[] a);
}
