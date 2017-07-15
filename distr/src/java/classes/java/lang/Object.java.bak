package java.lang;

import lejos.nxt.VM;

/**
 * All classes extend this one, implicitly.
 */
public class Object
{
    /**
     * Creates a shallow copy of the supplied Object
     * @param old Object to clone
     * @return A shallow copy of the Object
     */
    private static final native Object cloneObject(Object old);

    /**
     * Create a copy of this object, using a shallow copy.
     * @return The new copy of the Object
     * @throws java.lang.CloneNotSupportedException
     */
	protected Object clone() throws CloneNotSupportedException
	{
		if (this instanceof Cloneable)
        	return cloneObject(this);

		throw new CloneNotSupportedException();
	}
	
	public boolean equals (Object aOther)
	{
		return this == aOther;
	}
	
	/**
	 * @deprecated not implemented in leJOS 
	 */
	@Deprecated
	protected void finalize()
	{
		//nothing
	}

	/**
	 *Returns <code>null</code>. It's here to satisfy javac.
	 */
	public final Class<?> getClass()
	{
        // The compiler seems to gnerate calls to this method that do not use the
        // return value. So for now to avoid breaking code we simply return null.
		//throw new UnsupportedOperationException();
        return VM.getVM().getClass(this);
	}

	public int hashCode()
	{
		return System.identityHashCode(this);
	}

	/**
	 * Wake up one thread blocked on a wait(). Must be synchronized on
	 * this object otherwise an IllegalMonitorStateException will be thrown.
	 * <P>
	 * If multiple threads are waiting, higher priority threads will be woken
	 * in preference, otherwise the thread that gets woken is essentially
	 * random. 
	 */
	public final native void notify();
	
	/**
	 * Wake up all threads blocked on a wait(). Must be synchronized on
	 * this object otherwise an IllegalMonitorStateException will be thrown.
	 */
	public final native void notifyAll();
	
	public String toString()
	{	
		//TODO it should be:
		// return this.getClass().getName() + '@' + Integer.toHexString(this.hashCode());
		int hash = System.identityHashCode(this);
		return "Object@"+Integer.toHexString(hash);
	}
	
	/**
	 * This is the same as calling wait(0).
	 * TODO make this a Java method that calls wait(0) since native methods are expensive?
	 */
	public final native void wait() throws InterruptedException;
	
	/**
	 * Wait until notified. Must be synchronized on this object otherwise
	 * an IllegalMonitorStateException will be thrown. The wait can
	 * terminate if one of the following things occurs:
	 * <ol>
	 * <li>notify() or notifyAll() is called.
	 * <li>The calling thread is interrupted.
	 * <li>The timeout expires.
	 * </ol>
	 * @param timeout maximum time in milliseconds to wait. Zero means forever.
	 */
	public final native void wait(long timeout) throws InterruptedException;
	
	public final void wait(long timeout, int nanos) throws InterruptedException
	{
		//rounding up
		if (nanos > 0)
			timeout++;
		
		this.wait(timeout);
	}
}







