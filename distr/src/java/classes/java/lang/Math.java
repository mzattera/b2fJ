package java.lang;

/**
 * Mathematical functions.
 * 
 * @author <a href="mailto:bbagnall@mst.net">Brian Bagnall</a>
 */
public final class Math
{

	// Math constants
	public static final double E = 2.71828182845904523536028747135;
	public static final double PI = 3.14159265358979323846264338328;

	static final double LN2 = 0.693147180559945309417232121458;
	static final double LN10 = 2.30258509299404568401799145468;
	
	private static final double PIhalf = PI * 0.5;
	private static final double PIhalfhalf = PI * 0.25;
	private static final double PItwice = PI * 2.0;
	
	private static final double DEG_TO_RAD = 0.0174532925199432957692369076849;
	private static final double RAD_TO_DEG = 57.2957795130823208767981548141;
	
	// dividing by 2 for some kind of safety margin
	private static final float ROUND_FLOAT_MAX = Integer.MAX_VALUE >> 1;
	private static final float ROUND_FLOAT_MIN = -ROUND_FLOAT_MAX;
	// dividing by 2 for some kind of safety margin
	private static final double ROUND_DOUBLE_MAX = Long.MAX_VALUE >> 1;
	private static final double ROUND_DOUBLE_MIN = -ROUND_DOUBLE_MAX;

	// public static boolean isNaN (double d) {
	// return d != d;
	// }

	private Math()
	{
		// private constructor to make sure this class is not instantiated
	}

	/*========================= abs functions =========================*/ 

	/**
	 * Returns the absolute value of a double value. If the argument is not
	 * negative, the argument is returned. If the argument is negative, the
	 * negation of the argument is returned.
	 */
	public static double abs(double a)
	{
		// according to http://www.concentric.net/~Ttwang/tech/javafloat.htm
		return ((a <= 0.0) ? 0.0 - a : a);
	}

	/**
	 * Returns the absolute value of a float value. If the argument is not
	 * negative, the argument is returned. If the argument is negative, the
	 * negation of the argument is returned.
	 */
	public static float abs(float a)
	{
		// according to http://www.concentric.net/~Ttwang/tech/javafloat.htm
		return ((a <= 0.0f) ? 0.0f - a : a);
	}

	/**
	 * Returns the absolute value of an integer value. If the argument is not
	 * negative, the argument is returned. If the argument is negative, the
	 * negation of the argument is returned.
	 * Note that the return value of abs(Integer.MIN_VALUE) is Integer.MIN_VALUE. 
	 */
	public static int abs(int a)
	{
		return ((a < 0) ? -a : a);
	}

	/*========================= signum functions =========================*/ 

	/**
	 * Returns -1, 1 or 0 depending on the sign of f.
	 */
	public static float signum(float f)
	{
		// let NaN, 0.0 and -0.0 fall through
		if (f > 0)
			return 1.0f;
		if (f < 0)
			return -1.0f;

		return f;
	}

	/**
	 * Returns -1, 1 or 0 depending on the sign of f.
	 */
	public static double signum(double d)
	{
		// let NaN, 0.0 and -0.0 fall through
		if (d > 0)
			return 1.0;
		if (d < 0)
			return -1.0;

		return d;
	}

	/*========================= min/max functions =========================*/ 

	/**
	 * Returns the lesser of two integer values.
	 */
	public static int min(int a, int b)
	{
		return ((a < b) ? a : b);
	}

	/**
	 * Returns the lesser of two float values.
	 */
	public static float min(float a, float b)
	{
		if (a < b)
			return a;
		if (a > b)
			return b;
		// early out for non-zero values, NaN falls through
		if (a == b)
		{
			if (a != 0)
				return a;
		
			// handle negative/positive zero
			int ra = Float.floatToRawIntBits(a);
			int rb = Float.floatToRawIntBits(b);		
			return ra < rb ? a : b;
		}
		
		// either a or b must be NaN
		return Float.NaN;		
	}

	/**
	 * Returns the lesser of two double values.
	 * Might not work properly with NaN.
	 */
	public static double min(double a, double b)
	{
		if (a < b)
			return a;
		if (a > b)
			return b;
		
		// TODO: Fix NaN & negative 0.
		// early out for non-zero values, NaN falls through
		if (a == b)
		{
			return a;
		}
		
		// either a or b must be NaN
		return Double.NaN;
	}

	/**
	 * Returns the greater of two integer values.
	 */
	public static int max(int a, int b)
	{
		return ((a > b) ? a : b);
	}

	/**
	 * Returns the greater of two float values.
	 */
	public static float max(float a, float b)
	{
		if (a > b)
			return a;
		if (a < b)
			return b;
		// early out for non-zero values, NaN falls through
		if (a == b)
		{
			if (a != 0)
				return a;
			
			// handle negative/positive zero
			int ra = Float.floatToRawIntBits(a);
			int rb = Float.floatToRawIntBits(b);
			return ra > rb ? a : b;			
		}
		
		// either a or b must be NaN
		return Float.NaN;
	}

	/**
	 * Returns the greater of two double values.
	 * Might not work properly with NaN.
	 */
	public static double max(double a, double b)
	{
		if (a > b)
			return a;
		if (a < b)
			return b;
		
		// TODO: Fix NaN.
		// early out for non-zero values, NaN falls through
		if (a == b)
		{
			return a;
		}
		
		// either a or b must be NaN
		return Double.NaN;
	}

	/*========================= rounding functions =========================*/ 

	/**
	 * Returns the largest (closest to positive infinity) double value that is
	 * not greater than the argument and is equal to a mathematical integer.
	 */
	public static double floor(double a)
	{
		if (a > 0.0)
			return (a > ROUND_DOUBLE_MAX) ? a : (double)(long)a;
		
		// Using !>= instead of < also catches NaN
		if (!(a >= ROUND_DOUBLE_MIN))
			return a;
			
		// if b==a, there were no decimal places (also handles negative zero)
		double b = (long)a;
		return (b == a) ? a : (b - 1.0); 
	}

	/**
	 * Returns the smallest (closest to negative infinity) double value that is
	 * not less than the argument and is equal to a mathematical integer.
	 */
	public static double ceil(double a)
	{
		if (a < 0.0)
			// using double negative such that a==-0.3 yields negative zero
			return (a < ROUND_DOUBLE_MIN) ? a : -(double)(long)(-a);
		
		// Using !<= instead of > also catches NaN
		if (!(a <= ROUND_DOUBLE_MAX))
			return a;

		// if b==a, there were no decimal places (also handles negative zero)
		double b = (long)a;
		return b == a ? a : (b + 1.0); 
	}

	/**
	 * Returns the closest int to the argument.
	 */
	public static int round(float a)
	{
		// check whether rounding required
		if (a < ROUND_FLOAT_MIN || a > ROUND_FLOAT_MAX)
			return (int) a;

		return (int) Math.floor(a + 0.5);
	}

	/**
	 * Returns the closest long to the argument.
	 */
	public static long round(double a)
	{
		// no rounding required
		if (a < ROUND_DOUBLE_MIN || a > ROUND_DOUBLE_MAX)
			return (long) a;

		return (long) Math.floor(a + 0.5);
	}

	/**
	 * Returns the closest mathematical integer to the argument.
	 */
	public static double rint(double a)
	{
		// no rounding required
		if (a < ROUND_DOUBLE_MIN || a > ROUND_DOUBLE_MAX)
			return a;

		if (a < 0)
			return (long) (a - 0.5);

		return (long) (a + 0.5);
	}

	/*========================= trigonometric functions =========================*/ 

	/**
	 * Converts radians to degrees.
	 */
	public static double toDegrees(double angrad)
	{
		return angrad * RAD_TO_DEG;
	}

	/**
	 * Converts degrees to radians.
	 */
	public static double toRadians(double angdeg)
	{
		return angdeg * DEG_TO_RAD;
	}

	// Coefficients of Chebychev-Pade approximation of sin(x)
	private static final double COEFF_SIN_01 = +0.9999999999999999764211612009725588855111;
	private static final double COEFF_SIN_03 = -0.1666666666666652393535348747726998605241;
	private static final double COEFF_SIN_05 = +0.8333333333308337379310370484936396053884e-2;
	private static final double COEFF_SIN_07 = -0.1984126982196706774095496759095037380412e-3;
	private static final double COEFF_SIN_09 = +0.2755731157077441238598803505081829543534e-5;
	private static final double COEFF_SIN_11 = -0.2505048281275841971620868327213456590143e-7;
	private static final double COEFF_SIN_13 = +0.1588305691336997706018324915122156575682e-9;
	
	// Coefficients of Chebychev-Pade approximation of cos(x)
	private static final double COEFF_COS_00 = +0.9999999999999999999696857335603386685305;
	private static final double COEFF_COS_02 = -0.4999999999999999937087712052995206607828;
	private static final double COEFF_COS_04 = +0.4166666666666645245167779123297511727281e-1;
	private static final double COEFF_COS_06 = -0.1388888888886110072336550416638513155394e-2;
	private static final double COEFF_COS_08 = +0.2480158728388399090193789866235476908122e-4;
	private static final double COEFF_COS_10 = -0.2755731309846481722283180474149312600794e-6;
	private static final double COEFF_COS_12 = +0.2087558246021268894742907499544843726236e-8;
	private static final double COEFF_COS_14 = -0.1135338332274935375664837177431026057261e-10;

	// Coefficients of Chebychev-Pade approximation of tan(x)
	private static final double COEFF_TAN_A01 = +1.163208406359175145039529270641357908653;
	private static final double COEFF_TAN_A03 = -0.1551562294271971874673845256091948727104;
	private static final double COEFF_TAN_A05 = +0.3984273522258578489458762347483930831238e-2;
	private static final double COEFF_TAN_A07 = -0.2078385281638820833414798468958567962456e-4;
	private static final double COEFF_TAN_B00 = +1.163208406359175145145702168450150748988;
	private static final double COEFF_TAN_B02 = -0.5428923648802555771443974019753420196884;
	private static final double COEFF_TAN_B04 = +0.2985394096778725847225000070352932045180e-1;
	private static final double COEFF_TAN_B06 = -0.3627755504463478978823711298374056529721e-3;
	private static final double COEFF_TAN_B08 = +0.5798383713759288762215813642061562265476e-6;

	/**
	 * Should only be called for values 0 <= x <= Pi/4.
	 */
	private static double sin_chebypade(double x)
	{
		// works for values -Pi/4 <= x <= Pi/4
		double x2 = x * x;
		return (COEFF_SIN_01+(COEFF_SIN_03+(COEFF_SIN_05+(COEFF_SIN_07+(COEFF_SIN_09+(COEFF_SIN_11+(COEFF_SIN_13)*x2)*x2)*x2)*x2)*x2)*x2)*x;
	}
	
	/**
	 * Should only be called for values 0 <= x <= Pi/4.
	 */
	private static double cos_chebypade(double x)
	{
		// worls for values -Pi/4 <= x <= Pi/4
		double x2 = x * x;
		return (COEFF_COS_00+(COEFF_COS_02+(COEFF_COS_04+(COEFF_COS_06+(COEFF_COS_08+(COEFF_COS_10+(COEFF_COS_12+(COEFF_COS_14)*x2)*x2)*x2)*x2)*x2)*x2)*x2);
	}
	
	/**
	 * Sine function.
	 */
	public static double sin(double x)
	{		
		int neg = 0;
		
		//reduce to interval [-2PI, +2PI]
		x = x % PItwice;
		
		//reduce to interval [0, 2PI]
		if (x < 0)
		{
			neg++;
			x = -x;
		}
		
		//reduce to interval [0, PI]
		if (x > PI)
		{
			neg++;
			x -= PI;
		}
		
		//reduce to interval [0, PI/2]
		if (x > PIhalf)
			x = PI - x;	
		
		double y;		
		if (x < PIhalfhalf)
			y = sin_chebypade(x);
		else
			y = cos_chebypade(PIhalf - x);
		
		return ((neg & 1) == 0) ? y : -y;
	}
	
	/**
	 * Cosine function.
	 */
	public static double cos(double x)
	{
		int neg = 0;
		
		//reduce to interval [-2PI, +2PI]
		x = x % PItwice;
		
		//reduce to interval [0, 2PI]
		if (x < 0)
			x = -x;
		
		//reduce to interval [0, PI]
		if (x > PI)
		{
			neg++;
			x -= PI;
		}
		
		//reduce to interval [0, PI/2]
		if (x > PIhalf)
		{
			neg++;
			x = PI - x;
		}
		
		double y;		
		if (x < PIhalfhalf)
			y = cos_chebypade(x);
		else
			y = sin_chebypade(PIhalf - x);
		
		return ((neg & 1) == 0) ? y : -y;
	}
	
	/**
	 * Tangent function.
	 */
	public static double tan(double x)
	{
		int neg = 0;
		
		//reduce to interval [-PI, +PI]
		x = x % PI;
		
		//reduce to interval [0, PI]
		if (x < 0)
		{
			neg++;
			x = -x;
		}
		
		//reduce to interval [0, PI/2]
		if (x > PIhalf)
		{
			neg++;
			x = PI - x;
		}
		
		boolean inv = x > PIhalfhalf;
		if (inv)
			x = PIhalf - x;
		
		double x2 = x * x;
		double a = (COEFF_TAN_A01+(COEFF_TAN_A03+(COEFF_TAN_A05+(COEFF_TAN_A07)*x2)*x2)*x2)*x;
		double b = COEFF_TAN_B00+(COEFF_TAN_B02+(COEFF_TAN_B04+(COEFF_TAN_B06+(COEFF_TAN_B08)*x2)*x2)*x2)*x2;
		
		double y = inv ? b/a : a/b;		
		return ((neg & 1) == 0) ? y : -y;
	}

	/*==================== inverse trigonometric functions ====================*/ 

	// Coefficients of Chebychev-Pade approximation of arctan(x)
	private static final double COEFF_ARCTAN_A01 = 0.2148098238644807400980437313900874209791;
	private static final double COEFF_ARCTAN_A03 = 0.5822336291803317384927877022348417466425;
	private static final double COEFF_ARCTAN_A05 = 0.5896935461740917629331074424217383345014;
	private static final double COEFF_ARCTAN_A07 = 0.2762961405471209283480112104060944192893;
	private static final double COEFF_ARCTAN_A09 = 0.5998846249230414236694406199010243461828e-1;
	private static final double COEFF_ARCTAN_A11 = 0.5241080670594091071923751216579670668131e-2;
	private static final double COEFF_ARCTAN_A13 = 0.1203082209336721192361545554523165190249e-3;
	private static final double COEFF_ARCTAN_B00 = 0.2148098238644807424616443165027883378015;
	private static final double COEFF_ARCTAN_B02 = 0.6538369038018249190889077431869506291436;
	private static final double COEFF_ARCTAN_B04 = 0.7646772160018242189018026280068075002980;
	private static final double COEFF_ARCTAN_B06 = 0.4311082828151354005327854170265085211728;
	private static final double COEFF_ARCTAN_B08 = 0.1202932940016257879182979096874373869694;
	private static final double COEFF_ARCTAN_B10 = 0.1523641193862141984750802100415527660067e-1;
	private static final double COEFF_ARCTAN_B12 = 0.6791021403500245109987506932563112475291e-3;
	private static final double COEFF_ARCTAN_B14 = 0.4538215780227674758812916817748843935591e-5;

	/**
	 * Should only be called for values 0 <= x <= 1.
	 */
	private static double arctan_chebypade(double x)
	{
		// works for values -1 <= x <= 1
		double x2 = x * x;
		return (COEFF_ARCTAN_A01+(COEFF_ARCTAN_A03+(COEFF_ARCTAN_A05+(COEFF_ARCTAN_A07+(COEFF_ARCTAN_A09+(COEFF_ARCTAN_A11+(COEFF_ARCTAN_A13)*x2)*x2)*x2)*x2)*x2)*x2)*x
			/ (COEFF_ARCTAN_B00+(COEFF_ARCTAN_B02+(COEFF_ARCTAN_B04+(COEFF_ARCTAN_B06+(COEFF_ARCTAN_B08+(COEFF_ARCTAN_B10+(COEFF_ARCTAN_B12+(COEFF_ARCTAN_B14)*x2)*x2)*x2)*x2)*x2)*x2)*x2);
	}
	
	/**
	 * The inverse tangent function.
	 */
	public static double atan(double x)
	{
		boolean neg = x < 0;
		if (neg)
			x = -x;
		
		boolean inv = x > 1;
		if (inv)
			x = 1.0 / x;
		
		double y = arctan_chebypade(x);
		if (inv)
			y = PIhalf - y;
		
		return neg ? -y : y;
	}
	
	/**
	 * The inverse tangent function. This function converts the coordinates x and y to an angle in the range -Pi to Pi.
	 * The angle is relative to the positive x axis, counter clockwise.
	 */
	public static double atan2(double y, double x)
	{
		boolean neg_y = y < 0;
		if (neg_y)
			y = -y;
		
		boolean neg_x = x < 0;
		if (neg_x)
			x = -x;
		
		double r;
		boolean inv;
		if (x == y)
		{
			inv = false;
			r = (x==0) ? 0.0 : PIhalfhalf;
		}
		else
		{
			inv = y > x;
			r = arctan_chebypade(inv ? x/y : y/x);
		}
		
		if (inv)
			if (neg_x)
				r += PIhalf;
			else
				r = PIhalf - r;
		else if (neg_x)
			r = PI - r;
		
		return neg_y ? -r : r;
	}
}
