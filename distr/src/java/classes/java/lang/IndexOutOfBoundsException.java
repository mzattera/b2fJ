package java.lang;

public class IndexOutOfBoundsException extends RuntimeException{

    /**
     * Constructs an <code>IndexOutOfBoundsException</code> with no
     * detail message.
     */
    public IndexOutOfBoundsException() {
        super();
    }

    /**
     * Constructs an <code>IndexOutOfBoundsException</code> class
     * with the specified detail message.
     *
     * @param   s   the detail message.
     */
    public IndexOutOfBoundsException(String s) {
        super(s);
    }

}
