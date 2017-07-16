/*
 * @(#)DataOutputStream.java	1.00 01/12/14
 *
 * Adapted from the original Sun Microsystems code for leJOS.
 */
package java.io;

/**
 * Transmits java data types as bytes over an OutputStream.
 * @author Brian Bagnall
 * @author Sven Köhler
 */
public class DataOutputStream extends FilterOutputStream implements DataOutput
{	 
	protected int written;
	
	public DataOutputStream(OutputStream out)
	{
		super(out);
	}

	/**
	 * Increases the written counter by the specified value
	 * until it reaches Integer.MAX_VALUE.
	 */
	private void incCount(int value)
	{
		int temp = written + value;		  
		if (temp < 0)
			temp = Integer.MAX_VALUE;
		written = temp;
	}

	/**
	 * Returns the current value of the counter <code>written</code>, 
	 * the number of bytes written to this data output stream so far.
	 * If the counter overflows, it will be wrapped to Integer.MAX_VALUE.
	 *
	 * @return  the value of the <code>written</code> field.
	 * @see	  java.io.DataOutputStream#written
	 */
	public final int size()
	{
		return written;
	}
	
	@Override
	public void write(byte b[], int off, int len) throws IOException
	{
		out.write(b, off, len);
		incCount(len);
	}

	@Override
	public void write(int b) throws IOException
	{
		out.write(b);
		incCount(1);
	}

	public final void writeBoolean(boolean v) throws IOException
	{
		out.write(v ? 1 : 0);
		incCount(1);
	}

	public final void writeByte(int v) throws IOException
	{
		out.write(v);
		incCount(1);
	}
	
	public final void writeBytes(String s) throws IOException
	{
		int len = s.length();
		OutputStream o = this.out;
		
		for (int i=0; i<len; i++)
			o.write(s.charAt(i));
		
		incCount(len);
	}

	public final void writeChar(int v) throws IOException {
		OutputStream o = this.out;
		o.write(v >>> 8);
		o.write(v);
		incCount(2);
	}
	
	public final void writeChars(String s) throws IOException
	{
		int len = s.length();
		OutputStream o = this.out;
		
		for (int i=0; i<len; i++)
		{
			int c = s.charAt(i); 
			o.write(c >>> 8);
			o.write(c);
		}
		
		incCount(len << 1);
	}
	
	public final void writeFloat(float v) throws IOException
	{
		writeInt(Float.floatToIntBits(v));
	}

	public final void writeInt(int v) throws IOException
	{
		OutputStream o = this.out;
		o.write(v >>> 24);
		o.write(v >>> 16);
		o.write(v >>>  8);
		o.write(v);
		incCount(4);
	}

	public final void writeShort(int v) throws IOException
	{
		OutputStream o = this.out;
		o.write(v >>> 8);
		o.write(v);
		incCount(2);
	}

	public final void writeUTF(String s) throws IOException
	{
		int utfCount = countUTFBytes(s);
		writeShort(utfCount);
		writeUTFBytes(s);
	}
	
	private int countUTFBytes(String str) throws IOException
	{
		int len = str.length();
		int count = 0;
		for (int i = 0; i < len; i++)
		{
			int c = str.charAt(i);
			if (c > 0 && c < 128)
				count++;
			else if (c < 2048)
				count += 2;
		  	else
		  		count += 3;
			
			if (count > 0xFFFF)
				throw new IOException("String too large");
		}
		return count;
	}
	
	private void writeUTFBytes(String str) throws IOException
	{
		int len = str.length();
		for (int i = 0; i < len; i++)
		{
			int c = str.charAt(i);
			if (c > 0 && c < 128)
				write(c);
			else if (c < 2048)
			{
				write(0xc0 | (c >> 6));
				write(0x80 | (0x3f & c));
			}
			else
			{
				write(0xe0 | (c >> 12));
				write(0x80 | (0x3f & (c >> 6)));
				write(0x80 | (0x3f & c));
			}
		}
	}
}
	
  
