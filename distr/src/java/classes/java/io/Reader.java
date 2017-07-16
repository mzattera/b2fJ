package java.io;

/**
 * Basic Reader implementation. In contrast to the JDK, this class is NOT synchronized.
 * 
 * @author Sven Köhler
 */
public abstract class Reader implements Closeable
{
	private static final int SKIP_BUF_LEN = 32;
	
	//MISSING protected Object lock
	//MISSING protected Reader(Object lock)
	//MISSING int read(CharBuffer dest)
	
	protected Reader()
	{
		//nothing
	}
	
	public abstract void close() throws IOException;
	
	public void mark(int position) throws IOException
	{
		throw new IOException("mark not supported");
	}
	
	public boolean markSupported()
	{
		return false;
	}
	
	public int read() throws IOException
	{
		char buffer[] = new char[1];
		if (read(buffer, 0, 1) < 0)
			return -1;
		
		return buffer[0];
	}
	
	public int read(char[] cbuf) throws IOException
	{
		return read(cbuf, 0, cbuf.length);
	}
	
	public abstract int read(char[] cbuf, int off, int len) throws IOException;
	
	public boolean ready() throws IOException
	{
		return false;
	}
	
	public void reset() throws IOException
	{
		throw new IOException("mark not supported");
	}
	
	public int skip(int n) throws IOException
	{
		char[] buffer = new char[SKIP_BUF_LEN];
		int nbackup = n;
		
		while (n > 0)
		{
			int readlen = (n >= SKIP_BUF_LEN) ? SKIP_BUF_LEN : (int)n; 			
			int len = this.read(buffer, 0, readlen);
			if (len < 0)
				break;
			
			n -= len;
		}
		
		return nbackup - n;
	}
}
