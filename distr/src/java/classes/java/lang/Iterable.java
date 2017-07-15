package java.lang;

import java.util.Iterator;

/**
 * Interface needed by Java foreach loops. It just provides an Iterator.
 * 
 * @author Sven Köhler
 * @param <E> type of the elements
 */
public interface Iterable<E>
{
	Iterator<E> iterator();
}
