package java.util;
/*
* $Log: EmptyStackException.java,v $
* Revision 1.1  2003/08/17 14:59:42  mpscholz
* enhanced Vector
* added Stack and Queue and associated exception classes
*
*/

/////////////////////////////////////////////////////////
/**
 * An exception thrown by some stack class methods
 * to indicate that the stack is empty 
 */
public class EmptyStackException extends RuntimeException {

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
    public EmptyStackException() {
    } // EmptyStackException()

} // class EmptyStackException
