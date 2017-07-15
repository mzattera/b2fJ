package java.util;

/**
 * Exception thrown by Iterators if the underlying connection has been modified during the iteration.
 * @author Sven Köhler
 */
public class ConcurrentModificationException extends RuntimeException
{
	public ConcurrentModificationException()
	{
		super();
	}

	public ConcurrentModificationException(String message)
	{
		super(message);
	}
}
