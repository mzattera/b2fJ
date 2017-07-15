package java.lang;

public interface Comparable<T>
{
	/**
	 * Compares this with another Object.
	 * Returns -1 if this object is smaller,
	 * 0 if both objects are equal and
	 * 1 if this object is bigger. 
	 * @param o the object to compare with
	 * @return one of the values -1, 0, 1
	 */
	int compareTo(T o);
}
