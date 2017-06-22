package b2fj.memory;

/**
 * This is very basic memory access.
 *
 * @author  Massimiliano Zattera
 * @version 0.1
 */
public class Memory {
    
    /**
     * Returns content of given memory location.
     */
    public static native int peek(int addr);
    
    /**
     * Write a value into given memory location.
     */
    public static native void poke(int addr, int value);
}