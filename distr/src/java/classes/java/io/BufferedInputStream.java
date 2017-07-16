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
public class BufferedInputStream extends FilterInputStream
{
	private static final int DEFAULT_BUFFERSIZE = 64;
	private static final int MIN_BUFFERSIZE = 1;
	
	private final byte[] buffer;
	private int offset;
	private int limit;
	
	/**
	 * In the standard Java API the constructor accepts a Writer
	 * object, such as InputStreamWriter.
	 * @param out
	 */
	public BufferedInputStream(InputStream out)
	{
		this(out, DEFAULT_BUFFERSIZE);
	}
	
	public BufferedInputStream(InputStream out, int size)
	{
		super(out);
		
		if (size < MIN_BUFFERSIZE)
			size = MIN_BUFFERSIZE;
		
		this.buffer = new byte[size];
	}
	
	@Override
	public void close() throws IOException
	{
		this.limit = -1;
		this.in.close();
	}

	@Override
	public int available() throws IOException
	{
		checkOpen();
		
		return this.limit - this.offset + this.in.available();
	}
	
	private void checkOpen() throws IOException
	{
		if (this.limit < 0)
			throw new IOException("stream is closed");
	}
	
	private boolean fillBuffer() throws IOException
	{
		if (this.offset >= this.limit)
		{
			int len = 0;
			//some InputStream may be return 0, which is bad behavior actually
			while (len == 0)
				len = this.in.read(this.buffer, 0, this.buffer.length);
			if (len < 0)
				return false;
			
			this.offset = 0;
			this.limit = len;
		}
		return true;
	}

	@Override
	public int read() throws IOException
	{
		checkOpen();
		
		if (!fillBuffer())
			return -1;
		
		return this.buffer[this.offset++] & 0xFF;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		checkOpen();
		
		if (!fillBuffer())
			return -1;
		
		int blen = this.limit - this.offset;
		// case 1: buffer is not empty
		if (blen > 0)
		{
			if (blen > len)
				blen = len;
			
			System.arraycopy(this.buffer, this.offset, b, off, blen);
			this.offset += blen;
			return blen;
		}
		// case 2: buffer is empty
		return this.in.read(b, off, len);
	}

	@Override
	public int skip(int n) throws IOException
	{
		checkOpen();
		
		int blen = this.limit - this.offset;
		if (n <= blen)
		{
			this.offset += (int)n;
			return n;
		}
		
		int norig = n;
		
		//it holds n > blen
		n -= blen;
		this.offset = this.limit;
		
		while (n > 0)
		{
			int len = this.in.read(this.buffer, 0, this.buffer.length);
			if (len < 0)
				break;
			
			n -= len;
		}
		
		return norig - n;
	}
	
	
}
