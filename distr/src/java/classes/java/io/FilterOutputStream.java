package java.io;

public class FilterOutputStream extends OutputStream
{
	protected OutputStream out;
	
	public FilterOutputStream(OutputStream out)
	{
		this.out = out;
	}
	
	@Override
	public void close() throws IOException
	{
		try
		{
			this.flush();
		}
		finally
		{
			this.out.close();
		}
	}
	
	@Override
	public void flush() throws IOException
	{
		this.out.flush();
	}

	@Override
	public void write(int b) throws IOException
	{
		this.out.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		this.out.write(b, off, len);
	}
}
