package java.io;

public class ByteArrayOutputStream extends OutputStream
{
	private static final int INITIAL_CAPACITY = 10;
	private static final int CAPACITY_INCREMENT_NUM = 3;	//numerator of the increment factor
	private static final int CAPACITY_INCREMENT_DEN = 2;	//denominator of the increment factor
	
	protected byte[] buf;
	protected int count;
	
	public void ensureCapacity(int minCapacity)
	{
		int cl = buf.length;
		if (cl < minCapacity)
		{
			cl = cl * CAPACITY_INCREMENT_NUM / CAPACITY_INCREMENT_DEN + 1;
			while (cl < minCapacity)
				cl = cl * CAPACITY_INCREMENT_NUM / CAPACITY_INCREMENT_DEN + 1;
			
			byte[] newData = new byte[cl];
			System.arraycopy(buf, 0, newData, 0, count);
			buf = newData;
		}
	}

	public ByteArrayOutputStream()
	{
		this.buf = new byte[INITIAL_CAPACITY];
	}
	
	public ByteArrayOutputStream(int size)
	{
		if (size < 0)
			// throw new IllegalArgumentException("size is negative");
			throw new IllegalArgumentException();
		
		this.buf = new byte[size];
	}
	
	@Override
	public void close()
	{
		//nothing
	}
	
	public void reset()
	{
		this.count = 0;
	}
	
	public int size()
	{
		return this.count;
	}
	
	public byte[] toByteArray()
	{
		byte[] r = new byte[this.count];
		System.arraycopy(this.buf, 0, r, 0, this.count);
		return r;
	}
	
	@Override
	public String toString()
	{
		return new String(this.buf, 0, this.count);
	}
	
	/**
	 * @deprecated use {@link #toString(String)} instead
	 */
	@Deprecated
	public String toString(int hibyte)
	{
		hibyte <<= 8;
		
		int c = this.count;
		char[] cbuf = new char[c];
		
		for (int i=0; i<c; i++)
			cbuf[i] = (char)(this.buf[i] & 0xFF | hibyte);
		
		return new String(cbuf);
	}
	
	public String toString(String charset)
	{
		return new String(this.buf, 0, this.count, charset);
	}

	@Override
	public void write(byte[] b, int off, int len)
	{
		this.ensureCapacity(this.count + len);
		System.arraycopy(b, off, this.buf, this.count, len);
		this.count += len;
	}

	@Override
	public void write(int b)
	{
		this.ensureCapacity(this.count + 1);
		this.buf[this.count++] = (byte)b;
	}

	public void writeTo(OutputStream os) throws IOException
	{
		os.write(this.buf, 0, this.count);
	}
}
