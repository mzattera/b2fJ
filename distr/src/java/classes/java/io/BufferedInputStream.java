package java.io;

/**
 * This is a minimal implementation of BufferedInputStream.
 * For the time being, it does not support mark/reset.
 */
public class BufferedInputStream extends FilterInputStream
{
	private static final int DEFAULT_BUFFERSIZE = 64;
	private static final int MIN_BUFFERSIZE = 1;
	
	protected final byte[] buf;
	protected int pos;
	protected int count;
	
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
		
		this.buf = new byte[size];
	}

	private void checkOpen() throws IOException
	{
		if (this.count < 0)
			throw new IOException("stream is closed");
	}

	@Override
	public int available() throws IOException
	{
		checkOpen();
		
		return this.count - this.pos + this.in.available();
	}

	@Override
	public void close() throws IOException
	{
		this.count = -1;
		this.in.close();
	}
	
	@Override
	public void mark(int readlimit) {
		//nothing
	}
	
	@Override
	public boolean markSupported() {
		return false;
	}
	
	private boolean fillBuffer() throws IOException
	{
		if (this.pos >= this.count)
		{
			int len = 0;
			//some InputStream may be return 0, which is bad behavior actually
			while (len == 0)
				len = this.in.read(this.buf, 0, this.buf.length);
			if (len < 0)
				return false;
			
			this.pos = 0;
			this.count = len;
		}
		return true;
	}

	@Override
	public int read() throws IOException
	{
		checkOpen();
		
		if (!fillBuffer())
			return -1;
		
		return this.buf[this.pos++] & 0xFF;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		checkOpen();
		
		if (!fillBuffer())
			return -1;
		
		int blen = this.count - this.pos;
		// case 1: buffer is not empty
		if (blen > 0)
		{
			if (blen > len)
				blen = len;
			
			System.arraycopy(this.buf, this.pos, b, off, blen);
			this.pos += blen;
			return blen;
		}
		// case 2: buffer is empty
		return this.in.read(b, off, len);
	}

	@Override
	public synchronized void reset() throws IOException {
		throw new IOException("reset not supported");
	}
	
	@Override
	public int skip(int n) throws IOException
	{
		checkOpen();
		
		int blen = this.count - this.pos;
		if (n <= blen)
		{
			this.pos += (int)n;
			return n;
		}
		
		int norig = n;
		
		//it holds n > blen
		n -= blen;
		this.pos = this.count;
		
		while (n > 0)
		{
			int len = this.in.read(this.buf, 0, this.buf.length);
			if (len < 0)
				break;
			
			n -= len;
		}
		
		return norig - n;
	}
}
