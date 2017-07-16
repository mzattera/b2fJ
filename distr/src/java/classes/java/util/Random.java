package java.util;

/**
 * Pseudo-random number generation.
 */
public class Random
{
	//TODO make this class JDK compliant
	
  private int iPrevSeed, iSeed;
  
  public Random (int seed)
  {
    iPrevSeed = 1;
    iSeed = (int) seed;
  }

    public Random()
    {
    	this((int)System.currentTimeMillis());
    }
  
  /**
   * @return A random positive or negative integer.
   */
  public int nextInt()
  {
    int pNewSeed = (iSeed * 48271) ^ iPrevSeed;
    iPrevSeed = iSeed;
    iSeed = pNewSeed;
    return pNewSeed;
  }
  
  	private int next(int bits)
  	{
  		// just some work in progress, should replace nextInt()
  		int mask;
  		if (bits < 32)
  			mask = (1 << bits) - 1;
  		else
  			mask = -1;  		
  		return nextInt() & mask;
  	}

    /**
     * Returns a random integer in the range 0...n-1.
     * @param n  the bound
     * @return A random integer in the range 0...n-1.
     */
    public int nextInt (int n)
    {
	int m = nextInt() % n;
	return m >= 0 ? m : m + n;
    }

    /**
     * Returns a random boolean in the range 0-1.
     * @return A random boolean in the range 0-1.
     */
    public boolean nextBoolean()
    {
		return this.next(1) != 0;    	
    }

    public float nextFloat()
    {
    	// we need 24 bits number to create 23 bit mantissa
		return this.next(24) * 0x1p-24f;
    }
}
