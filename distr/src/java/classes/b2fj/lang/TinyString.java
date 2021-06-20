package b2fj.lang;

/**
 * An string of bytes.
 */
public final class TinyString implements CharSequence, Comparable<String> {

    final byte capacity;
    final byte[] characters;

    int length = 0;

    public TinyString() {
        this((byte)0,0);
    }

    public TinyString(byte capacity) {
        this.capacity = capacity;
        this.characters = new byte[capacity];
        this.length = 0;
        for (int i = 0; i < capacity; i++)
            characters[i] = ' ';
    }

    private TinyString(byte capacity, int len) {
        this.capacity = capacity;
        this.characters = new byte[len];
        this.length = len;
    }

    /**
     * Create a String from a byte array
     *
     * @param b the byte array
     */
    public TinyString(byte[] b) {
        this(b, 0, b.length);
    }

    /**
     * Create a String from a byte array
     *
     * @param b the byte array
     */
    public TinyString(byte[] b, int off, int len) {
        this((byte)len,len);
        System.arraycopy(b, off, characters, 0, len);
    }

    /**
     * Create a String from a byte array
     *
     * @param b the byte array
     * @deprecated
     */
    @Deprecated
    public TinyString(byte[] b, int hibyte, int off, int len) {
        this((byte)len,len);
        // for (int i = 0; i < len; i++)
        //    characters[i] = (b[off + i]);
        System.arraycopy(b, off, characters, 0, len);
    }

    /**
     * Create a String from a byte array
     *
     * @param b       the byte array
     * @param charset ignored - assumed to be US ASCII
     */
    public TinyString(byte[] b, int off, int len, String charset) {
        this(b, off, len);
    }

    /**
     * Create a String from a byte array
     *
     * @param b       the byte array
     * @param charset ignored - assumed to be US ASCII
     */
    public TinyString(byte[] b, String charset) {
        this(b, 0, b.length, charset);
    }

    /**
     * Create a String from a character array
     *
     * @param c the character array
     */
    public TinyString(char[] c) {
        this(c, 0, c.length);
    }

    /**
     * Create a String from a character array.
     *
     * @param c   the character array
     * @param off the offset - usually 0
     * @param len the length of the String - must not be greater than c.length
     */
    public TinyString(char[] c, int off, int len) {
        this((byte)len,len);
        arraycopy(c, off, characters, 0, len);
    }

    public TinyString(TinyString s) {
        this(s.capacity,s.length);
        copy(s);
    }

    public TinyString(java.lang.String s) {
        this((byte)s.length(),s.length());
        copy(s);
    }

    public void copy(byte[] b) {
        copy(b,0,b.length);
    }

    public void copy(byte[] b, int off, int len) {
        int minLen = Math.min(b.length,capacity);
        System.arraycopy(b, off, characters, 0, minLen);
    }

    private void copy(String string) {
        for (int i = 0; i < string.length() & i<this.capacity; i++) {
            characters[i] = (byte) string.charAt(i);
        }
        this.length= string.length();
    }

    private void copy(TinyString s) {
        copy(s.characters);
    }

    /**
     * Copies one array to another.
     */
    public static void arraycopy(char[] src, int srcOffset, byte[] dest, int destOffset, int length) {
        for (int i = 0; i < length; i++)
            dest[i + destOffset] = (byte) (src[i + srcOffset] & 0xFF);
    }

    /**
     * Return the character at the given index
     *
     * @return the characters at the given index
     */
    public char charAt(int index) {

        if (index > characters.length)
            throw new StringIndexOutOfBoundsException(index);

        return ((char) (characters[index] & 0xFF));

    }

    public int compareTo(TinyString str) {
        int len1 = this.characters.length;
        int len2 = str.characters.length;

        int len = (len1 < len2) ? len1 : len2;

        for (int i = 0; i < len; i++) {
            byte c1 = this.characters[i];
            byte c2 = str.characters[i];

            if (c1 != c2)
                return (c1 < c2) ? -1 : 1;
        }

        if (len1 != len2)
            return (len1 < len2) ? -1 : 1;

        return 0;
    }

    public int compareTo(String str) {
        int len1 = this.characters.length;
        int len2 = str.length();

        int len = (len1 < len2) ? len1 : len2;

        for (int i = 0; i < len; i++) {
            byte c1 = this.characters[i];
            byte c2 = (byte) str.charAt(i);

            if (c1 != c2)
                return (c1 < c2) ? -1 : 1;
        }

        if (len1 != len2)
            return (len1 < len2) ? -1 : 1;

        return 0;
    }

    /**
     * Compares the String with an Object
     *
     * @return true if the String is equal to the object, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        // also catches other == null
        if (!(other instanceof TinyString))
            return false;

        TinyString os = (TinyString) other;
        if (os.characters.length != characters.length)
            return false;

        for (int i = 0; i < characters.length; i++) {
            if (characters[i] != os.characters[i])
                return false;
        }

        return true;
    }

    public byte[] getBytes() {
        return this.characters;
    }

    /**
     * Special version of hash that returns the same value the same String values
     */
    @Override
    public int hashCode() {
        // computation is compatible with JDK, otherwise Java 7 String-switch-case,
        // which is based on hashCode, will fail.
        int h = 0;
        for (int i = 0; i < this.characters.length; i++) {
            h = 31 * h + this.characters[i];
        }
        return h;
    }

    /**
     * Returns true, if and only if this string is of zero length.
     *
     * @return true if this string is of zero length, false otherwise.
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Return the length of the String in characters
     *
     * @return the length of the String
     */
    public int length() {
        return length;
    }

    public CharSequence subSequence(int start, int end) {
        return this.substring(start, end);
    }

    /**
     * Return substring from starting position to the end of the String
     *
     * @param start the starting position
     * @return the substring
     */
    public TinyString substring(int start) {
        return substring(start, characters.length);
    }

    /**
     * Return substring from starting index to position before end index
     *
     * @param start the start index
     * @param end   the end index (not included)
     * @return the substring
     */
    public TinyString substring(int start, int end) {
        if (start < 0 || start > characters.length)
            throw new StringIndexOutOfBoundsException(start);
        if (end > characters.length)
            throw new StringIndexOutOfBoundsException(end);
        if (end < start)
            throw new StringIndexOutOfBoundsException(end - start);

        return new TinyString(characters, start, end - start);
    }

    @Override
    public java.lang.String toString() {
        return new java.lang.String(characters,0,length);
    }

    public void ensureCapacity(byte newLen) {
        if (newLen > capacity) {
            throw new java.lang.ArrayIndexOutOfBoundsException();
        }
    }

    public TinyString set(int i) {
        this.set(i, 10);
        return this;
    }

    public TinyString set(String s) {
        copy(s);
        return this;
    }

    public TinyString set(TinyString s) {
        copy(s);
        return this;
    }

    /**
     * For use by {@link Integer}
     */
    TinyString set(int i, int radix) {
        //System.out.print('v');
        //System.out.println(java.lang.String.valueOf(i));
        int newLen = StringUtils.exactStringLength(i, radix);
        //System.out.println("len="+java.lang.String.valueOf(len));
        ensureCapacity((byte) newLen);
        StringUtils.getIntBytes(characters, newLen, i, radix);
        this.length = newLen;
        //System.out.println("length="+this.length);
        return this;
    }

    /**
     * Appends a string with no null checking
     */
    public TinyString append(TinyString s) {
        if (s == null)
            return this;

        // Reminder: compact code more important than speed
        byte[] sc = s.characters;
        byte sl = (byte) s.length;

        byte newlen = (byte) (this.length + sl);
        this.ensureCapacity(newlen);

        System.arraycopy(sc, 0, characters, this.length, sl);
        this.length = newlen;

        return this;
    }

    /**
     * Appends a string with no null checking
     */
    public TinyString append(String s) {
        if (s == null)
            return this;

        // Reminder: compact code more important than speed
        byte sl = (byte) s.length();

        byte newlen = (byte) (this.length + sl);
        this.ensureCapacity(newlen);

        for (int i = 0; i < sl; i++)
            characters[i + this.length] = (byte) (s.charAt(i) & 0xFF);
        this.length = newlen;

        return this;
    }

    public void setLength(byte newLen) {
        if (newLen < 0)
            throw new IndexOutOfBoundsException();

        ensureCapacity((byte) newLen);

        for (int i = length; i < newLen; i++)
            characters[i] = 0x32;

        length = newLen;
    }

    public void clear() {
        for (int i = 0; i < capacity; i++)
            characters[i] = ' ';
        length = 0;
    }

    /**
     * Just some utility methods for the wrapper classes as well as StringBuffer and StringBuilder.
     *
     * @author Sven Köhler
     */
    private static class StringUtils {

        /**
         * Low-level convert of int to char[].
         *
         * @param p position of the character after the last digit
         */
        static int getIntBytes(byte[] buf, int p, int v, int radix) {
            int v2 = (v <= 0) ? v : -v;

            do {
                buf[--p] = (byte) (Character.forDigit(-(v2 % radix), radix) & 0xFF);
                v2 /= radix;
            } while (v2 != 0);

            if (v < 0)
                buf[--p] = '-';

            return p;
        }

        /**
         * Exact size of buffer for {@link #getIntChars(char[], int, int, int)}.
         */
        static int exactStringLength(int v, int radix) {
            int c = (v < 0) ? 1 : 0;
            do {
                c++;
                v /= radix;
            } while (v != 0);

            return c;
        }

    }
}