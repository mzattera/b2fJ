package java.lang;
/**
 * Class to allow an object to execute code. This interface should be implemented
 * by any class which wishes to allow its instances to execute via a thread.
 * <p>
 * The implmenting class must define a single method Run which takes no arguments
 * and returns nothing. This method will be called by a thread when the thread
 * is started. 
 * 
 * @author andy
 */
public interface Runnable {
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     */
    public abstract void run();
}
