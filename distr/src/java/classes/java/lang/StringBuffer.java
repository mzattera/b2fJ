package java.lang;

/**
 * An expandable string of characters. Actually not very expandable!
 * 09/25/2001 added number formatting thanks to Martin E. Nielsen.
 * You must ensure that the buffer is large enough to take the formatted
 * number.
 *<P>
 * @author <a href="mailto:martin@egholm-nielsen.dk">Martin E. Nielsen</a>
 */
public final class StringBuffer
{
  char[] characters;
  int curPos = 0;

  /**
   * Conversion between integers from 0 to 9 and their respective
   * chars.
   **/
  private static final char[] numbers = { '0', '1', '2', '3', '4',
					    '5', '6', '7', '8', '9' };

  /**
   * The value of <i>log(10)</i> used for converting from base
   * <i>e</i> to base 10.
   **/
  private static final float log10 = 2.30258509f;

  public StringBuffer () {
  	characters = new char[0];
  }
  
  public StringBuffer (String aString)
  {
    characters = aString.toCharArray();
  }

  public StringBuffer (int length)
  {
    characters = new char[length];
  }

  public StringBuffer delete(int start, int end)
  {
        if (start >= 0 && start < end && start < curPos)
        {
                if (end >= curPos)
                        end = curPos;
                else
                        System.arraycopy(characters, end, characters, start, curPos-end);
                        
                curPos -= end-start;
        }
        
        return this;
  }

  public StringBuffer append (String s)
  {
    // Reminder: compact code more important than speed
    char[] sc = s.toCharArray();
    int cl = characters.length;
    int sl = sc.length;
    char [] nc = characters;
    if (sl + curPos > cl)
    {
        nc = new char[sl + curPos];
        System.arraycopy (characters, 0, nc, 0, curPos);
    }
    System.arraycopy (sc, 0, nc, curPos, sl);
    characters = nc;
    curPos += sl;
    return this;
  }

  public StringBuffer append (java.lang.Object aObject)
  {
    return append (aObject.toString());
  }

  public StringBuffer append (boolean aBoolean)
  {
    return append (aBoolean ? "true" : "false");
  }
  
  public StringBuffer append (char aChar)
  {
    return append (new String (new char[] { aChar }, 0, 1));
  }

  public StringBuffer append (int aInt)
  {
	if ( aInt < 0 ) {
	    characters[ curPos++ ] = '-';
	    aInt = -aInt;
	} // if

	int pow = ( int )Math.floor( Math.log( aInt ) / log10 );

	int div = 0;
	while ( pow >= 0 ) {
	    div = ( int ) ( aInt / (int)Math.pow( 10, pow ) );
	    
	    characters[ curPos++ ] = numbers[ div ];
	    aInt -= div * (int)Math.pow( 10, pow );
	    pow--;
	} // while
	
	return this;
  }

  public StringBuffer append (long aLong)
  {
        return append("<longs not supported>");
  }

  public StringBuffer append (float aFloat)
  {
    try {
        append (aFloat, 8);
    } catch (ArrayIndexOutOfBoundsException e) {
        curPos = Math.min(characters.length, curPos);
    }
    
    return this;
  }

  public StringBuffer append (double aDouble)
  {
    try {
        append ((float)aDouble, 8);
    } catch (ArrayIndexOutOfBoundsException e) {
        curPos = Math.min(characters.length, curPos);
    }
    
    return this;
  }
  
  public String toString()
  {
    return new String (characters, 0, curPos);
  }

  public char charAt(int i)
  {
        return characters[i];
  }
  
  public int length()
  {
        return curPos;
  }

  /**
  * Retrieves the contents of the StringBuffer in the form of an array of characters.
  */
  public char [] getChars()
  {
    return characters;
  }
  
    /**
     * Helper method for converting floats and doubles.
     *
     * @author Martin E. Nielsen
     **/
    private StringBuffer append( float number, int significantDigits ) {

	if ( number == 0 ) {
	    characters[ curPos++ ] = '0';
	    return this;
	} // if
	    
	if ( number < 0 ) {
	    characters[ curPos++ ] = '-';
	    number = -number;
	} // if

	// calc. the power (base 10) for the given number:
	int pow = ( int )Math.floor( Math.log( number ) / log10 );
	int exponent = 0;

	// use exponential formatting if number too big or too small
	if ( pow < -3 || pow > 6 ) {
	    exponent = pow;
	    number /= Math.exp( Math.ln10 * exponent );
	} // if

	// Recalc. the pow if exponent removed and d has changed
	pow = ( int )Math.floor( Math.log( number ) / log10 );

	// Decide how many insignificant zeros there will be in the
	// lead of the number.
	int insignificantDigits = -Math.min( 0, pow );

	// Force it to start with at least "0." if necessarry
	pow = Math.max( 0, pow );
        double divisor = Math.pow(10, pow);
        
	// Loop over the significant digits (17 for double, 8 for float)
	for ( int i = 0, end = significantDigits+insignificantDigits, div; i < end; i++  ) {

	    // Add the '.' when passing from 10^0 to 10^-1
	    if ( pow == -1 ) {
		characters[ curPos++ ] = '.';
	    } // if
	    
	    // Find the divisor
	    div = ( int ) ( number / divisor );
	    // This might happen with 1e6: pow = 5 ( instead of 6 )
	    if ( div == 10 ) {
		characters[ curPos++ ] = '1';
		characters[ curPos++ ] = '0';
	    } // if
	    else {
//		characters[ curPos ] = numbers[ div ];
		characters[ curPos ] = (char)(div + '0');
		curPos++;
	    } // else

	    number -= div * divisor;
	    divisor /= 10.0;
	    pow--;

	    // Break the loop if we have passed the '.'
	    if ( number == 0 && divisor < 0.1 ) break;
	} // for

	// Remove trailing zeros
  	while ( characters[ curPos-1 ] == '0' )
  	    curPos--;

	// Avoid "4." instead of "4.0"
	if ( characters[ curPos-1 ] == '.' )
	    curPos++;

	// Restore the exponential format
	if ( exponent != 0 ) {
	    characters[ curPos++ ] = 'E';
	    append( exponent );
	} // if
	
	return this;
    }
}


