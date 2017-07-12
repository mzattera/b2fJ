/**
 * Tests that CCodeWriter prints out same thing than ByteWriter (+ ClassToC utility).
 */
package test;

import java.io.FileOutputStream;
import java.io.IOException;

import js.tinyvm.io.BEByteWriter;
import js.tinyvm.io.BECCodeWriter;
import js.tinyvm.io.IByteWriter;
import js.tinyvm.io.LEByteWriter;
import js.tinyvm.io.LECCodeWriter;

/**
 * @author maxi
 *
 */
public class TestCodeWriter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			IByteWriter w;

			w = new BEByteWriter(new FileOutputStream("D:\\BEByteWriter.o", false));
			test(w);
			w = new LEByteWriter(new FileOutputStream("D:\\LEByteWriter.o", false));
			test(w);
			w = new BECCodeWriter(new FileOutputStream("D:\\BECCodeWriter.txt", false));
			test(w);
			w = new LECCodeWriter(new FileOutputStream("D:\\LECCodeWriter.txt", false));
			test(w);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println ("Finished.");
	}

	private static void test(IByteWriter w) throws IOException {
		w.writeU1(0x01);
		w.writeU2(0x0102);
		w.writeU4(0x01020304);
		w.writeU8(0x0102030405060708L);

		w.writeBoolean(true);
		w.writeBoolean(false);
		w.writeByte((byte) 0x01);
		w.writeChar('x');
		w.writeShort(0x0102);
		w.writeInt(0x01020304);
		w.writeLong(0x0102030405060708L);

		w.writeFloat(Float.intBitsToFloat(0x01020304));
		w.writeDouble(Double.longBitsToDouble(0x0102030405060708L));

		byte[] b = { 1, 2, 3, 4 };
		w.write(b);
		w.write(b[0]);

		w.close();
	}

}
