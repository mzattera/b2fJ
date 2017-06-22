package java.util;

/**
 * Pseudo-random number generation.
 */
public class Random
{
  private int iPrevSeed, iSeed;
  
  public Random (long seed)
  {
    iPrevSeed = 1;
    iSeed = (int) seed;
  }

    public Random()
    {
	this(System.currentTimeMillis());
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
}
