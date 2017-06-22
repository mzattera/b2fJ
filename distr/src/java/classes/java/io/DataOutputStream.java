 /*
 * @(#)DataOutputStream.java   1.00 01/12/14
 *
 * Adapted from the original Sun Microsystems code for leJOS.
 * @author Brian Bagnall
 */
package java.io;

public class DataOutputStream extends OutputStream {
    
   protected int written;
   protected OutputStream out;
   
   public DataOutputStream(OutputStream out) {
      this.out = out;
   }

   /**
   * Increases the written counter by the specified value
   * until it reaches Integer.MAX_VALUE.
   */
   private void incCount(int value) {
      int temp = written + value;
        
      //if (temp < 0)
      //temp = Integer.MAX_VALUE;
      written = temp;
   }

   /**
   * Writes the specified byte (the low eight bits of the argument 
   * <code>b</code>) to the underlying output stream. If no exception 
   * is thrown, the counter <code>written</code> is incremented by 
   * <code>1</code>.
   * <p>
   * Implements the <code>write</code> method of <code>OutputStream</code>.
   *
   * @param      b   the <code>byte</code> to be written.
   * @exception  IOException  if an I/O error occurs.
   * @see        java.io.FilterOutputStream#out
   */
   public synchronized void write(int b) throws IOException {
      out.write(b);
      incCount(1);
   }

   /**
   * Writes <code>len</code> bytes from the specified byte array 
   * starting at offset <code>off</code> to the underlying output stream. 
   * If no exception is thrown, the counter <code>written</code> is 
   * incremented by <code>len</code>.
   *
   * @param      b     the data.
   * @param      off   the start offset in the data.
   * @param      len   the number of bytes to write.
   * @exception  IOException  if an I/O error occurs.
   * @see        java.io.FilterOutputStream#out
   */
   public synchronized void write(byte b[], int off, int len) throws IOException {
      out.write(b, off, len);
      incCount(len);
   }

    /**
     * Flushes this data output stream. This forces any buffered output 
     * bytes to be written out to the stream. 
     * <p>
     * The <code>flush</code> method of <code>DataOuputStream</code> 
     * calls the <code>flush</code> method of its underlying output stream.
     *
     * @exception  IOException  if an I/O error occurs.
     * @see        java.io.FilterOutputStream#out
     * @see        java.io.OutputStream#flush()
     */
    public void flush() throws IOException {
	out.flush();
    }

   /**
   * Writes a <code>boolean</code> to the underlying output stream as 
   * a 1-byte value. The value <code>true</code> is written out as the 
   * value <code>(byte)1</code>; the value <code>false</code> is 
   * written out as the value <code>(byte)0</code>. If no exception is 
   * thrown, the counter <code>written</code> is incremented by 
   * <code>1</code>.
   *
   * @param      v   a <code>boolean</code> value to be written.
   * @exception  IOException  if an I/O error occurs.
   * @see        java.io.FilterOutputStream#out
   */
   public final void writeBoolean(boolean v) throws IOException {
      out.write(v ? 1 : 0);
      incCount(1);
   }

   /**
   * Writes out a <code>byte</code> to the underlying output stream as 
   * a 1-byte value. If no exception is thrown, the counter 
   * <code>written</code> is incremented by <code>1</code>.
   *
   * @param      v   a <code>byte</code> value to be written.
   * @exception  IOException  if an I/O error occurs.
   * @see        java.io.FilterOutputStream#out
   */
   public final void writeByte(int v) throws IOException {
      out.write(v);
      incCount(1);
   }

    /**
     * Writes a <code>short</code> to the underlying output stream as two
     * bytes, high byte first. If no exception is thrown, the counter 
     * <code>written</code> is incremented by <code>2</code>.
     *
     * @param      v   a <code>short</code> to be written.
     * @exception  IOException  if an I/O error occurs.
     * @see        java.io.FilterOutputStream#out
     */
   public final void writeShort(int v) throws IOException {
      OutputStream out = this.out;
      out.write((v >>> 8) & 0xFF);
      out.write((v >>> 0) & 0xFF);
      incCount(2);
   }

   /**
   * Writes a <code>char</code> to the underlying output stream as a 
   * 2-byte value, high byte first. If no exception is thrown, the 
   * counter <code>written</code> is incremented by <code>2</code>.
   *
   * @param      v   a <code>char</code> value to be written.
   * @exception  IOException  if an I/O error occurs.
   * @see        java.io.FilterOutputStream#out
   */
   public final void writeChar(int v) throws IOException {
      OutputStream out = this.out;
      out.write((v >>> 8) & 0xFF);
      out.write((v >>> 0) & 0xFF);
      incCount(2);
   }

   /**
   * Writes an <code>int</code> to the underlying output stream as four
   * bytes, high byte first. If no exception is thrown, the counter 
   * <code>written</code> is incremented by <code>4</code>.
   *
   * @param      v   an <code>int</code> to be written.
   * @exception  IOException  if an I/O error occurs.
   * @see        java.io.FilterOutputStream#out
   */
   public final void writeInt(int v) throws IOException {
      OutputStream out = this.out;
      //for(byte i=24;i>=0;i-=8)
      //   out.write((int)(v >>>  i) & 0xFF);
      out.write((v >>> 24) & 0xFF);
      out.write((v >>> 16) & 0xFF);
      out.write((v >>>  8) & 0xFF);
      out.write((v >>>  0) & 0xFF);
      incCount(4);
   }

   /**
   * Writes a <code>long</code> to the underlying output stream as eight
   * bytes, high byte first. In no exception is thrown, the counter 
   * <code>written</code> is incremented by <code>8</code>.
   *
   * @param      v   a <code>long</code> to be written.
   * @exception  IOException  if an I/O error occurs.
   * @see        java.io.FilterOutputStream#out
   */
/*   public final void writeLong(long v) throws IOException {
      OutputStream out = this.out;
      out.write((int)(v >>> 56) & 0xFF);
      out.write((int)(v >>> 48) & 0xFF);
      out.write((int)(v >>> 40) & 0xFF);
      out.write((int)(v >>> 32) & 0xFF);
      out.write((int)(v >>> 24) & 0xFF);
      out.write((int)(v >>> 16) & 0xFF);
      out.write((int)(v >>>  8) & 0xFF);
      out.write((int)(v >>>  0) & 0xFF);
      //for(byte i=56;i>=0;i-=8)
      //   out.write((int)(v >>>  i) & 0xFF);
      incCount(8);
   }
*/
   /**
   * Converts the float argument to an <code>int</code> using the 
   * <code>floatToIntBits</code> method in class <code>Float</code>, 
   * and then writes that <code>int</code> value to the underlying 
   * output stream as a 4-byte quantity, high byte first. If no 
   * exception is thrown, the counter <code>written</code> is 
   * incremented by <code>4</code>.
   *
   * @param      v   a <code>float</code> value to be written.
   * @exception  IOException  if an I/O error occurs.
   * @see        java.io.FilterOutputStream#out
   * @see        java.lang.Float#floatToIntBits(float)
   */
   public final void writeFloat(float v) throws IOException {
      writeInt(Float.floatToIntBits(v));
   }

   /**
   * Converts the double argument to a <code>long</code> using the 
   * <code>doubleToLongBits</code> method in class <code>Double</code>, 
   * and then writes that <code>long</code> value to the underlying 
   * output stream as an 8-byte quantity, high byte first. If no 
   * exception is thrown, the counter <code>written</code> is 
   * incremented by <code>8</code>.
   *
   * @param      v   a <code>double</code> value to be written.
   * @exception  IOException  if an I/O error occurs.
   * @see        java.io.FilterOutputStream#out
   * @see        java.lang.Double#doubleToLongBits(double)
   */
   //public final void writeDouble(double v) throws IOException {
      //writeLong(Double.doubleToLongBits(v));
   //}

   /**
   * Returns the current value of the counter <code>written</code>, 
   * the number of bytes written to this data output stream so far.
   * If the counter overflows, it will be wrapped to Integer.MAX_VALUE.
   *
   * @return  the value of the <code>written</code> field.
   * @see     java.io.DataOutputStream#written
   */
   public final int size() {
      return written;
   }
}
