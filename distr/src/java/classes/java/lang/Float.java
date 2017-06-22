package java.lang;

/**
 * Minimal Float implementation that supports 
 * floatToIntBits and intBitsToFloat
 *
 * @author Lawrie Griffiths
 */
  public final class Float {

    private Float() {
    }
  
    /**
     * Returns the bit represention of a single-float value.
     * The result is a representation of the floating-point argument 
     * according to the IEEE 754 floating-point "single 
     * precision" bit layout. 
     * <ul>
     * <li>Bit 31 (the bit that is selected by the mask 
     * <code>0x80000000</code>) represents the sign of the floating-point 
     * number. 
     * <li>Bits 30-23 (the bits that are selected by the mask 
     * <code>0x7f800000</code>) represent the exponent. 
     * <li>Bits 22-0 (the bits that are selected by the mask 
     * <code>0x007fffff</code>) represent the significand (sometimes called 
     * the mantissa) of the floating-point number. 
     * <li>If the argument is positive infinity, the result is 
     * <code>0x7f800000</code>. 
     * <li>If the argument is negative infinity, the result is 
     * <code>0xff800000</code>.
     * <p>
     * If the argument is NaN, the result is the integer
     * representing the actual NaN value.  The lejos implementation 
     * behaves like floatToRawIntBits and does not collapse NaN values.
     * </ul>
     * In all cases, the result is an integer that, when given to the 
     * {@link #intBitsToFloat(int)} method, will produce a floating-point 
     * value equal to the argument to <code>floatToRawIntBits</code>.
     * 
     * @param   value   a floating-point number.
     * @return  the bits that represent the floating-point number.
     */
  public static native int floatToIntBits(float value);

    /**
     * Returns the single-float corresponding to a given bit represention.
     * The argument is considered to be a representation of a
     * floating-point value according to the IEEE 754 floating-point
     * "single precision" bit layout.
     * <p>
     * If the argument is <code>0x7f800000</code>, the result is positive
     * infinity.
     * <p>
     * If the argument is <code>0xff800000</code>, the result is negative
     * infinity.
     * <p>
     * If the argument is any value in the range <code>0x7f800001</code> 
     * through <code>0x7fffffff</code> or in the range 
     * <code>0xff800001</code> through <code>0xffffffff</code>, the result is 
     * NaN. All IEEE 754 NaN values of type <code>float</code> are, in effect,
     * lumped together by the Java programming language into a single 
     * <code>float</code> value called NaN.  Distinct values of NaN are only
     * accessible by use of the <code>Float.floatToRawIntBits</code> method.
     * <p>
     * In all other cases, let <i>s</i>, <i>e</i>, and <i>m</i> be three 
     * values that can be computed from the argument: 
     * <blockquote><pre>
     * int s = ((bits >> 31) == 0) ? 1 : -1;
     * int e = ((bits >> 23) & 0xff);
     * int m = (e == 0) ?
     *                 (bits & 0x7fffff) << 1 :
     *                 (bits & 0x7fffff) | 0x800000;
     * </pre></blockquote>
     * Then the floating-point result equals the value of the mathematical 
     * expression <i>s&#183;m&#183;2<sup>e-150</sup></i>.
     *
     * @param   value an integer.
     * @return  the single-format floating-point value with the same bit
     *          pattern.
     */
  public static native float intBitsToFloat(int value);
}

   
