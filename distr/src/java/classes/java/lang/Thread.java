package java.lang;

/**
 * A thread of execution (or task). Now handles priorities, daemon threads
 * and interruptions.
 */
public abstract class Thread
{
  /**
   * The minimum priority that a thread can have. The value is 1.
   */
  public final static int MIN_PRIORITY = 1;

 /**
  * The priority that is assigned to the primordial thread. The value is 5.
  */
  public final static int NORM_PRIORITY = 5;

  /**
   * The maximum priority that a thread can have. The value is 10.
   */
  public final static int MAX_PRIORITY = 10;

  // Note 1: This class cannot have a static initializer.

  // Note 2: The following fields are used by the VM.
  // Their sizes and location can only be changed
  // if classes.h is changed accordingly. Needless
  // to say, they are read-only.

  private Thread _TVM_nextThread;
  private int _TVM_waitingOn;
  private int _TVM_sleepUntil;
  private int _TVM_stackFrameArray;
  private int _TVM_stackArray;
  private byte _TVM_stackFrameArraySize;
  private byte _TVM_monitorCount;
  private byte _TVM_threadId; 
  private byte _TVM_state; 
  private byte _TVM_priority; 
  private byte _TVM_interrupted; 
  private byte _TVM_daemon; 

  // Extra instance state follows:
  
  private String name;

  public final boolean isAlive()
  {
    return _TVM_state > 1;
  }    
	  
  public Thread()
  {
    this ("");
  }

  public Thread (String name)
  {
  	Thread t = currentThread();
	if (t == null)
		setPriority(NORM_PRIORITY);
	else
		setPriority(t.getPriority());	
    this.name = name;
  }

  public abstract void run();
  
  public final native void start();
  public static native void yield();
  public static native void sleep (long aMilliseconds) throws InterruptedException;
  public static native Thread currentThread();
  public final native int getPriority();
  /**
   * Set the priority of this thread. Higher number have higher priority.
   * The scheduler will always run the highest priority thread in preference
   * to any others. If more than one thread of that priority exists the
   * scheduler will time-slice them. In order for lower priority threas
   * to run a higher priority thread must cease to be runnable. i.e. it
   * must exit, sleep or wait on a monitor. It is not sufficient to just
   * yield.
   * <P>
   * Threads inherit the priority of their parent. The primordial thread
   * has priority NORM_PRIORITY.
   *
   * @param priority must be between MIN_PRIORITY and MAX_PRIORITY.
   */
  public final native void setPriority(int priority);
  
  /**
   * Set the interrupted flag. If we are asleep we will wake up
   * and an InterruptedException will be thrown.
   */
  public native void interrupt();
  public static native boolean interrupted();
  public final native boolean isInterrupted();
  
  /**
   * Set the daemon flag. If a thread is a daemon thread its existence will
   * not prevent a JVM from exiting.
   */
  public final native boolean isDaemon();
  public final native void setDaemon(boolean on);
  
  /**
   * Join not yet implemented
   */
  public final native void join() throws InterruptedException;
  public final native void join(long timeout) throws InterruptedException;
}



