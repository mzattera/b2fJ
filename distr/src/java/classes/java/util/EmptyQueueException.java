package java.util;
/*
* $Log: EmptyQueueException.java,v $
* Revision 1.1  2003/08/17 14:59:42  mpscholz
* enhanced Vector
* added Stack and Queue and associated exception classes
*
*/

/////////////////////////////////////////////////////////
/**
 * An exception thrown by some Queue class methods
 * to indicate that the Queue is empty 
 */
public class EmptyQueueException extends RuntimeException {

	////////////////////////////////////////////
	// constants
	////////////////////////////////////////////
    
	////////////////////////////////////////////
	// fields
	////////////////////////////////////////////

	////////////////////////////////////////////
	// constructors
	////////////////////////////////////////////

	////////////////////////////////////////////
	/**
	 * creates a new exception wit null message string
	 */
    public EmptyQueueException() {
    } // EmptyQueueException()

} // class EmptyQueueException
