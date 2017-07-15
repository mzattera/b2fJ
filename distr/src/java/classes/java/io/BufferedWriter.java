package java.io;

/**
 * NOTE: Text writing is not synchronized either like standard Java.
 * 
 * @author Sven Köhler
 */
public class BufferedWriter extends Writer
{
	private static final int DEFAULT_BUFFERSIZE = 64;
	private static final int MIN_BUFFERSIZE = 1;
	
	private final Writer parent;
	private final char[] buffer;
	private int limit;
	
	/**
	 * In the standard Java API the constructor accepts a Writer
	 * object, such as OutputStreamWriter.
	 * @param out
	 */
	public BufferedWriter(Writer out)
	{
		this(out, DEFAULT_BUFFERSIZE);
	}
	
	public BufferedWriter(Writer out, int size)
	{
		if (size < MIN_BUFFERSIZE)
			size = MIN_BUFFERSIZE;
		
		this.parent = out;
		this.buffer = new char[size];
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
			this.parent.close();
		}
	}
	
	private void flushAny() throws IOException
	{
		if (this.limit > 0)
		{
			this.parent.write(this.buffer, 0, this.limit);
			this.limit = 0;
		}
	}
	
	private int flushFull() throws IOException
	{
		if (this.limit >= this.buffer.length)
		{
			this.parent.write(this.buffer, 0, this.limit);
			this.limit = 0;
		}
		return this.buffer.length - this.limit;
	}

	@Override
	public void flush() throws IOException
	{
		this.flushAny();
		this.parent.flush();
	}
	
	public void newLine() throws IOException
	{
		//TODO use constant for this
		this.write('\r');
		this.write('\n');
	}
	
	@Override
	public Writer append(CharSequence str, int start, int end) throws IOException
	{
		int len = end - start;
		while (len > 0)
		{
			int plen = this.flushFull();
			if (plen > len)
				plen = len;
			
			for (int i=0; i<plen; i++)
				this.buffer[this.limit + i] = str.charAt(start + i);
		
			len -= plen;
			start += plen;
			this.limit += plen;
		}
		return this;
	}

	@Override
	public void write(char[] c, int off, int len) throws IOException
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

	@Override
	public void write(int c) throws IOException
	{
		this.flushFull();		
		this.buffer[this.limit++] = (char)c;
	}

	@Override
	public void write(String str, int off, int len) throws IOException
	{
		while (len > 0)
		{
			int plen = this.flushFull();
			if (plen > len)
				plen = len;

			str.getChars(off, off+plen, this.buffer, this.limit);
		
			len -= plen;
			off += plen;
			this.limit += plen;
		}
	}
}
