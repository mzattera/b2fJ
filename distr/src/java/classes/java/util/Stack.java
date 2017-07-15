package java.util;

/*
 * $Log: Stack.java,v $
 * Revision 1.2  2005/11/23 17:46:45  mpscholz
 * minor javadoc related changes
 *
 * Revision 1.1  2003/08/17 14:59:42  mpscholz
 * enhanced Vector
 * added Stack and Queue and associated exception classes
 */

/**
 * A LIFO stack of objects. TODO recycable
 */
public class Stack<E> extends Vector<E>
{
	/**
	 * creates a new Stack instance
	 */
	public Stack()
	{
		super(7, 0);
	}

	/**
	 * pushes an object onto the stack
	 * 
	 * @param anObject the object
	 * @return Object the object pushed onto the stack
	 */
	public E push(E anObject)
	{
		// add the object to base vector
		addElement(anObject);
		return anObject;
	}

	/**
	 * fetches an object from the top of the stack and removes it
	 * 
	 * @return Object the object removed from the top of the stock
	 * @throws EmptyStackException
	 */
	public synchronized E pop()
	{
		int idx = size() - 1;
		if (idx < 0)
			throw new EmptyStackException();
		// get object
		E popped = elementAt(idx);
		// remove and return object
		removeElementAt(idx);
		return popped;
	} // pop()

	/**
	 * fetches an object from the stack <br>
	 * does not remove it!
	 * 
	 * @return Object the object at the top of the stack
	 * @throws EmptyStackException
	 */
	public synchronized E peek()
	{
		int idx = size() - 1;
		if (idx < 0)
			throw new EmptyStackException();
		// return top element
		return elementAt(idx);
	}

	/**
	 * is this stack empty?
	 * 
	 * @return boolean true, if the stack is empty
	 */
	public boolean empty()
	{
		return (size() == 0);
	}

	public synchronized int search(Object o)
	{
		int i = this.lastIndexOf(o);
		return (i < 0) ? i : this.size() - i;
	}

}
