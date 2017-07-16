package java.io;

public class ByteArrayInputStream extends InputStream
{
	protected byte[] buf;
	protected int count;
	protected int mark;
	protected int pos;
	
	public ByteArrayInputStream(byte[] buf)
	{
		this(buf, 0, buf.length);
	}

	public ByteArrayInputStream(byte[] buf, int off, int len)
	{
		if (len < 0)
			// throw new IllegalArgumentException("length must be positive");
			throw new IllegalArgumentException();
		
		this.buf = buf;
		this.pos = off;
		this.mark = off;
		this.count = off + len;
	}

	@Override
	public int available()
	{
		return this.count - this.pos;
	}

	@Override
	public void close()
	{
		//nothing
	}

	@Override
	public synchronized void mark(int readlimit)
	{
		this.mark = this.pos;
	}

	@Override
	public boolean markSupported()
	{
		return true;
	}

	@Override
	public int read()
	{
		if (this.pos < this.count)
			return this.buf[this.pos++] & 0xFF;
		
		return -1;
	}

	@Override
	public int read(byte[] b, int off, int len)
	{
		if (len < 0)
			throw new IndexOutOfBoundsException("len must be positive");
		if (len == 0)
			return 0;
		
		int max = this.count - this.pos;
		if (max <= 0)
			return -1;
		
		if (len > max)
			len = max;

		System.arraycopy(this.buf, this.pos, b, off, len);
		this.pos += len;
		return len;
	}

	@Override
	public synchronized void reset()
	{
		this.pos = this.mark;
	}

	@Override
	public int skip(int n)
	{
		if (n < 0)
			return 0;
		
		int max = this.count - this.pos;
		if (n > max)
			n = max;
		
		this.pos += n;
		return n;
	}

}
