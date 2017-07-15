package java.io;

public class FilterInputStream extends InputStream
{
	protected InputStream in;
	
	public FilterInputStream(InputStream in)
	{
		this.in = in;
	}
	
	@Override
	public int available() throws IOException
	{
		return this.in.available();
	}
	
	@Override
	public void close() throws IOException
	{
		this.in.close();
	}

	@Override
	public void mark(int readAheadLimit)
	{
		this.in.mark(readAheadLimit);
	}
	
	@Override
	public int read() throws IOException
	{
		return this.in.read();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		return this.in.read(b, off, len);
	}

	@Override
	public void reset() throws IOException
	{
		this.in.reset();
	}
	
	@Override
	public long skip(long n) throws IOException
	{
		return this.in.skip(n);
	}
}
