package java.lang;

/**
 * All classes extend this one, implicitly.
 */
public class Object
{
  public boolean equals (Object aOther)
  {
    return this == aOther;
  }

  public int hashCode()
  {
    return getDataAddress (this);
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

  /**
   * This is the same as calling wait(0).
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
  
  /**
   * Returns the empty string. It's here to satisfy javac.
   */
  public String toString()
  {
    return "";
  }

  /**
   * Returns <code>null</code>. It's here to satisfy javac.
   */
  public final Class getClass()
  {
    return null;
  }

  private native static int getDataAddress (Object obj);
}







