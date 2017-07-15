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
	private OutputStream os;
	
    public PrintStream(OutputStream os) {
    	this.os = os;  	
    }
    
    @Override
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
    @Override
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
        print0(s);
        write('\n');
        //TODO make flush optional
        flush();
    }
    
    /**
     * Writes a newline character
     * to the underlying output stream.
     */
    public synchronized void println() {
   		write('\n');
   		//TODO make flush optional
   		flush();
    }    
    
    /*** print() Delegates ***/
    
    public void print(boolean v)
    {
    	print0(String.valueOf(v));
    }
    
    public void print(char v)
    {
    	print0(String.valueOf(v));
    }
    
    public void print(char[] v)
    {
    	print0(String.valueOf(v));
    }
    
    public void print(double v)
    {
    	print0(String.valueOf(v));
    }
    
    public void print(float v)
    {
    	print0(String.valueOf(v));
    }
    
    public void print(int v)
    {
    	print0(String.valueOf(v));
    }
    
    public void print(long v)
    {
    	print0(String.valueOf(v));
    }
    
    public void print(Object v)
    {
    	print0(String.valueOf(v));
    }
    
    /**
     * Writes a string to the underlying output stream.
     * 
     * @param s the string to print
     */
    public void print(String s) {
    	print0(String.valueOf(s));
    }
    
    /*** println() Delegates ***/
    
    public void println(boolean v)
    {
    	println0(String.valueOf(v));
    }
    
    public void println(char v)
    {
    	println0(String.valueOf(v));
    }
    
    public void println(char[] v)
    {
    	println0(String.valueOf(v));
    }
    
    public void println(double v)
    {
    	println0(String.valueOf(v));
    }
    
    public void println(float v)
    {
    	println0(String.valueOf(v));
    }
    
    public void println(int v)
    {
    	println0(String.valueOf(v));
    }
    
    public void println(long v)
    {
    	println0(String.valueOf(v));
    }
    
    public void println(Object v)
    {
    	println0(String.valueOf(v));
    }
    
    public void println(String s)
    {
    	println0(String.valueOf(s));
    }
}
