package java.lang;

/**
 * Mathematical functions.
 *
 * @author <a href="mailto:bbagnall@escape.ca">Brian Bagnall</a>
 */
public final class Math {
		
	// Math constants
	public static final double E  = 2.718281828459045;
	public static final double PI = 3.141592653589793;
   public static final double NaN       = 0.0f / 0.0f;
   
   static final float PI2 = 1.570796326794897f;
	static final double ln10      = 2.30258509299405;
	static final double ln2       = 0.69314718055995;
	
        // Used by log() and exp() methods
	private static final float LOWER_BOUND = 0.9999999f;
	private static final float UPPER_BOUND = 1.0f;

	// Used to generate random numbers.
	private static java.util.Random RAND = new java.util.Random(System.currentTimeMillis());

	//public static boolean isNaN (double d) {
	//  return d != d;
	//}
	
	private Math() {} // To make sure this class is not instantiated

   // Private because it only works when -1 < x < 1 but it is used by atan2
   private static double ArcTan(double x)  // Using a Chebyshev-Pade approximation
   {
     double x2=x*x;
     return (0.7162721433f+0.2996857769f*x2)*x/(0.7163164576f+(0.5377299313f+0.3951620469e-1f*x2)*x2);
   }


	/**
	* Returns the smallest (closest to negative infinity) double value that is not
	* less than the argument and is equal to a mathematical integer.
 	*/
	public static double ceil(double a) {
		return ((a<0)?(int)a:(int)(a+1));	
	}
	
	/**
	* Returns the largest (closest to positive infinity) double value that is not
	* greater than the argument and is equal to a mathematical integer.
	*/
	public static double floor(double a) {	
		return ((a<0)?(int)(a-1):(int)a);	
	}
	
	/**
	* Returns the closest int to the argument.
	*/	
	public static int round(float a) {	
		return (int)floor(a + 0.5f);
	}
	
	/**
	* Returns the lesser of two integer values.
	*/
	public static int min(int a, int b) {	
		return ((a<b)?a:b);
	}
	
	/**
	*Returns the lesser of two double values.
	*/
	public static double min(double a, double b) {	
		return ((a<b)?a:b);
	}
	
	/**
	*Returns the greater of two integer values.
	*/
	public static int max(int a, int b) {	
		return ((a>b)?a:b);
	}
	
	/**
	*Returns the greater of two double values.
	*/
	public static double max(double a, double b) {	
		return ((a>b)?a:b);
	}
	
	/**
	* Random number generator.
	* Returns a double greater than 0.0 and less than 1.0
	*/
	public static double random()
  {
    final int MAX_INT = 2147483647;
    int n = MAX_INT;
    
    // Just to ensure it does not return 1.0
    while(n == MAX_INT)
    	n = abs (RAND.nextInt());
        
    return n * (1.0 / MAX_INT);
  }
	
	/**
	* Exponential function.  Returns E^x (where E is the base of natural logarithms).
	* Thanks to David Edwards of England for conceiving the code and Martin E. Nielsen
	* for modifying it to handle large arguments.
        * = sum a^n/n!, i.e. 1 + x + x^2/2! + x^3/3!
        * <P>
        * Seems to work better for +ve numbers so force argument to be +ve.
	*/	
	public static double exp(double a)
	{
	    boolean neg = a < 0 ? true : false;
	    if (a < 0)
                a = -a;
            int fac = 1;
    	    double term = a;
    	    double sum = 0;
    	    double oldsum = 0;
    	    double end;

    	    do {
    	        oldsum = sum;
    	        sum += term;
    
    	        fac++;
        
    	        term *= a/fac;
	        end = sum/oldsum;
      	    } while (end < LOWER_BOUND || end > UPPER_BOUND);

            sum += 1.0f;
            
	    return neg ? 1.0f/sum : sum;
	}
	
	/**
	* Natural log function.  Returns log(a) to base E
	* Replaced with an algorithm that does not use exponents and so
	* works with large arguments.
	* @see <a href="http://www.geocities.com/zabrodskyvlada/aat/a_contents.html">here</a>
	*/
	public static double log(double x)
	{
	        if (x < 1.0)
	                return -log(1.0/x);
	                
	        double m=0.0;
	        double p=1.0;
	        while (p <= x) {
	                m++;
	                p=p*2;
	        }
	        
	        m = m - 1;
	        double z = x/(p/2);
	        
	        double zeta = (1.0 - z)/(1.0 + z);
	        double n=zeta;
	        double ln=zeta;
	        double zetasup = zeta * zeta;
	        
	        for (int j=1; true; j++)
	        {
	                n = n * zetasup;
	                double newln = ln + n / (2 * j + 1);
	                double term = ln/newln;
	                if (term >= LOWER_BOUND && term <= UPPER_BOUND)
	                        return m * ln2 - 2 * ln;
	                ln = newln;
	        }
	}

	/**
	* Power function.  This is a slow but accurate method.
	* Thanks to David Edwards of England for conceiving the code.
	*/
	public static double pow(double a, double b) {
		return exp(b * log(a));
	}
	
	/**
	* Returns the absolute value of a double value. If the argument is not negative, the argument is
   * returned. If the argument is negative, the negation of the argument is returned.
	*/
	public static double abs(double a) {
		return ((a<0)?-a:a);
	}

	/**
	* Returns the absolute value of an integer value. If the argument is not negative, the argument is
   * returned. If the argument is negative, the negation of the argument is returned.
	*/
	public static int abs(int a) {
		return ((a<0)?-a:a);
	}
	
  /**
  * Sine function using a Chebyshev-Pade approximation. Thanks to Paulo Costa for donating the code.
  */
  public static double sin(double x)  // Using a Chebyshev-Pade approximation
  {
    int n=(int)(x/PI2)+1; // reduce to the 4th and 1st quadrants
    if(n<1)n=n-1;
    if ((n&2)==0) x=x-(n&0xFFFFFFFE)*PI2;  // if it from the 2nd or the 3rd quadrants
    else        x=-(x-(n&0xFFFFFFFE)*PI2);

    double x2=x*x;
    return (0.9238318854f-0.9595498071e-1f*x2)*x/(0.9238400690f+(0.5797298195e-1f+0.2031791179e-2f*x2)*x2);
  }

  /**
  * Cosine function using a Chebyshev-Pade approximation. Thanks to Paulo Costa for donating the code.
  */
  public static double cos(double x)
  {
    int n=(int)(x/PI2)+1;
    if(n<1)n=n-1;
    x=x-(n&0xFFFFFFFE)*PI2;  // reduce to the 4th and 1st quadrants

    double x2=x*x;

    float si=1f;
    if ((n&2)!=0) si=-1f;  // if it from the 2nd or the 3rd quadrants
    return si*(0.9457092528f+(-0.4305320537f+0.1914993010e-1f*x2)*x2)/(0.9457093212f+(0.4232119630e-1f+0.9106317690e-3f*x2)*x2);
  }

   /**
  * Square root - thanks to Paulo Costa for donating the code.
  */
  public static double sqrt(double x)
  {
    double root = x, guess=0;

    if(x<0) return NaN;

    // the accuarcy test is percentual
    for(int i=0; (i<16) && ((guess > x*(1+5e-7f)) || (guess < x*(1-5e-7f))); i++)
    {
      root = (root+x/root)*0.5f; // a multiplication is faster than a division
      guess=root*root;   // cache the square to the test
    }
    return root;
  }
	
   /**
   * Tangent function.
   */
   public static double tan(double a) {
      return sin(a)/cos(a);
   }
		
  /**
  * Arc tangent function. Thanks to Paulo Costa for donating the code.
  */
  public static double atan(double x)
  {
    return atan2(x,1);
  }

   /**
  * Arc tangent function valid to the four quadrants
  * y and x can have any value without sigificant precision loss
  * atan2(0,0) returns 0. Thanks to Paulo Costa for donating the code.
  */
  public static double atan2(double y, double x)
  {
    float ax=(float)abs(x);
    float ay=(float)abs(y);

    if ((ax<1e-7) && (ay<1e-7)) return 0f;

    if (ax>ay)
    {
      if (x<0)
      {
        if (y>=0) return ArcTan(y/x)+PI;
        else return ArcTan(y/x)-PI;
      }
      else return ArcTan(y/x);
    }
    else
    {
      if (y<0) return ArcTan(-x/y)-PI/2;
      else return ArcTan(-x/y)+PI/2;
    }
  }

  /**
   * Arc cosine function.
   */
  public static double acos(double a) {
    if ((a<-1)||(a>1)) {
      return Double.NaN;
    }
    return PI/2 - atan(a/sqrt(1 - a * a));
  }
	
   /**
   * Arc sine function.
   */
   public static double asin(double a) {
      return atan(a/sqrt(1-a*a));
   }
	
  /**
   * Converts radians to degrees.
   */
   public static double toDegrees(double angrad) {
      return angrad * (360.0 / (2 * PI));
   }
	
   /**
    * Converts degrees to radians.
    */
   public static double toRadians(double angdeg) {
      return angdeg * ((2 * PI) / 360.0);
   }
}
