
public class SupportedStuff {

	public static void arraycopy(char[] src, int srcOffset, char[] dest, int destOffset, int length) {
		for (int i = 0; i < length; i++)
			dest[i + destOffset] = src[i + srcOffset];
	}

	public static void main(String[] args) {

		float l1, l2;
		l1 = 33.333F;
		l2 = 66.666F;

//		char[] sb = new char[50];
//		int len = MyStringUtils.getFloatChars(l1, sb, 0);
//		for (int j = 0; j < len; ++j) {
//			System.out.println("[" + ((int)sb[j]) + "]");
//		}
//		System.out.println();
//		System.out.println(";;;");
//		System.out.println(sb);
//
//		char[] sc = new char[len];
//		arraycopy(sb, 0, sc, 0, len);
//		System.out.println();
//		System.out.println("===");
//		System.out.println(sb);
//		System.out.println();

		System.out.println(l1);
		System.out.println(l2);

		int i = Float.floatToIntBits(l1);
		l2 = Float.intBitsToFloat(i);

		System.out.print(">> ");
		System.out.println(i);
		System.out.println(l1);
		System.out.println(l2);

		if (l1 > l2)
			System.out.println("LI.");
		else
			System.out.println("LII.");

		l1 = l1 + l2;
		System.out.println(l1);

		l1 = l1 * l2;
		System.out.println(l1);
	}

}
