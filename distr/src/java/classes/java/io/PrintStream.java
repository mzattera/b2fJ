package java.io;

/**
 * Minimal implementation of PrintStream.
 * 
 * Currently only implements the mandatory write
 * method and println.
 * 
 * @author Lawrie Griffiths
 *
 */
public class PrintStream extends OutputStream {	
    
    private static final char NEW_LINE = '\n';
    
	private OutputStream os;
	
    public PrintStream(OutputStream os) {
    	this.os = os;  	
    }
    
	public void write (int c) {
    	try {
            synchronized (this)
            {
                os.write(c);
            }
    	} catch (IOException ioe) {};
    }
    
    /**
     * Flush any pending output in the stream
     */
	public void flush()
    {
    	try {
    		os.flush();
    	} catch (IOException ioe) {}      
    }
    
    private synchronized void print0(String s)
    {
        for(int i=0;i<s.length();i++) {
            write(s.charAt(i));
        }
        //TODO optional flush
    }
    
    /**
     * Writes a string followed by a newline character
     * to the underlying output stream.
     * 
     * @param s the string to print
     */
    private synchronized void println0(String s) {
        for(int i=0;i<s.length();i++) {
            write(s.charAt(i));
        }
        write(NEW_LINE);
        //TODO make flush optional
        flush();
    }
    
    /**
     * Writes a newline character
     * to the underlying output stream.
     */
    public synchronized void println() {
   		write(NEW_LINE);
   		//TODO make flush optional
   		flush();
    }    
        
    /**
     * Writes a string to the underlying output stream.
     * 
     * @param s the string to print
     */
    public void print(String s) {
    	print0(s);
    }    
        
    /**
     * Writes a string to the underlying output stream.
     * 
     * @param s the string to print
     */
    public void println(String s) {
    	println0(s);
    }    
}
