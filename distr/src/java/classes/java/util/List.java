package java.util;

/**
 * @author Sven Köhler
 * @param <E> type of the elements
 */
public interface List<E> extends Collection<E>
{
	void add(int index, E e);
	boolean addAll(int index, Collection<? extends E> c);
	
	E get(int index);
	E set(int index, E e);
	E remove(int index);
	
	int indexOf(Object o);
	int lastIndexOf(Object o);
	
	ListIterator<E> listIterator();
	ListIterator<E> listIterator(int index);
	
	List<E> subList(int start, int end);
}
