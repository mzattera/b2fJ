package java.util;

/**
 * Represents a long set of bits.
 */
public class BitSet implements Cloneable
{
	private static final int INITIAL_SIZE = 4;
	private static final int CAPACITY_INCREMENT_NUM = 3;	//numerator of the increment factor
	private static final int CAPACITY_INCREMENT_DEN = 2;	//denominator of the increment factor
	
	private int[] bits;
	
	//MISSING void and(BitSet)
	//MISSING void andNot(BitSet)
	//MISSING void clear(int, int)
	//MISSING void flip(int, int)
	//MISSING BitSet get(int, int)
	//MISSING int hashCode()
	//MISSING boolean intersects
	//MISSING int nextClearBit(int)
	//MISSING int nextSetBit(int)
	//MISSING void or(BitSet)
	//MISSING void set(int, int)
	//MISSING void set(int, int, boolean)
	//MISSING void xor(BitSet)

	public BitSet()
	{
		this.bits = new int[INITIAL_SIZE];
	}
	
	public BitSet(int nbits)
	{
		if (nbits < 0)
			throw new NegativeArraySizeException();
		
		this.bits = new int[(nbits + 31) / 32];
	}
	
	public int cardinality()
	{
		int r = 0;
		int len = this.bits.length;
		for (int i=0; i<len; i++)
			r += Integer.bitCount(this.bits[i]);
		return r;
	}
	
	public void clear()
	{
		int l = this.bits.length;
		for (int i=0; i<l; i++)
			this.bits[i] = 0;
	}
  
	public void clear (int n)
	{
		if (n < 0)
			throw new IndexOutOfBoundsException();
		
		int idx = n / 32;
		if (idx < this.bits.length)
			this.bits[idx] &=  ~(1 << (n % 32));
	}

	private void ensureCapacity(int chunks)
	{
		int el = this.bits.length;
		if (el < chunks)
		{
			el = el * CAPACITY_INCREMENT_NUM / CAPACITY_INCREMENT_DEN + 1;
			while (el < chunks)
				el = el * CAPACITY_INCREMENT_NUM / CAPACITY_INCREMENT_DEN + 1;
			
			int[] newData = new int[el];
			System.arraycopy(this.bits, 0, newData, 0, this.bits.length);
			this.bits = newData;
		}
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof BitSet))
			return false;
		
		BitSet o = (BitSet)obj;
		int lo = o.bits.length;
		int lt = this.bits.length;
		int lc = Math.min(lo, lt);
		
		for (int i=0; i<lc; i++)
			if (o.bits[i] != this.bits[i])
				return false;
		
		for (int i=lc; i<lo; i++)
			if (o.bits[i] != 0)
				return false;
		
		for (int i=lc; i<lt; i++)
			if (this.bits[i] != 0)
				return false;
		
		return true;
	}

	public void flip(int n)
	{
		if (n < 0)
			throw new IndexOutOfBoundsException();
		
		int idx = n / 32;
		this.ensureCapacity(idx + 1);
		
		this.bits[idx] ^= (1 << (n % 32));
	}

	public boolean get(int n)
	{
		if (n < 0)
			throw new IndexOutOfBoundsException();
		
		int idx = n / 32;		
		return idx < this.bits.length && (this.bits[idx] & (1 << (n % 32))) != 0;
	}
	
	public int length()
	{
		int len = this.bits.length;
		for (int i=len-1; i>=0; i--)
			if (this.bits[i] != 0)
				return i * 32 + highBitNum(this.bits[i]);
		
		return 0;
	}
	
	private static int highBitNum(int v)
	{
		if (v < 0)
			return 31;
		
		int r = 0;
		if (v >= 1 << 16) { r += 16; v >>= 16; }		
		if (v >= 1 << 8) { r += 8; v >>= 8; }		
		if (v >= 1 << 4) { r += 4; v >>= 4; }		
		if (v >= 1 << 2) { r += 2; v >>= 2; }		
		return r + v;
	}
	
	public boolean isEmpty()
	{
		int len = this.bits.length;
		for (int i=0; i<len; i++)
			if (this.bits[i] != 0)
				return false;
		
		return true;
	}
	
	public void set(int n)
	{
		if (n < 0)
			throw new IndexOutOfBoundsException();
		
		int idx = n / 32;
		this.ensureCapacity(idx + 1);
		
		this.bits[idx] |= (1 << (n % 32));
	}

	public void set(int n, boolean val)
	{
		if (val)
			this.set(n);
		else
			this.clear(n);
	}
	
	public int size()
	{
		return this.bits.length * 32;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		int len = this.bits.length;
		boolean colon = false;
		for (int i=0; i<len; i++)
			for (int j=0; j<32; j++)
				if ((this.bits[i] & (1 << j)) != 0)
				{
					if (colon)
						sb.append(", ");
					sb.append(i * 32 + j);
					colon = true;
				}

		sb.append('}');
		return sb.toString();
	}
}
