package java.io;

/**
 * This is a minimal implementation of BufferedWriter.
 * Normally BufferedWriter extends the abstract class Writer. 
 * The main reason for NXJ including this is to allow the new-line
 * character in text.
 * NOTE: Text writing is not synchronized either like standard Java.
 * 
 * @author BB
 *
 */
public class BufferedOutputStream extends FilterOutputStream
{
	private static final int DEFAULT_BUFFERSIZE = 64;
	private static final int MIN_BUFFERSIZE = 1;
	
	private final byte[] buffer;
	private int limit;
	
	/**
	 * In the standard Java API the constructor accepts a Writer
	 * object, such as OutputStreamWriter.
	 * @param out
	 */
	public BufferedOutputStream(OutputStream out)
	{
		this(out, DEFAULT_BUFFERSIZE);
	}
	
	public BufferedOutputStream(OutputStream out, int size)
	{
		super(out);
		
		if (size < MIN_BUFFERSIZE)
			size = MIN_BUFFERSIZE;
		
		this.buffer = new byte[size];
	}
	
	@Override
	public void close() throws IOException
	{
		try
		{
			this.flushAny();
		}
		finally
		{
			this.out.close();
		}
	}
	
	private void flushAny() throws IOException
	{
		if (this.limit > 0)
		{
			this.out.write(this.buffer, 0, this.limit);
			this.limit = 0;
		}
	}
	
	private int flushFull() throws IOException
	{
		if (this.limit >= this.buffer.length)
		{
			this.out.write(this.buffer, 0, this.limit);
			this.limit = 0;
		}
		return this.buffer.length - this.limit;
	}

	@Override
	public void flush() throws IOException
	{
		this.flushAny();
		this.out.flush();
	}
	
	@Override
	public void write(int c) throws IOException
	{
		this.flushFull();		
		this.buffer[this.limit++] = (byte)c;
	}

	@Override
	public void write(byte[] c, int off, int len) throws IOException
	{
		while (len > 0)
		{
			int plen = this.flushFull();
			if (plen > len)
				plen = len;
			
			System.arraycopy(c, off, this.buffer, this.limit, plen);
		
			len -= plen;
			off += plen;
			this.limit += plen;
		}
	}
}
